package layer;


import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.logger.CustomLogger;
import layer.writters.JsonWritter;
import layer.writters.PlainWritter;
import utlis.ConsoleHelper;
import config.ServerConfig;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class ProcessingLevel implements Runnable {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private final BlockingQueue<UserPackage> processingQueue = serverConfig.getProcessingQueue();
    private final JsonWritter jsonWriter = new JsonWritter();
    private final PlainWritter plainWritter = new PlainWritter();
    private static final CustomLogger log = new CustomLogger(ProcessingLevel.class.getSimpleName());

    @Override
    public void run() {
        try {
            while (true) {
                UserPackage userPackage = processingQueue.take();
                log.debug("Получил dto из processingQueue");
                switch (userPackage.dataType()) {
                    case JSON:
                        jsonWriter.write(userPackage);
                        break;
                    case PLAIN:
                        plainWritter.write(userPackage);
                        break;
                    case CONSOLE:
                        ConsoleHelper.writeMessage(userPackage.date() + " " + userPackage.ip() + " " + userPackage.data());
                        break;
                    default:
                        log.debug("Тип введенных данных" + userPackage.dataType() + "а не " + Arrays.toString(ExpectedDataType.values()));
                }
            }
        } catch (InterruptedException e) {
            log.error("Ошибка при извлечении данных из processingQueue", e);
        }
    }
}
