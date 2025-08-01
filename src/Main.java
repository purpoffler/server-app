import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<String> inputQueue = new LinkedBlockingQueue();
        new LinkedBlockingQueue();
        Thread inputThread = new Thread(new InputPacket(inputQueue));
        inputThread.start();
    }
}