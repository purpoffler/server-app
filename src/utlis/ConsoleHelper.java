package utlis;

import config.ServerConfig;

public class ConsoleHelper {
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static void writeSystemMessage(String message) {
        System.out.println(ServerConfig.getInstance().getColorBlue() + "Системное сообщение:\n" + message + ServerConfig.getInstance().getColorDefault() + "\n");
    }
}
