package layer;


import layer.dto.UserPackage;
import layer.enums.ExpectedDataType;
import utlis.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utlis.ConsoleHelper;
import utlis.ServerConfig;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class ProcessingLevel implements Runnable {
    private final BlockingQueue<UserPackage> processingQueue = ServerConfig.getProcessingQueue();
    private final JsonWriter jsonWriter = new JsonWriter();
    private static final Logger log = LoggerFactory.getLogger(ProcessingLevel.class);

    @Override
    public void run() {
        try {
            while (true) {
                UserPackage userPackage = processingQueue.take();
                log.debug("Получил dto из processingQueue");
                switch (userPackage.getDataType()) {
                    case JSON:
                        jsonWriter.writeJson(userPackage.getDate(), userPackage.getIp(), userPackage.getData());
                        break;
                    case PLAIN:
                        writePlainFile(userPackage.getDate(), userPackage.getIp(), userPackage.getData());
                        break;
                    case CONSOLE:
                        ConsoleHelper.writeMessage(userPackage.getDate() + " " + userPackage.getIp() + " " + userPackage.getData());
                        break;
                    default:
                        log.warn("Тип введенных данных" + userPackage.getDataType() + "а не " + Arrays.toString(ExpectedDataType.values()));
                }
            }
        } catch (InterruptedException e) {
            log.error("Ошибка при извлечении данных из processingQueue в классе ProcessingLevel");
        } catch (IOException e) {
            log.error("Ошибка при создании объекта потока ввода-вывода");
        }
    }

    public void writePlainFile(String date, String ip, String data) throws IOException {
        Path path = Paths.get(".", "UserText.txt").toAbsolutePath();
        File file = new File(path.toUri());
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(date + " " + ip + " " + data);
            writer.append('\n');
            writer.flush();
        }
    }
}
