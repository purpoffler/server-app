package utlis;

import config.ServerConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConsoleHelper {
    private static final ServerConfig serverConfig = ServerConfig.getInstance();
    private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static void writeSystemMessage(String message) {
        System.out.println(serverConfig.getColorBlue() + "Системное сообщение:\n" + message + serverConfig.getColorDefault() + "\n");
    }
}
