package utlis;

import layer.dto.UserPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConfig {
    private static ServerConfig instance;
    private final String signature = "zWj`Jjkg";
    private final BlockingQueue<UserPackage> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> resultValidateQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<UserPackage> processingQueue = new LinkedBlockingQueue<>();
    private final static String filePath = "src/config/system.properties";

    private Logger log;
    private String jsonFileName;
    private String plainFileName;
    private int port;
    private String colorBlue;
    private String colorRed;
    private String colorGreen;
    private String colorDefault;

    public static void init() {
        if (instance == null) {
            instance = new ServerConfig();
            System.setProperty("log4j.configurationFile", "src/config/log4j2.xml");
            instance.log = LoggerFactory.getLogger(ServerConfig.class);
        }
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(filePath));
            System.setProperty("log4j.configurationFile", "config/log4j2.xml");
            instance.port = Integer.parseInt(properties.getProperty("port"));
            instance.colorBlue = properties.getProperty("colorBlue");
            instance.colorRed = properties.getProperty("colorRed");
            instance.colorGreen = properties.getProperty("colorGreen");
            instance.colorDefault = properties.getProperty("colorDefault");
            instance.jsonFileName = properties.getProperty("jsonFileName");
            instance.plainFileName = properties.getProperty("plainFileName");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        if (instance == null) {
            instance.log.warn("Синглтон еще не инициализирован, треш. А должен как бы [{}]", instance.getClass());
            ConsoleHelper.writeSystemMessage("Произошла ошибка непредвиденная ошибка, перезапустите приложение");
        }
        return instance;
    }

    public String getSignature() {
        return signature;
    }

    public BlockingQueue<UserPackage> getInputQueue() {
        return inputQueue;
    }

    public BlockingQueue<String> getResultValidateQueue() {
        return resultValidateQueue;
    }

    public BlockingQueue<UserPackage> getProcessingQueue() {
        return processingQueue;
    }

    public int getPort() {
        return port;
    }

    public String getColorBlue() {
        return colorBlue;
    }

    public String getColorRed() {
        return colorRed;
    }

    public String getColorDefault() {
        return colorDefault;
    }

    public String getColorGreen() {
        return colorGreen;
    }

    public String getPlainFileName() {
        return plainFileName;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }
}
