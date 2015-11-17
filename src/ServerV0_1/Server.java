/*
 * COMPLETE
 * DO NOT CHANGE
 * NO KNOWN BUGS
 */

package ServerV0_1;

import java.io.*;
import java.net.*;

public class Server {
	private ServerSocket serverSocket1, serverSocket2;
	private Socket connectionToClient1, connectionToClient2;
	private DataInputStream inputFromClient1, inputFromClient2;
	private DataOutputStream outputToClient1, outputToClient2;
	private int port1, port2;
	private String messageToClient1, messageToClient2;

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

	private void listenFromClient1() throws IOException {
		// Listens to client 1 then puts it into the message to client 2 waiting
		// list
		messageToClient2 = inputFromClient1.readUTF();
		// System.out.println("RECEIVED FROM CLIENT 1!!!");
	}

	private void listenFromClient2() throws IOException {
		// Listens to client 2 then puts it into the message to client 2 waiting
		// list
		messageToClient1 = inputFromClient2.readUTF();
		// System.out.println("RECEIVED FROM CLIENT 2!!!");
	}

	private void sendToClient1() throws IOException {
		//Send message to client 1
		// System.out.println("SENT TO CLIENT 1!!!");
		outputToClient1.writeUTF(messageToClient1);
	}

	private void sendToClient2() throws IOException {
		//Sends message to client 2
		// System.out.println("SENT TO CLIENT 2!!!");
		outputToClient2.writeUTF(messageToClient2);
	}

}
