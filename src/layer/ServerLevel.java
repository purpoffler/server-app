package layer;

import utlis.ConsoleHelper;

import java.io.*;
import java.util.concurrent.BlockingQueue;

public class ServerLevel implements Runnable {
    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<String> resultValidateQueue;

    public ServerLevel(BlockingQueue<String> inputQueue, BlockingQueue<String> resultValidateQueue) {
        this.inputQueue = inputQueue;
        this.resultValidateQueue = resultValidateQueue;
    }

    public void run() {
        try (Connection connection = new Connection()) {
            ConsoleHelper.writeSystemMessage("Клиент подключился");
            while (true) {
                String word = connection.receive(); // ждём пока клиент что-нибудь нам напишет
                ConsoleHelper.writeSystemMessage(word);
                if (!word.isEmpty()) {
                    connection.send("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
                    inputQueue.put(word);
                    //System.out.println(inputQueue);
                } else {
                    connection.send("Лел, а данных-то я не получил!");
                }
                String resultValidatePacket = resultValidateQueue.take();
                connection.send(resultValidatePacket);
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeSystemMessage("Ошибка в считывании пакета");
        } catch (ClassNotFoundException e) {
            ConsoleHelper.writeSystemMessage("Ошибка во время отправки ответа");
        } catch (IOException e) {
            ConsoleHelper.writeSystemMessage("Клиент отключился");
        }
    }
}