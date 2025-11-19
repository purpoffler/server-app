import layer.ValidationLevel;
import layer.ProcessingLevel;
import layer.ServerLevel;
import config.ServerConfig;
import layer.logger.CustomLogger;


public class Main {
    public static void main(String[] args) {
        ServerConfig.getInstance();

        CustomLogger log = new CustomLogger(Main.class.getSimpleName());

        log.info("Программа запустилась");

        Thread inputThread = new Thread(new ServerLevel());
        Thread checkThread = new Thread(new ValidationLevel());
        Thread processThread = new Thread(new ProcessingLevel());

        inputThread.start();
        checkThread.start();
        processThread.start();
    }
}