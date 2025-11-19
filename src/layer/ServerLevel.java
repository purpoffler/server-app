package layer;

import config.ServerConfig;
import layer.logger.CustomLogger;
import layer.socket.Connection;
import utlis.ConsoleHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ServerLevel implements Runnable {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
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
                    if (word != null) {
                        serverConfig.getInputQueue().put(word + "|" + connection.getIp());
                        log.debug("Отправляем на валидацию: " + word);
                        String resultValidatePacket = serverConfig.getResultValidateQueue().poll(500, TimeUnit.MILLISECONDS);
                        log.debug("То что забралось из очереди " + resultValidatePacket);
                        if (resultValidatePacket != null) {
                            String response = String.format("Привет, это Сервер! Подтверждаю, вы написали : %s %s",
                                    word,
                                    serverConfig.getColorGreen() + resultValidatePacket + serverConfig.getColorDefault());
                            connection.send(response + "\n");
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