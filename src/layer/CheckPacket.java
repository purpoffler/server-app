package layer;

import layer.enums.ExpectedDataType;
import utlis.ConsoleHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class CheckPacket implements Runnable {
    BlockingQueue<String> inputQueue;
    BlockingQueue<String> processingQueue;

    public CheckPacket(BlockingQueue<String> inputQueue, BlockingQueue<String> processingQueue) {
        this.inputQueue = inputQueue;
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {
        try {
            while (true) { // цикл обработки пакетов
                String packet = inputQueue.take();
                String signature = packet.substring(0, 8);
                String dataLength = packet.substring(8, 11);
                String dataType = packet.substring(11, 18);
                String data = packet.substring(18, packet.length() - 9);
                String clientCRC32 = packet.substring(packet.length() - 9, packet.length() - 1);
                if (checkCRC32(data, clientCRC32)) {
                    sendMessage("Пакет в норме)");
                    ConsoleHelper.writeSystemMessage(String.format("%s|%s|%s|%s|%s|%n", signature, dataLength, dataType, data, clientCRC32));
                } else {
                    sendMessage("Пакет поломался :(");
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    private void sendMessage(String message) {
        try {
            ServerLevel.out.write(message + "\n");
            ServerLevel.out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке ответа клиенту", e);
        }
    }

}
