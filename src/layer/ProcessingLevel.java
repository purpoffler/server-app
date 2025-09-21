package layer;

import dto.UserPackage;
import layer.enums.ExpectedDataType;
import utlis.ConsoleHelper;

import java.util.concurrent.BlockingQueue;

public class ProcessingLevel implements Runnable {
    private final BlockingQueue<UserPackage> processingQueue;

    public ProcessingLevel(BlockingQueue<UserPackage> processingQueue) {
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {
        try {
            UserPackage userPackage = processingQueue.take();
            ConsoleHelper.writeSystemMessage("Получил данные из processingQueue");
            switch (userPackage.getDataType()) {
                case ExpectedDataType.JSON:
                    writeJson(userPackage.getDate(), userPackage.getIp(), userPackage.getData());
                    break;
                case ExpectedDataType.PLAIN:
                    writePlainFile(userPackage.getDate(), userPackage.getIp(), userPackage.getData());
                    break;
                case ExpectedDataType.CONSOLE:
                    ConsoleHelper.writeMessage(userPackage.getDate() + " " + userPackage.getIp() + " " + userPackage.getData());
                    break;
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeSystemMessage("Ошибка при извлечении данных из processingQueue в классе ProcessingLevel");
        }
    }

    private void writeJson(String date, String ip, String data) {

    }

    public void writePlainFile(String date, String ip, String data) {

    }
}
