/*
 * KNOWN BUGS:
 * -Allows duplicate usernames
 */

package ClientV2_0;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	private Socket clientSocket;
	private DataInputStream inputData;
	private DataOutputStream outputData;
	private String serverAddress;
	private int portNumber;
	private String username;
	private ClientGraphics graphics;
	private boolean loggedIn;

	private Thread serverListenThread;
	private Thread clientListenThread;

	public Client() {
		graphics = new ClientGraphics();

		String address = "";
		int portNumber = 19000;
		String username = " ";

		// If the user wants advanced controls
		if (graphics
				.yesNoPopup("Select \"Yes\" for advanced setup. \n Select \"No\" for basic setup")) {

			address = graphics.inputPopup("Server Address:");

			while (address.length() < 1)
				address = graphics
						.inputPopup("Please input a valid server address");

			graphics.setServerAddressLabel(serverAddress);

			// Tries to get a valid port number
			try {
				portNumber = Integer
						.parseInt(graphics
								.inputPopup("Please input port number for the server:"));
			} catch (NumberFormatException e) {
				boolean stop = false;
				while (!stop) {
					try {
						portNumber = Integer.parseInt(graphics
								.inputPopup("Please a valid port number:"));

						stop = true;
					} catch (NumberFormatException x) {
						stop = false;
					}
				}
			}

			graphics.setPortNumberLabel(portNumber);
		} else {
			address = graphics.inputPopup("Server Address:");

			while (address.length() < 1)
				address = graphics
						.inputPopup("Please input a valid server address");

			graphics.setServerAddressLabel(address);

			graphics.setPortNumberLabel(portNumber);
		}

		username = graphics.inputPopup("Please input your desired username: ");

		while (username.length() == 0
				|| username.contains(new StringBuilder(" "))) {
			username = graphics.inputPopup("Please input a valid username: ");
		}

		graphics.setUsernameLabel(username);

		try {
			clientSocket = new Socket(serverAddress, portNumber);
			inputData = new DataInputStream(clientSocket.getInputStream());
			outputData = new DataOutputStream(clientSocket.getOutputStream());

			outputData.writeUTF("l " + username);

			while (true) {
				String tempMessage = inputData.readUTF();
				if (tempMessage.charAt(0) == 'l') {
					if (tempMessage.charAt(2) == 't') {
						break;
					} else if (tempMessage.charAt(2) == 'f') {
						username = graphics
								.inputPopup("User exists, please choose another username");

						while (username.length() == 0
								|| username.contains(new StringBuilder(" "))) {
							username = graphics
									.inputPopup("Please input a valid username: ");
						}
						
						outputData.writeUTF("l " + username);
					}
				}
			}

			this.serverAddress = address;
			this.portNumber = portNumber;
			this.username = username;

			serverListenThread();
			clientListenThread();
		} catch (UnknownHostException e) {
			graphics.throwError("Invalid server address");
			graphics.throwError("Please restart and input a valid ");
			graphics.throwError("");
		} catch (IOException e) {
			graphics.throwError("Cannot connect to server");
			graphics.throwError("Check your internet connection");
			graphics.throwError("If problem persists contact software administrator");

		}
	}

	// Reads from ClientGraphics
	public void clientListenThread() {
		clientListenThread = new Thread() {
			public void run() {
				while (!clientListenThread.isInterrupted()) {
					String username = graphics.getMessageUsername();
					String message = graphics.getMessage();

					// System.out.println(username);
					// System.out.println(message);

					if (username.length() > 0 && message.length() > 0) {
						try {
							outputData
									.writeUTF("m " + username + " " + message);
						} catch (IOException e) {
							graphics.throwError("Disconnected From Server");
							graphics.throwError("Check your connection and try again");
							graphics.throwError("If problem persists contact software administrator");
							stopAllThreads();

						}
					}

					try {
						this.sleep(200);
					} catch (InterruptedException e) {
						graphics.throwError("Internal Error Occured");
						graphics.throwError("Please Restart Client");
						graphics.throwError("clientListenThread(): Sleep unsuccessful");
						graphics.throwError("If problem persists contact software administrator");
						stopAllThreads();
					}
				}
			}
		};
		clientListenThread.start();
	}

	// Listens to the server for messages
	public void serverListenThread() {
		// System.out.println("CLIENT LISTEN");
		serverListenThread = new Thread() {
			@Override
			public void run() {
				while (!serverListenThread.isInterrupted()) {
					try {
						// System.out.println("In try");
						serverListen();
					} catch (IOException e) {
						graphics.throwError("Disconnected From Server");
						graphics.throwError("Check your connection and try again");
						graphics.throwError("If problem persists contact software administrator");
						stopAllThreads();
					}
				}
			}
		};
		serverListenThread.start();
	}

	// Listens to the server
	private void serverListen() throws IOException {
		// System.out.println("Server Listen called");

		String message = inputData.readUTF();

		// System.out.println(message);

		// If the command is a message
		if (message.charAt(0) == 'm') {
			message = message.substring(2);

			System.out.println(message);

			String username = "";
			for (int i = 0; i < message.length(); i++) {
				if (message.charAt(i) == ' ') {
					message = message.substring(i + 1);
					break;
				} else {
					username = username + message.charAt(i);
				}
			}

			graphics.updateMessages(username, message);

			// Checks if log in succeeded
			// NOT COMPLETE
			// NEEDED TO BE COMPLETED
		} else if (message.charAt(0) == 'l') {
			// If log in succeeded
			if (Character.toLowerCase(message.charAt(1)) == 't') {

				// If Log in failed
			} else if (Character.toLowerCase(message.charAt(1)) == 'f') {

				// Else throw error
			} else {

			}
			// If the message sent in is a list of users that are online
		} else if (message.charAt(0) == 'o') {
			// System.out.println("LIST OF ONLINE USERS");

			message = message.substring(2);
			List<String> onlineUsers = new ArrayList<String>();

			String tempUser = "";

			for (int i = 0; i < message.length(); i++) {
				if (message.charAt(i) == ' ') {
					onlineUsers.add(tempUser);
					tempUser = "";
				} else {
					tempUser = tempUser + message.charAt(i);
				}
			}

			onlineUsers.add(tempUser);

			// System.out.println("Adding online user" + x);
			graphics.addOnlineUser(onlineUsers);

		}

	}

	// Sends message to the server
	public void clientSendMessage(String message) throws IOException {
		// System.out.println(message);
		outputData.writeUTF(message);
	}

	private void stopAllThreads() {
		serverListenThread.interrupt();
		clientListenThread.interrupt();
	}
}
