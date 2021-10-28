package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;


public class EchoServer {
    public static final int portNumber = 6013;

    public static void main(String[] args) {
        try {
            // Start listening on the specified port
            ServerSocket socket = new ServerSocket(portNumber);

            // Run forever, which is common for server style services
            while (true) {
                // Wait until someone connects, thereby requesting a date
                Socket client = socket.accept();
                System.out.println("Got a request!");

                // Helpful guide here:
                // https://stackoverflow.com/questions/1830698/what-is-inputstream-output-stream-why-and-when-do-we-use-them
                // Object that gets input from the client
                InputStream clientInput = client.getInputStream();
                // Object that sends output to the client
                OutputStream clientOutput = client.getOutputStream();

                Thread thread = new ClientHandler(client, clientInput, clientOutput);

                thread.start();
                

                
            }
            // *Very* minimal error handling.
        } catch (IOException ioe) {
            System.out.println("We caught an unexpected exception");
            System.err.println(ioe);
        }
    }   
}

 class ClientHandler extends Thread {
     final InputStream clientInput;
     final OutputStream clientOutput;
     final Socket socket;

    public ClientHandler(Socket socket, InputStream clientInput, OutputStream clientOutput) {
        this.socket = socket;
        this.clientInput = clientInput;
        this.clientOutput = clientOutput;
    }
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            // Buffer size of client input
            byte[] bufferSize = new byte[1024];
            int bytesRead;
            // Read data sent by client socket
            while ((bytesRead = clientInput.read(bufferSize)) != -1) {
                // Send data back to client console
                clientOutput.write(bufferSize, 0, bytesRead);
                // Clear buffer
                clientOutput.flush();
            }

            // TODO: Close the socket via client
            // Close the client socket since we're done.
            socket.close();
        }
        
    }