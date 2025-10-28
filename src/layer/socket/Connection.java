package layer.socket;

import config.ServerConfig;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket clientSocket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public Connection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(ServerConfig.getInstance().getPort());
        this.clientSocket = serverSocket.accept();
        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void send(String string) throws IOException {
        out.write(string);
        out.flush();
    }

    public String receive() throws IOException{
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
