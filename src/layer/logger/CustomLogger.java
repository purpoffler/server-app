package layer.logger;

import config.ServerConfig;
import utlis.ConsoleHelper;
import utlis.DateCalculator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomLogger {
    private final String className;

    public CustomLogger(String className) {
        this.className = className;
    }

    public void info(String message) {
        writeToFile(format(message, "[INFO]"));
    }

    public void debug(String message) {
        writeToFile(format(message, "[DEBUG]"));
    }

    public void error(String message, Throwable t) {
        writeToFile(format(message, "[ERROR]"));
        String header = "Exception in thread \"" + Thread.currentThread().getName() + "\" " + t.toString();
        writeToFile(header);
        for (StackTraceElement ste : t.getStackTrace()) {
            writeToFile(String.format("\t %s", ste));
        }
    }

    public String format(String message, String logLvl) {
        return String.format("[%s] %s %s - %s", DateCalculator.getDate(), logLvl, className, message);
    }

    private void writeToFile(String output) {
        Path logPath = Paths.get(ServerConfig.getInstance().getLogFilePath());
        if (!ensureLogDirectory(logPath)) {
            return;
        }
        try (FileWriter fw = new FileWriter(logPath.toFile(), true)) {
            fw.write(output);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            ConsoleHelper.writeSystemMessage("Ошибка записи в данных в .log. Логов не будет");
        }
    }

    private boolean ensureLogDirectory(Path logPath) {
        Path logDirectory = logPath.getParent();
        try {
            Files.createDirectories(logDirectory);
            return true;
        } catch (IOException e) {
            ConsoleHelper.writeSystemMessage("Ошибка cоздания директории log. Логов не будет");
            return false;
        }
    }
}
