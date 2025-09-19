package utlis;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFile {
    private static boolean initialized = false;

    private LogToFile() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        try {
            FileHandler fh = new FileHandler("Client.log", true);
            fh.setFormatter(new SimpleFormatter());

            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.ALL);
            rootLogger.addHandler(fh);

            initialized = true;
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Что-то не так с логгером");
        }
    }

    public static Logger getLogger(Object o) {
        init();
        return Logger.getLogger(o.getClass().getName());
    }
}