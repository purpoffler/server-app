package layer;

import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.socket.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ConsoleHelper;
import utlis.DateCalculator;
import utlis.ServerConfig;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerLevel implements Runnable {
    private final BlockingQueue<UserPackage> inputQueue = ServerConfig.getInputQueue();
    private final BlockingQueue<String> resultValidateQueue = ServerConfig.getResultValidateQueue();
    private boolean isClientDisconnected = false;
    private static final Logger log = LoggerFactory.getLogger(ServerLevel.class);

    public void run() {
        while (true) {
            try (Connection connection = new Connection()) {
                ConsoleHelper.writeSystemMessage("Клиент подключился");
                log.debug("Клиент подключился");
                while (true) {
                    String word = connection.receive(); // ждём пока клиент что-нибудь нам напишет
                    isClientDisconnected = false;
                    log.debug("Сообщение от клиента:" + word);
                    if (!word.isEmpty()) {
                        String[] packetBlocks = word.split("\\|");

                        String date = DateCalculator.getDate();
                        UserPackage userPackage = new UserPackage(packetBlocks[0], packetBlocks[1], ExpectedDataType.valueOf(packetBlocks[2].trim().toUpperCase()), packetBlocks[3], packetBlocks[4], connection.getIp(), date);

                        inputQueue.put(userPackage);

                        String resultValidatePacket = resultValidateQueue.poll(500, TimeUnit.MILLISECONDS);
                        log.debug("То что забралось из очереди " + resultValidatePacket);
                        if (resultValidatePacket != null) {
                            connection.send("Привет, это Сервер! Подтверждаю, вы написали : " + word + " " + ServerConfig.getColorGreen() + resultValidatePacket + ServerConfig.getColorDefault() + "\n");
                            log.debug("Отправляем клиенту пакет: " + word + " Результат проверки пакета: " + resultValidatePacket);
                        }
                    } else {
                        connection.send("false");
                        log.debug("Сообщение от клиента пусто, отправляем ему false");
                    }
                }
            } catch (InterruptedException e) {
                log.error("Ошибка в считывании пакета");
            } catch (ClassNotFoundException e) {
                log.warn("Клиент ничего не написал. Ошибка в ServerLevel в методе .receive()");
            } catch (IOException e) {
                if (!isClientDisconnected) {
                    ConsoleHelper.writeSystemMessage("Клиент отключился, жду нового клиента...");
                    log.info("Клиент отключился, жду нового клиента...");
                    isClientDisconnected = true;
                }
            }
        }
    }
}