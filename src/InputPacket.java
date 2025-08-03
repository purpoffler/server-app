import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class InputPacket implements Runnable {

    private final BlockingQueue<String> inputQueue;

    public InputPacket(BlockingQueue<String> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        Thread server = new Thread(new Server(inputQueue));
        server.start();
    }



}