import dto.UserPackage;
import layer.ValidationLevel;
import layer.ProcessingLevel;
import layer.ServerLevel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        // Очереди для связи между потоками
        BlockingQueue<UserPackage> inputQueue = new LinkedBlockingQueue<>();
        BlockingQueue<String> resultValidateQueue = new LinkedBlockingQueue<>();
        BlockingQueue<UserPackage> processingQueue = new LinkedBlockingQueue<>();


        // Создаем потоки и передаем в них очереди, с которыми они должны работать
        Thread inputThread = new Thread(new ServerLevel(inputQueue, resultValidateQueue));
        Thread checkThread = new Thread(new ValidationLevel(inputQueue, resultValidateQueue, processingQueue));
        Thread processThread = new Thread(new ProcessingLevel(processingQueue));

        inputThread.start();
        checkThread.start();
        processThread.start();
    }
}