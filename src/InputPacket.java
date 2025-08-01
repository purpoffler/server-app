//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.concurrent.BlockingQueue;

public class InputPacket implements Runnable {
    private final BlockingQueue<String> inputQueue;

    public InputPacket(BlockingQueue<String> inputQueue) {
        this.inputQueue = inputQueue;
    }

    public void run() {
        Thread server = new Thread(new Server());
        server.start();
    }
}
