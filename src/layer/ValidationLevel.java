package layer;

import dto.UserPackage;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class ValidationLevel implements Runnable {
    private final BlockingQueue<UserPackage> inputQueue;
    private final BlockingQueue<UserPackage> processingQueue;
    private final BlockingQueue<String> resultValidateQueue;
    private final String signature = "zWj`Jjkg";


    public ValidationLevel(BlockingQueue<UserPackage> inputQueue, BlockingQueue<String> resultValidateQueue, BlockingQueue<UserPackage> processingQueue) {
        this.inputQueue = inputQueue;
        this.processingQueue = processingQueue;
        this.resultValidateQueue = resultValidateQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                UserPackage userPackage = inputQueue.take();
                String clientSignature = userPackage.getSignature();
                String data = userPackage.getData();
                String clientCRC32 = userPackage.getClientCRC32();
                //ConsoleHelper.writeSystemMessage(String.valueOf(checkCRC32(data, clientCRC32)));
                if (checkSignature(clientSignature)) {
                    if (checkCRC32(data, clientCRC32)) {
                        if (!resultValidateQueue.offer("Пакет в норме")) {
                            ConsoleHelper.writeSystemMessage("Очередь полна, объект не добавился");
                        }
                        ConsoleHelper.writeSystemMessage("Я положил в очередь положительный ответ проверки пакета");
                        processingQueue.put(userPackage);
                        ConsoleHelper.writeSystemMessage("Положил в processingQueue");
                    } else {
                        ConsoleHelper.writeSystemMessage("CRC32 не совпадает");
                        sendNegativeAnswer();
                    }
                } else {
                    ConsoleHelper.writeSystemMessage("Неправильная сигнатура пакета");
                    sendNegativeAnswer();
                }
            } catch (InterruptedException e) {
                ConsoleHelper.writeSystemMessage("Ошибка при извлечении данных из inputQueue в CheckPacket");
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
            ConsoleHelper.writeSystemMessage("Я положил в очередь отрицательный ответ проверки пакета");
        } else {
            ConsoleHelper.writeSystemMessage("Очередь полна, объект не добавился");
        }
    }
}
