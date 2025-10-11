package utlis;

import layer.ValidationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
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
