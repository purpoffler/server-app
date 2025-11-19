package layer;

import config.ServerConfig;
import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.exceptions.IncorrectPacketException;
import layer.logger.CustomLogger;
import utlis.DateCalculator;

import java.util.zip.CRC32;

public class ValidationLevel implements Runnable {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private static final CustomLogger log = new CustomLogger(ValidationLevel.class.getSimpleName());

    @Override
    public void run() {
        while (true) {
            try {
                String clientInput = serverConfig.getInputQueue().take();
                UserPackage userPackage = parseClientInput(clientInput);
                if (!checkSignature(userPackage.signature())) {
                    throw new IncorrectPacketException("Неправильная сигнатура пакета");
                }
                if (!checkCRC32(userPackage.data(), userPackage.clientCRC32())) {
                    throw new IncorrectPacketException("CRC32 не совпадает");
                }
                if (!serverConfig.getResultValidateQueue().offer("Пакет в норме")) {
                    log.debug("Очередь полна, положительный ответ проверки пакета не добавился");
                }
                log.debug("Я положил в очередь resultValidateQueue положительный ответ проверки пакета");
                serverConfig.getProcessingQueue().put(userPackage);
                log.debug("Положил dto в processingQueue");
            } catch (InterruptedException e) {
                log.error("Ошибка при извлечении данных из inputQueue", e);
            } catch (IncorrectPacketException e) {
                log.error(e.getMessage(), e);
                sendNegativeAnswer();
            }
        }
    }

    private UserPackage parseClientInput(String clientInput) {
        String[] packetBlocks = clientInput.split("\\|");
        return new UserPackage(
                packetBlocks[0],
                packetBlocks[1],
                ExpectedDataType.valueOf(packetBlocks[2].trim().toUpperCase()),
                packetBlocks[3],
                packetBlocks[4],
                packetBlocks[5],
                DateCalculator.getDate());
    }

    private boolean checkCRC32(String data, String clientCRC32) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        long value = crc32.getValue();
        String serverCRC32 = String.valueOf(value);
        return serverCRC32.equals(clientCRC32);
    }

    private boolean checkSignature(String clientSignature) {
        return serverConfig.getSignature().equals(clientSignature);
    }

    private void sendNegativeAnswer() {
        if (serverConfig.getResultValidateQueue().offer("Пакет поломался")) {
            log.debug("Я положил в очередь отрицательный ответ проверки пакета");
        } else {
            log.debug("Очередь полна, отрицательный ответ не добавился");
        }
    }
}
