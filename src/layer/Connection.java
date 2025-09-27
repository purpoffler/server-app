package layer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket clientSocket; //сокет для общения
    private final ServerSocket serverSocket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public Connection() throws IOException {
        serverSocket = new ServerSocket(4004);
        clientSocket = serverSocket.accept();
        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void send(String string) throws IOException {
        out.write(string);
        out.flush();
    }

    public String receive() throws IOException, ClassNotFoundException {
        return in.readLine();
    }

    public String getIp() {
        return clientSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
