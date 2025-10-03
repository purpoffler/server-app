package layer;


import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.writters.JsonWritter;
import layer.writters.PlainWritter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ConsoleHelper;
import utlis.ServerConfig;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class ProcessingLevel implements Runnable {
    private final BlockingQueue<UserPackage> processingQueue = ServerConfig.getProcessingQueue();
    private final JsonWritter jsonWriter = new JsonWritter();
    private final PlainWritter plainWritter = new PlainWritter();
    private static final Logger log = LoggerFactory.getLogger(ProcessingLevel.class);

    @Override
    public void run() {
        try {
            while (true) {
                UserPackage userPackage = processingQueue.take();
                log.debug("Получил dto из processingQueue");
                switch (userPackage.getDataType()) {
                    case JSON:
                        jsonWriter.write(userPackage);
                        break;
                    case PLAIN:
                        plainWritter.write(userPackage);
                        break;
                    case CONSOLE:
                        ConsoleHelper.writeMessage(userPackage.getDate() + " " + userPackage.getIp() + " " + userPackage.getData());
                        break;
                    default:
                        log.warn("Тип введенных данных" + userPackage.getDataType() + "а не " + Arrays.toString(ExpectedDataType.values()));
                }
            }
        } catch (InterruptedException e) {
            log.error("Ошибка при извлечении данных из processingQueue [{}]", this.getClass(), e);
        }
    }
}
