//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void run() {
        try {
            try {
                server = new ServerSocket(4004);
                System.out.println("Сервер запущен!");
                clientSocket = server.accept();

                try {
                    System.out.println("Клиент подключился");
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    while(true) {
                        String word = in.readLine();
                        System.out.println(word);
                        if (!word.isEmpty()) {
                            out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
                        } else {
                            if (word.equalsIgnoreCase("стоп")) {
                                return;
                            }

                            out.write("Лел, а данных-то я не получил!");
                        }

                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Потеря соединения", e);
        }
    }
}
