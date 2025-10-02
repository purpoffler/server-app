package utlis;

import layer.dto.UserPackage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class ServerConfig {
    private static ServerConfig instance;
    private static final String signature = "zWj`Jjkg";
    private static final BlockingQueue<UserPackage> inputQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> resultValidateQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<UserPackage> processingQueue = new LinkedBlockingQueue<>();
    private static Properties properties = new Properties();
    private static String filePath = "src/config/system.properties";
    private static String host;
    private static int port;
    private static String colorBlue;
    private static String colorRed;
    private static String colorGreen;
    private static String colorDefault;

    static {
        try {
            properties.load(new FileReader(filePath));
            System.setProperty("log4j.configurationFile", "config/log4j2.xml");
            host = properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            colorBlue = properties.getProperty("colorBlue");
            colorRed = properties.getProperty("colorRed");
            colorGreen = properties.getProperty("colorGreen");
            colorDefault = properties.getProperty("colorDefault");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        if (instance == null) {
            instance = new ServerConfig();
        }
        return instance;
    }

    public static String getSignature(){
        return signature;
    }

    public static BlockingQueue<UserPackage> getInputQueue() {
        return inputQueue;
    }

    public static BlockingQueue<String> getResultValidateQueue() {
        return resultValidateQueue;
    }

    public static BlockingQueue<UserPackage> getProcessingQueue() {
        return processingQueue;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getColorBlue() {
        return colorBlue;
    }

    public static String getColorRed() {
        return colorRed;
    }

    public static String getColorDefault() {
        return colorDefault;
    }

    public static String getColorGreen() {
        return colorGreen;
    }
}
