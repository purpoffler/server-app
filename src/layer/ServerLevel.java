package layer;

import dto.UserPackage;
import layer.enums.ExpectedDataType;
import utlis.ConsoleHelper;
import utlis.DateCalculator;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerLevel implements Runnable {
    private final BlockingQueue<UserPackage> inputQueue;
    private final BlockingQueue<String> resultValidateQueue;
    private boolean isClientDisconnected = false;

    public ServerLevel(BlockingQueue<UserPackage> inputQueue, BlockingQueue<String> resultValidateQueue) {
        this.inputQueue = inputQueue;
        this.resultValidateQueue = resultValidateQueue;
    }

    public void run() {
        while (true) {
            try (Connection connection = new Connection()) {
                ConsoleHelper.writeSystemMessage("Клиент подключился");
                while (true) {
                    String word = connection.receive(); // ждём пока клиент что-нибудь нам напишет
                    isClientDisconnected = false;
                    //ConsoleHelper.writeSystemMessage(word);
                    if (!word.isEmpty()) {
                        String[] packetBlocks = word.split("\\|");
                        //Создаем dto UserPackage
                        String date = DateCalculator.getDate();
                        UserPackage userPackage = new UserPackage(packetBlocks[0], packetBlocks[1], ExpectedDataType.valueOf(packetBlocks[2].trim().toUpperCase()), packetBlocks[3], packetBlocks[4], connection.getIp(), date);

                        inputQueue.put(userPackage);
                        //Забираем результат проверки пакета из очереди
                        String resultValidatePacket = resultValidateQueue.poll(500, TimeUnit.MILLISECONDS);
                        //ConsoleHelper.writeSystemMessage("То что забралось из очереди " + resultValidatePacket);
                        if (resultValidatePacket != null) {
                            connection.send("Привет, это Сервер! Подтверждаю, вы написали : " + word + " " + "\u001B[32m" + resultValidatePacket + "\u001B[0m" + "\n");
                        }
                    } else {
                        connection.send("false");
                    }
                }
            } catch (InterruptedException e) {
                ConsoleHelper.writeSystemMessage("Ошибка в считывании пакета");
            } catch (ClassNotFoundException e) {
                ConsoleHelper.writeSystemMessage("Клиент ничего не написал. Ошибка в ServerLevel в методе .receive()");
            } catch (IOException e) {
                if (!isClientDisconnected) {
                    ConsoleHelper.writeSystemMessage("Клиент отключился, жду нового клиента...");
                    isClientDisconnected = true;
                }
            }
        }
    }
}