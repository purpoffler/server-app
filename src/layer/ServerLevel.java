package layer;

import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.logger.CustomLogger;
import layer.socket.Connection;
import utlis.ConsoleHelper;
import utlis.DateCalculator;
import config.ServerConfig;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


public class ServerLevel implements Runnable {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private final BlockingQueue<UserPackage> inputQueue = serverConfig.getInputQueue();
    private final BlockingQueue<String> resultValidateQueue = serverConfig.getResultValidateQueue();
    private boolean isClientDisconnected = false;
    private static final CustomLogger log = new CustomLogger(ServerLevel.class.getSimpleName());

    public void run() {
        while (true) {
            try (Connection connection = new Connection()) {
                ConsoleHelper.writeSystemMessage("Клиент подключился");
                log.debug("Клиент подключился");
                while (true) {
                    String word = connection.receive();
                    isClientDisconnected = false;
                    log.debug("Сообщение от клиента:" + word);
                    if (!(word == null)) {
                        String[] packetBlocks = word.split("\\|");

                        UserPackage userPackage = new UserPackage(
                                packetBlocks[0],
                                packetBlocks[1],
                                ExpectedDataType.valueOf(packetBlocks[2].trim().toUpperCase()),
                                packetBlocks[3],
                                packetBlocks[4],
                                connection.getIp(),
                                DateCalculator.getDate());
                        log.debug("Отправляем пакет на валидацию: " + userPackage);
                        inputQueue.put(userPackage);

                        String resultValidatePacket = resultValidateQueue.poll();
                        log.debug("То что забралось из очереди " + resultValidatePacket);
                        if (resultValidatePacket != null) {
                            connection.send("Привет, это Сервер! Подтверждаю, вы написали : " + word + " " + serverConfig.getColorGreen() + resultValidatePacket + serverConfig.getColorDefault() + "\n");
                            log.debug("Отправляем клиенту пакет: " + word + " Результат проверки пакета: " + resultValidatePacket);
                        }
                    } else {
                        connection.send("false");
                        log.debug("Сообщение от клиента пусто, отправляем ему false");
                    }
                }
            } catch (InterruptedException e) {
                log.error("Ошибка в считывании пакета", e);
            } catch (IOException e) {
                if (!isClientDisconnected) {
                    ConsoleHelper.writeSystemMessage("Клиент отключился, жду нового клиента...");
                    log.debug("Клиент отключился, жду нового клиента...");
                    isClientDisconnected = true;
                }
            }
        }
    }
}