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
        // Accept a connection
        Socket socket = serverSocket.accept();

        // Create a new clientHandler and thread for the accepted connection
        clientHandler newClientConnection = new clientHandler(socket);
        Thread newClientThread = new Thread(newClientConnection);
        newClientThread.start();
    }
}

public class clientHandler implements Runnable {

    // The socket where to listen/talk pre thread
    private Socket socket;

    public clientHandler(Socket socket) {
        // Constructor for the clientHandler socket
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream getClientInput = socket.getInputStream();
            OutputStream SendClientOutput = socket.getOutputStream();

            // Read the input from the client
            int clientInput = getClientInput.read();

            while (clientInput != -1) {
                // Send the input back to the client
                SendClientOutput.write(clientInput);
                clientInput = getClientInput.read();
            }

            // Close the socket
            SendClientOutput.flush();
            socket.shutdownOutput();

        } catch (IOException exception) {
            exception.printStackTrace();
        } 
    }
}
} 