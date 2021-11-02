package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;


public class EchoServer {
    public static final int portNumber = 6013;

    public static void main(String[] args) throws IOException, InterruptedException {
        EchoServer server = new EchoServer();
		server.start();
}

private void start() throws IOException, InterruptedException {

    ServerSocket serverSocket = new ServerSocket(portNumber);

    while (true) {

        Socket socket = serverSocket.accept();
        clientHandler newClientConnection = new clientHandler(socket);

        Thread newClientThread = new Thread(newClientConnection);

        newClientThread.start();
    }
}

public class clientHandler implements Runnable {

    private Socket socket;

    public clientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream fromClientStream = socket.getInputStream();
            OutputStream toClientStream = socket.getOutputStream();

            int clientInput = fromClientStream.read();

            while (clientInput != -1) {
                toClientStream.write(clientInput);
                clientInput = fromClientStream.read();
            }

            toClientStream.flush();
            socket.shutdownOutput();

        } catch (IOException exception) {
            exception.printStackTrace();
        } 
    }
}
} 