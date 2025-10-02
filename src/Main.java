import layer.ValidationLevel;
import layer.ProcessingLevel;
import layer.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ServerConfig;


public class Main {
    public static void main(String[] args) {
        ServerConfig serverConfig = ServerConfig.getInstance();

        Logger log = LoggerFactory.getLogger(Main.class);
        log.info("Программа запустилась");

        Thread inputThread = new Thread(new ServerLevel());
        Thread checkThread = new Thread(new ValidationLevel());
        Thread processThread = new Thread(new ProcessingLevel());

        inputThread.start();
        checkThread.start();
        processThread.start();
    }
}