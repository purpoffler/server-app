package utlis;

import layer.ValidationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsoleHelper {
    private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    private static final Logger log = LoggerFactory.getLogger(ConsoleHelper.class);

    public static void getInstruction() {
        System.out.println();
        System.out.println("Привет!");
        System.out.println("С помощью меня ты можешь отправить данные различных типов:");
        System.out.println("""
                    CONSOLE – сервер выведет данные в консоль;
                    PLAIN – сервер сохранит данные в обычный файл свободного формата;
                    JSON – сервер сохранить данные в файл с типом .json и json-форматированием;
                """);
        System.out.println("Введи формат, в котором ты хочешь отправить данные, например, CONSOLE");
    }

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static void writeSystemMessage(String message) {
        System.out.println(ServerConfig.getColorBlue() + "Системное сообщение:\n" + message + ServerConfig.getColorDefault() + "\n");
    }

    public static String readString() {
        while (true) {
            try {
                return bf.readLine();
            } catch (IOException e) {
                System.out.println(" Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
                log.error(" Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            }
        }
    }
}
