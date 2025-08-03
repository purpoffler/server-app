import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Server implements Runnable {
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    static BufferedWriter out; // поток записи в сокет
    private final BlockingQueue<String> inputQueue;

    public Server(BlockingQueue<String> inputQueue) {
        this.inputQueue = inputQueue;
    }

    public void run() {
        try {
            try {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004
                System.out.println("Сервер запущен!");

                clientSocket = server.accept(); // accept() будет ждать пока кто-нибудь не захочет подключиться
                try {
                    System.out.println("Клиент подключился");
                    // Читаем сообщения от клиента
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    // Отправляем сообщения клиенту
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    while (true) {
                        String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
                        System.out.println(word);

                        if (!word.isEmpty()) {
                            out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
                            inputQueue.put(word);
                            System.out.println(inputQueue);
                        } else {
                            out.write("Лел, а данных-то я не получил!");
                        }
                        out.flush();
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                finally { // в любом случае сокет будет закрыт
//                    clientSocket.close();
//                    in.close();
//                    out.close();
//                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Потеря соединения", e);
        }
    }
}