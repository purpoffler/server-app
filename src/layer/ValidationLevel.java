package layer;

import layer.dto.UserPackage;
import layer.logger.CustomLogger;
import config.ServerConfig;

import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class ValidationLevel implements Runnable {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private final BlockingQueue<UserPackage> inputQueue = serverConfig.getInputQueue();
    private final BlockingQueue<UserPackage> processingQueue = serverConfig.getProcessingQueue();
    private final BlockingQueue<String> resultValidateQueue = serverConfig.getResultValidateQueue();
    private final String signature = serverConfig.getSignature();
    private static final CustomLogger log = new CustomLogger(ValidationLevel.class.getSimpleName());

    @Override
    public void run() {
        while (true) {
            try {
                UserPackage userPackage = inputQueue.take();
                String clientSignature = userPackage.signature();
                String data = userPackage.data();
                String clientCRC32 = userPackage.clientCRC32();
                log.debug("Результат проверки CRC32" + String.valueOf(checkCRC32(data, clientCRC32)));
                log.debug("Результат проверки сигнатуры" + String.valueOf(checkSignature(clientSignature)));
                if (checkSignature(clientSignature)) {
                    if (checkCRC32(data, clientCRC32)) {
                        if (!resultValidateQueue.offer("Пакет в норме")) {
                            log.debug("Очередь полна, положительный ответ проверки пакета не добавился");
                        }
                        log.debug("Я положил в очередь положительный ответ проверки пакета");
                        processingQueue.put(userPackage);
                        log.debug("Положил dto в processingQueue");
                    } else {
                        log.debug("CRC32 не совпадает");
                        sendNegativeAnswer();
                    }
                } else {
                    log.debug("Неправильная сигнатура пакета");
                    sendNegativeAnswer();
                }
            } catch (InterruptedException e) {
                log.error("Ошибка при извлечении данных из inputQueue", e);
            }
        }
    }

    // Проверка контрольной суммы
    private boolean checkCRC32(String data, String clientCRC32) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        long value = crc32.getValue();
        String serverCRC32 = String.valueOf(value);
        return serverCRC32.equals(clientCRC32);
    }

    // Проверка Сигнатуры
    private boolean checkSignature(String clientSignature) {
        return signature.equals(clientSignature);
    }

    // Отправка негативного ответа в очередь resultValidateQueue
    private void sendNegativeAnswer() {
        if (resultValidateQueue.offer("Пакет поломался")) {
            log.debug("Я положил в очередь отрицательный ответ проверки пакета");
        } else {
            log.debug("Очередь полна, отрицательный ответ не добавился");
        }
    }
}
