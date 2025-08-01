import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {

        // Очереди для связи между потоками
        BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> processingQueue = new LinkedBlockingQueue<>();

        // Создаем потоки и передаем в них очереди, с которыми они должны работать
        Thread inputThread = new Thread(new InputPacket(inputQueue));

//     Thread checkThread = new Thread(new Packaging(inputQueue, processingQueue));
//     Thread processThread = new Thread(new Client(processingQueue));

        inputThread.start();

//    checkThread.start();
//    processThread.start();
    }
}