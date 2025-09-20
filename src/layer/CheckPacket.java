package layer;

import layer.enums.ExpectedDataType;
import utlis.ConsoleHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.zip.CRC32;

public class CheckPacket implements Runnable {
    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<String> processingQueue;
    private final BlockingQueue<String> resultValidateQueue;

    public CheckPacket(BlockingQueue<String> inputQueue, BlockingQueue<String> processingQueue, BlockingQueue<String> resultValidateQueue) {
        this.inputQueue = inputQueue;
        this.processingQueue = processingQueue;
        this.resultValidateQueue = resultValidateQueue;
    }

    @Override
    public void run() {
        try {
            while (true) { // цикл обработки пакетов
                String packet = inputQueue.take();
                //ConsoleHelper.writeMessage(packet);
                String[] packetBlocks = packet.split("\\|");
                ConsoleHelper.writeMessage(Arrays.toString(packetBlocks));

                String signature = packetBlocks[0];
                String dataLength = packetBlocks[1];
                String dataType = packetBlocks[2];
                String data = packetBlocks[3];
                String clientCRC32 = packetBlocks[4];

                ConsoleHelper.writeSystemMessage(String.valueOf(checkCRC32(data, clientCRC32)));
                ConsoleHelper.writeSystemMessage(clientCRC32);
                if (checkCRC32(data, clientCRC32)) {
                    ConsoleHelper.writeMessage("Проверка пакета " + String.valueOf(checkCRC32(data, clientCRC32)));
                    resultValidateQueue.offer("Пакет в норме)");
                    ConsoleHelper.writeSystemMessage(String.format("%s|%s|%s|%s|%s|%n", signature, dataLength, dataType, data, clientCRC32));
                } else {
                    ConsoleHelper.writeSystemMessage("Я говорю, что поломалси");
                    resultValidateQueue.offer(new String("Пакет поломался("));
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
}
