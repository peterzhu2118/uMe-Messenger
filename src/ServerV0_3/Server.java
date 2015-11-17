package ServerV0_3;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread{
	private ServerSocket serverSocket;
	private Socket connectionToClient;
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;
	private int port;
	private String clientUsername;
	
	// Constructor that takes in the port number
	public Server(int port, String username) throws IOException {
		this.port = port;

		clientUsername = username;
	}
	
	@Override
	public void run(){
		try {
			serverSocket = new ServerSocket(port);
			connectionToClient = serverSocket.accept();
			inputFromClient = new DataInputStream(
					connectionToClient.getInputStream());
			outputToClient = new DataOutputStream(
					connectionToClient.getOutputStream());
			
			startClientListenThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// Starts the thread for client 1
	private void startClientListenThread() {
		// System.out.println("Cilent 1 thread begins");
		// Starts a new thread for client 1
		Thread t = new Thread() {
			@Override
			public void run() {
				// Keeps on taking input from client
				while (true) {
					try {
						// Listens to client 1
						System.out.println("From " + clientUsername + ": "+ listenFromClient());
						
					} catch (IOException e) {
						try {
							new Server(port, clientUsername);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// Exception is thrown if the client disconnects
						// System.out.println("Connection lost");
						break;
					}
				}
			}
		};
		t.start();

		// System.out.println("client 1 thread ends");
	}

	// Listens to client 1 then puts it into the message to client 2 waiting
	// list
	private String listenFromClient() throws IOException {
		return inputFromClient.readUTF();
	}

	private void sendToClient(String message) throws IOException{
		outputToClient.writeUTF(message);
	}

	// NOT COMPLETE
	// NEED TO BE CHANGED
	// Throws error
	public void throwError(String message) {
		System.out.println("Internal Error: " + message);
	}

}
