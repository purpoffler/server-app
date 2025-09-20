package layer;

import utlis.ConsoleHelper;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerLevel implements Runnable {
    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<String> resultValidateQueue;

    public ServerLevel(BlockingQueue<String> inputQueue, BlockingQueue<String> resultValidateQueue) {
        this.inputQueue = inputQueue;
        this.resultValidateQueue = resultValidateQueue;
    }

    public void run() {
        try (Connection connection = new Connection()) {
            ConsoleHelper.writeSystemMessage("Клиент подключился");
            while (true) {

                String word = connection.receive(); // ждём пока клиент что-нибудь нам напишет
                //ConsoleHelper.writeSystemMessage(word);

                if (!word.isEmpty()) {
                    inputQueue.put(word);
                    //ConsoleHelper.writeMessage("Какой пакет получили"  + word);

                    String resultValidatePacket = resultValidateQueue.poll(700, TimeUnit.MILLISECONDS);
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
            ConsoleHelper.writeSystemMessage("Ошибка во время отправки ответа");
        } catch (IOException e) {
            ConsoleHelper.writeSystemMessage("Клиент отключился");
        }
    }
}