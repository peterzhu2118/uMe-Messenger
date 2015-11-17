package ServerV3_0;

import java.io.*;
import java.net.*;
import java.util.*;

import com.google.common.collect.Multimap;

public class Server extends Thread {
	private Socket connectionToClient;
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;
	private String clientUsername;

	private Thread onlineUserSendThread;
	private Thread clientListenThread;
	private Thread messageRetrieve;

	private boolean loggedIn;

	public Server(Socket x) {
		connectionToClient = x;
	}

	@Override
	public void run() {
		try {
			inputFromClient = new DataInputStream(
					connectionToClient.getInputStream());
			outputToClient = new DataOutputStream(
					connectionToClient.getOutputStream());

			startClientListenThread();
			startMessageRetrieveThread();
			startOnlineUserSendThread();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startOnlineUserSendThread() {
		onlineUserSendThread = new Thread() {
			public void run() {
				while (!onlineUserSendThread.isInterrupted()) {
					// System.out.println("Looking for online users for " +
					// clientUsername);
					HashSet<String> users = MessageExchange.getOnlineUsers();

					String message = "o ";

					for (String x : users) {
						if (x != clientUsername)
							message = message + x + " ";
					}

					message = message.trim();

					if (message.length() > 1) {
						// System.out.println("Sending online users to " +
						// clientUsername);

						try {
							// System.out.println("Online users " + message);
							sendToClient(message);
						} catch (IOException e) {
							closeConnectionAndThreads();
							break;
						}
					}

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						closeConnectionAndThreads();
						break;
					}
				}
			}
		};
		onlineUserSendThread.start();
	}

	public int getPortNumber() {
		return connectionToClient.getLocalPort();
	}

	// Starts the thread for client
	private void startClientListenThread() {
		// System.out.println("Cilent 1 thread begins");
		// Starts a new thread for client 1
		clientListenThread = new Thread() {
			@Override
			public void run() {
				// Keeps on taking input from client
				while (!clientListenThread.isInterrupted()) {
					try {
						// Listens to client 1
						messageSortCenter(listenFromClient());

					} catch (IOException e) {
						// Exception is thrown if the client disconnects
						// System.out.println("Connection lost");
						closeConnectionAndThreads();
						break;
					}
				}
			}
		};
		clientListenThread.start();

		// System.out.println("client 1 thread ends");
	}

	private void messageSortCenter(String message) throws IOException {
		System.out.println(message);
		if (message.charAt(0) == 'm' && loggedIn) { // 'm' command is for
			System.out.println("Message Came!");
			// sending messages
			message = message.substring(2);

			String username = "";

			for (int i = 0; i < message.length(); i++) {
				if (message.charAt(i) == ' ') {
					break;
				} else {
					username = username + message.charAt(i);
				}
			}

			sendMessageToOtherUser(username, message);
			// 'l' is for logging in as a user
			// NOT COMPLETE
		} else if (message.charAt(0) == 'l' && !loggedIn) {
			System.out.println("Log in successful");
			message = message.substring(2);

			String username = "";

			for (int i = 0; i < message.length(); i++) {
				if (message.charAt(i) == ' ') {
					break;
				} else {
					username = username + message.charAt(i);
				}
			}

			if (!MessageExchange.isUserOnline(username)) {
				clientUsername = username;

				MessageExchange.addOnlineUser(clientUsername);

				loggedIn = true;

				sendToClient("l t");
			}else{
				sendToClient("l f");
			}

		}
	}

	// Sends message to another client
	private void sendMessageToOtherUser(String destUsername, String message) {
		MessageExchange.addMessage(clientUsername, destUsername, message);
	}

	// Listens to client 1 then puts it into the message to client 2 waiting
	// list
	private String listenFromClient() throws IOException {
		return inputFromClient.readUTF();
	}

	// Starts a new thread that listens from MessageExchange
	private void startMessageRetrieveThread() {
		messageRetrieve = new Thread() {
			@Override
			public void run() {
				largeLoop: while (!messageRetrieve.isInterrupted()) {
					Multimap<String, String> messages = MessageExchange
							.getMessages(clientUsername);

					// If there are messages for this client
					if (!messages.isEmpty()) {
						for (String x : messages.keySet()) {
							Collection<String> listOfValues = messages.get(x);

							for (String z : listOfValues) {
								try {
									// System.out.println("SENDIG MESSAGE TO CLIENT");
									// System.out.println("X is " + x);
									// System.out.println("Z is " + z);

									for (int y = 0; y < z.length(); y++) {
										if (z.charAt(y) == ' ') {
											z = z.substring(y + 1);
											break;
										}
									}

									sendToClient("m " + x + " " + z);
								} catch (IOException e) {
									closeConnectionAndThreads();
									break largeLoop;
								}
							}
						}
					}

					// Checks every 100 miliseconds
					try {
						sleep(100);
					} catch (InterruptedException e) {
						closeConnectionAndThreads();
						break;
					}
				}
			}
		};
		messageRetrieve.start();
	}

	// Sends a message to the client
	private void sendToClient(String message) throws IOException {
		outputToClient.writeUTF(message);
	}

	// Checks if the connection is active
	public boolean checkConnection() {
		return connectionToClient.isClosed();
	}

	public String getUsername() {
		return clientUsername;
	}

	// NEEDS TO BE COMPLETED
	// Stops all threads and closes the connection for garbage collection to
	// pick up
	public void closeConnectionAndThreads() {
		// System.out.println("Closed connection for " + clientUsername);

		onlineUserSendThread.interrupt();
		// System.out.println("Online Users send stopepd");

		clientListenThread.interrupt();
		// System.out.println("client listen stopped");

		messageRetrieve.interrupt();
		// System.out.println("message retrieve stopped");

		MessageExchange.removeUser(clientUsername);

		try {
			connectionToClient.close();

			// System.out.println(connectionToClient.isClosed());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// NOT COMPLETE
	// NEED TO BE CHANGED
	// Throws error
	public void throwError(String message) {
		System.out.println("Internal Error: " + message);
	}

}
