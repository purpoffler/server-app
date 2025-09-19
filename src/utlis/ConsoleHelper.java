package utlis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsoleHelper {
    private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

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
        System.out.println("\u001B[34m" + "Системное сообщение:\n" + message + "\u001B[0m" + "\n");
    }

    public static String readString() {
        while (true) {
            try {
                    String line = bf.readLine();
                    return line;
            } catch (IOException e) {
                System.out.println(" Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            }
        }
    }

    public static int readInt() {
        int digit;
        while (true) {
            try {
                digit = Integer.parseInt(readString());
                return digit;
            } catch (NumberFormatException e) {
                System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            }
        }
    }

}
