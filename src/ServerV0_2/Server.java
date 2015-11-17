package ServerV0_2;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	private ServerSocket serverSocket1, serverSocket2;
	private Socket connectionToClient1, connectionToClient2;
	private DataInputStream inputFromClient1, inputFromClient2;
	private DataOutputStream outputToClient1, outputToClient2;
	private int port1, port2;
	private String messageToClient1, messageToClient2;
	private ServerFileIO files;
	private String client1Username, client2Username;

	// No arg constructor that sets the default ports
	public Server() throws IOException {
		this(9999, 9998);
	}

	// Constructor that takes in the port number
	public Server(int port1, int port2) throws IOException {
		// System.out.println("in contructor");
		serverSocket1 = new ServerSocket(port1);
		serverSocket2 = new ServerSocket(port2);
		connectionToClient1 = serverSocket1.accept();
		connectionToClient2 = serverSocket2.accept();
		inputFromClient1 = new DataInputStream(
				connectionToClient1.getInputStream());
		inputFromClient2 = new DataInputStream(
				connectionToClient2.getInputStream());
		outputToClient1 = new DataOutputStream(
				connectionToClient1.getOutputStream());
		outputToClient2 = new DataOutputStream(
				connectionToClient2.getOutputStream());

		this.port1 = port1;
		this.port2 = port2;

		client1Username = "User1";
		client2Username = "User2";

		files = new ServerFileIO(client1Username, client2Username);

		messageInitializer();

		// System.out.println("done contrructor");
	}

	// Starts the two threads for the clients
	public void startServer() {
		// System.out.println("Start server started");
		startClient1Thread();
		startClient2Thread();
		// System.out.println("start server ended");
	}

	// Starts the thread for client 1
	private void startClient1Thread() {
		// System.out.println("Cilent 1 thread begins");
		// Starts a new thread for client 1
		Thread t = new Thread() {
			@Override
			public void run() {
				// Keeps on taking input from client
				while (true) {
					try {
						// Listens to client 1
						listenFromClient1();
						// System.out.println("IN TRY FOR CLIENT 1");
						// Sends that connection to client 2
						sendToClient2();
					} catch (IOException e) {
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

	// Starts the thread for client 2
	private void startClient2Thread() {
		// System.out.println("client 2 thread begins");
		// Starts a new thread for client 2
		Thread t = new Thread() {
			@Override
			public void run() {
				// Keeps on taking input from client 2
				while (true) {
					try {
						// Listens to client 2
						listenFromClient2();
						// System.out.println("IN TRY FOR CLIENT 2");
						// Sends the message to client 1
						sendToClient1();
					} catch (IOException e) {
						// Exception is thrown if the connection is lost to the
						// client
						// System.out.println("Connection lost");
						break;
					}
				}
			}
		};
		t.start();

		// System.out.println("Client 2 thread ends");
	}

	// Listens to client 1 then puts it into the message to client 2 waiting
	// list
	private void listenFromClient1() throws IOException {
		messageToClient2 = inputFromClient1.readUTF();
		// System.out.println("RECEIVED FROM CLIENT 1!!!");
	}

	// Listens to client 2 then puts it into the message to client 2 waiting
	// list
	private void listenFromClient2() throws IOException {
		messageToClient1 = inputFromClient2.readUTF();
		// System.out.println("RECEIVED FROM CLIENT 2!!!");
	}

	// Send message to client 1
	private void sendToClient1() throws IOException {
		// System.out.println("SENT TO CLIENT 1!!!");
		// Adds the message to the text file
		files.addMessage("User1", messageToClient1);
		outputToClient1.writeUTF(messageToClient1);
	}

	// Sends message to client 2
	private void sendToClient2() throws IOException {
		// System.out.println("SENT TO CLIENT 2!!!");
		// Adds the message to the text file
		files.addMessage("User2", messageToClient2);
		outputToClient2.writeUTF(messageToClient2);
	}

	// Sends message to client 1 takes in parameter
	// Doesnt log into ServerFileIO
	private void sendToClient1NoLog(String message) throws IOException {
		outputToClient1.writeUTF(message);
	}

	// Sends message to cleint 2 takes in parameter
	// Doesnt log into ServerFileIO
	private void sendToClient2NoLog(String message) throws IOException {
		outputToClient2.writeUTF(message);
	}

	// Initializes the messages from past conversations
	private void messageInitializer() throws IOException {
		ArrayList<String> listOfMessages = files.getAllMessages();

		// Goes through all the messages
		for (String x : listOfMessages) {
			// Stores the username for current message
			String username = "";

			// Gets the current username
			for (int i = 0; i < x.length(); i++) {
				if (x.charAt(i) == ' ') {
					x = x.substring(i + 1);
					break;
				} else
					username = username + x.charAt(i);
			}

			// System.out.println("Username is: " + username);
			// System.out.println("Message is: " + x);

			// If the username of the client is the same of the client 1
			// username
			if (username.equals(client1Username)
					|| username.equals(client2Username)) {
				sendToClient1NoLog(username + " " + x);
				sendToClient2NoLog(username + " " + x);
			}// If the username doesnt exist
			else
				throwError("In messageInitilizer, cannot find user for current message");

		}
	}

	// NOT COMPLETE
	// NEED TO BE CHANGED
	// Throws error
	public void throwError(String message) {
		System.out.println("Internal Error: " + message);
	}

}
