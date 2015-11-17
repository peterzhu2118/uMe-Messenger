package ClientV1_1;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket clientSocket;
	private DataInputStream inputData;
	private DataOutputStream outputData;
	private String serverAddress;
	private int portNumber;
	private String myUsername;

	// Sets the client to the default port of 9999 and connection to the local
	// server
	// NOT RECOMMENDED FOR REAL USE
	public Client() throws UnknownHostException, IOException {
		this("User1", "localhost", 9999);
	}

	// Sets the server address and the port number of the server
	public Client(String username, String address, int port) throws UnknownHostException,
			IOException {
		serverAddress = address;
		portNumber = port;

		clientSocket = new Socket(serverAddress, portNumber);
		inputData = new DataInputStream(clientSocket.getInputStream());
		outputData = new DataOutputStream(clientSocket.getOutputStream());
		
		myUsername = username;
	}

	// Listens to the server for messages
	public void clientListenThread() {
		// System.out.println("CLIENT LISTEN");
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						// System.out.println("In try");
						clientListen();
					} catch (IOException e) {
						ClientGraphics.throwError("Disconnected from server");
						break;
					}
				}
			}
		};
		t.start();
	}

	// Listens to the server
	private void clientListen() throws IOException {
		String message = inputData.readUTF();
		// System.out.println("Client - " + message);
		
		String username = "";

		// Gets the current username
		for (int i = 0; i < message.length(); i++) {
			if (message.charAt(i) == ' ') {
				message = message.substring(i + 1);
				break;
			} else
				username = username + message.charAt(i);
		}
		
		if(username.equals(myUsername)){
			username = "Me";
		}
		
		ClientGraphics.recievedMessage(username, message);
	}

	// Sends message to the server
	public void clientSendMessage(String message) throws IOException {
		// System.out.println(message);
		outputData.writeUTF(message);
	}
}
