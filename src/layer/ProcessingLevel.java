package layer;

import dto.UserPackage;

import java.util.concurrent.BlockingQueue;

public class ProcessingLevel implements Runnable{
    private final BlockingQueue<UserPackage> processingQueue;

    public ProcessingLevel(BlockingQueue<UserPackage> processingQueue) {
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {

    }
}
