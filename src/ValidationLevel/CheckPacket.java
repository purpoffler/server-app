package ValidationLevel;

import IngestionLevel.Server;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

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
                String CRC32 = packet.substring(packet.length() - 9, packet.length() - 1);

                if (checkType(dataType)) {
                    sendMessage("Пакет в норме)");
                } else {
                    sendMessage("Пакет поломался :(");
                }

                System.out.printf("%s|%s|%s|%s|%s|%n", signature, dataLength, dataType, data, CRC32);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Тип данных - 7 символов
    private boolean checkType(String dataType) {
        if (Arrays.stream(ExpectedDataType.values()).anyMatch(x -> x.name().equalsIgnoreCase(dataType))) {
            return true;
        }
        return false;
    }

    private void sendMessage(String message) {
        try {
            Server.out.write(message + "\n");
            Server.out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке ответа клиенту", e);
        }
    }

}
