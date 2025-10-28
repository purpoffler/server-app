package config;

import layer.dto.UserPackage;
import utlis.ConsoleHelper;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerConfig {
    private volatile static ServerConfig instance;
    private final BlockingQueue<UserPackage> inputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> resultValidateQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<UserPackage> processingQueue = new LinkedBlockingQueue<>();
    private final static String SIGNATURE = "zWj`Jjkg";
    private final static String CONFIG_FILE_PATH = "src/config/system.properties";

    private String jsonFileName;
    private String plainFileName;
    private int port;
    private String colorBlue;
    private String colorGreen;
    private String colorDefault;
    private String logFilePath;

    private ServerConfig() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(CONFIG_FILE_PATH));
            this.port = Integer.parseInt(properties.getProperty("port", "4004"));
            this.colorBlue = properties.getProperty("colorBlue", "");
            this.colorGreen = properties.getProperty("colorGreen", "");
            this.colorDefault = properties.getProperty("colorDefault", "");
            this.jsonFileName = properties.getProperty("jsonFileName", "UserJson.json");
            this.plainFileName = properties.getProperty("plainFileName", "UserText.txt");
            this.logFilePath = properties.getProperty("logFilePath", "logFilePath=logs/custom.log");
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Файл c property не найден");
        }
    }

    public static ServerConfig getInstance() {
        if (instance == null) {
            synchronized (ServerConfig.class) {
                if (instance == null) {
                    instance = new ServerConfig();
                }
            }
        }
        return instance;
    }

    public String getSignature() {
        return SIGNATURE;
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

    public String getLogFilePath() {
        return logFilePath;
    }
}
