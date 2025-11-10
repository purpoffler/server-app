package layer;


import config.ServerConfig;
import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.logger.CustomLogger;
import layer.writters.JsonWriter;
import layer.writters.PlainWriter;
import utlis.ConsoleHelper;

import java.util.Arrays;

public class ProcessingLevel implements Runnable {
    private final ServerConfig serverConfig = ServerConfig.getInstance();
    private final JsonWriter jsonWriter = new JsonWriter();
    private final PlainWriter plainWriter = new PlainWriter();
    private static final CustomLogger log = new CustomLogger(ProcessingLevel.class.getSimpleName());

    @Override
    public void run() {
        try {
            while (true) {
                UserPackage userPackage = serverConfig.getProcessingQueue().take();
                log.debug("Получил dto из processingQueue");
                switch (userPackage.dataType()) {
                    case JSON:
                        jsonWriter.write(userPackage);
                        break;
                    case PLAIN:
                        plainWriter.write(userPackage);
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
