package layer;


import dto.UserPackage;
import layer.enums.ExpectedDataType;
import layer.jsonWriter.JsonWriter;
import utlis.ConsoleHelper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class ProcessingLevel implements Runnable {
    private final BlockingQueue<UserPackage> processingQueue;
    private JsonWriter jsonWriter = new JsonWriter();

    public ProcessingLevel(BlockingQueue<UserPackage> processingQueue) {
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                UserPackage userPackage = processingQueue.take();
                ConsoleHelper.writeSystemMessage("Получил данные из processingQueue");
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
                        ConsoleHelper.writeSystemMessage("Тип введенных данных" + userPackage.getDataType() + "а не " + Arrays.toString(ExpectedDataType.values()));
                }
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeSystemMessage("Ошибка при извлечении данных из processingQueue в классе ProcessingLevel");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
