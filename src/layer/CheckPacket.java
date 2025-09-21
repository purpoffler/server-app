package layer;

import dto.UserPackage;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class CheckPacket implements Runnable {
    private final BlockingQueue<UserPackage> inputQueue;
    private final BlockingQueue<UserPackage> processingQueue;
    private final BlockingQueue<String> resultValidateQueue;

    public CheckPacket(BlockingQueue<UserPackage> inputQueue, BlockingQueue<String> resultValidateQueue, BlockingQueue<UserPackage> processingQueue) {
        this.inputQueue = inputQueue;
        this.processingQueue = processingQueue;
        this.resultValidateQueue = resultValidateQueue;
    }

    @Override
    public void run() {
        try {
            while (true) { // цикл обработки пакетов
                UserPackage userPackage = inputQueue.take();
                String data = userPackage.getData();
                String clientCRC32 = userPackage.getClientCRC32();
                //ConsoleHelper.writeSystemMessage(String.valueOf(checkCRC32(data, clientCRC32)));
                if (checkCRC32(data, clientCRC32)) {
                    ConsoleHelper.writeMessage("Проверка пакета " + String.valueOf(checkCRC32(data, clientCRC32)));
                    if (resultValidateQueue.offer("Пакет в норме")) {
                        ConsoleHelper.writeSystemMessage("Я положил в очередь положительный ответ проверки пакета");
                        processingQueue.put(userPackage);
                        ConsoleHelper.writeSystemMessage("Положил в processingQueue");
                    } else {
                        ConsoleHelper.writeSystemMessage("Очередь полна, объект не добавился");
                    }
                } else {
                    //ConsoleHelper.writeSystemMessage("Я говорю, что пакет поломался");
                    if (resultValidateQueue.offer("Пакет поломался")) {
                        ConsoleHelper.writeSystemMessage("Я положил в очередь отрицательный ответ проверки пакета");
                    } else {
                        ConsoleHelper.writeSystemMessage("Очередь полна, объект не добавился");
                    }
                }
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeSystemMessage("Ошибка при извлечении данных из inputQueue в CheckPacket");
        }
    }

    // Проверка контрольной суммы
    public boolean checkCRC32(String data, String clientCRC32) {
        CRC32 crc32 = new CRC32();
        crc32.update(data.getBytes());
        long value = crc32.getValue();
        String serverCRC32 = String.valueOf(value);
        return serverCRC32.equals(clientCRC32);
    }
}
