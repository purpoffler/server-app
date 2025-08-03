import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.CRC32;

public class CheckPacket implements Runnable {

    BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    BlockingQueue<String> processingQueue = new LinkedBlockingQueue<>();

    public CheckPacket(BlockingQueue<String> inputQueue, BlockingQueue<String> processingQueue) {
        this.inputQueue = inputQueue;
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {
        try {
            String packet = inputQueue.take();
            String signature = packet.substring(0, 8);
            String dataLength = packet.substring(8, 11);
            String dataType = packet.substring(11, 18);
            String data = packet.substring(18, packet.length() - 9);
            String CRC32 = packet.substring(packet.length() - 9, packet.length() - 1);

            try {
                if (checkType(dataType)) {
                    // как-то отправляем ответ клиенту, что пакет норм
                    Server.out.write("Пакет в норме)");
                } else {
                    // как-то отправляем ответ клиенту, что пакет не норм
                    Server.out.write("Пакет в поломалси ((()");
                }
            } catch (IOException e) {
                throw new RuntimeException("Что-то случилось во время отправки сообщения клиенту (проверка целостности пакета)", e);
            }


            System.out.println(String.format("%s|%s|%s|%s|%s|", signature, dataLength, dataType, data, CRC32));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Тип данных - 7 символов
    private boolean checkType(String dataType) {
        if (dataType.equalsIgnoreCase("CONSOLE") || dataType.equalsIgnoreCase("PLAIN") || dataType.equalsIgnoreCase("JSON")) {
            return true;
        }
        return false;
    }


}
