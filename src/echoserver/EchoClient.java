package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;
	
	public static void main(String[] args) throws IOException {
		EchoClient client = new EchoClient();
		// Start the client object and its threads
		client.start();
	}

// Start the client and its threads
private void start() throws IOException {
	// Initialize socket connection
	Socket socket = new Socket("localhost", PORT_NUMBER);

	// Get the input and output streams from the socket
	InputStream socketInputStream = socket.getInputStream();
	OutputStream socketOutputStream = socket.getOutputStream();

	// Instantiate input and output streams of the client
	UserInputThread userInput = new UserInputThread(socket, socketOutputStream);
	ServerOutputThread serverOutput = new ServerOutputThread(socket, socketInputStream);

	// Start two threads for the client userInput and serverOutput
	Thread userInputThread = new Thread(userInput);
	Thread serverOutputThread = new Thread(serverOutput);
	
	userInputThread.start();
	serverOutputThread.start();

	// wait for the threads to finish and join them
	try {
		userInputThread.join();
		serverOutputThread.join();
	} catch (InterruptedException exception) {
		System.out.println("InterruptedException: " + exception.getMessage());
		System.exit(1);
	}

	// close the socket
	socket.close();
	
}

public class UserInputThread implements Runnable {

	Socket socket;
	OutputStream outputToServer;
	
	public UserInputThread(Socket socket, OutputStream socketOutputStream){
		this.socket = socket;
		this.outputToServer = socketOutputStream;
	}

	// read the user input and send it to the server
	public void run	() {
		try{
			
			int keyboardInput = System.in.read();
			while (keyboardInput != -1){
				outputToServer.write(keyboardInput);
				keyboardInput = System.in.read();
			}

			// Flush the buffer close the socket
			outputToServer.flush();
			socket.shutdownOutput();

		} catch(IOException e) {
			System.out.println("Error reading user input");
			System.out.println(e);
			System.exit(1);
		}
	}
}
public class ServerOutputThread implements Runnable {
	Socket socket;
	InputStream serverInputStream;
	
	public ServerOutputThread(Socket socket, InputStream socketInputStream){
		// Construct the socket and input stream
		this.socket = socket;
		this.serverInputStream = socketInputStream;
	}

	// read the server output and print it to the console
	public void run	() {
		try	{
			int serverOutput = serverInputStream.read();
			while (serverOutput != -1){
				System.out.write(serverOutput);
				serverOutput = serverInputStream.read();
			}

			// close the socket
			System.out.flush();
			socket.shutdownInput();

		} catch(IOException e) {
			System.out.println("Error reading user input");
			System.out.println(e);
			System.exit(1);
		}
	}
}
}