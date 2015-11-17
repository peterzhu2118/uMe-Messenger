package ClientV1_1;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

public class ClientGraphics extends JFrame implements ActionListener {
	private JButton sendButton;
	private JList<String> listOfMessages;
	private static DefaultListModel<String> messageList;
	private static JTextField textMessage;
	private JScrollPane scrollableListOfMessages;
	private Container cont;
	private Client client;

	public ClientGraphics(String username, String address, int portNumber) {
		// Sets up the frame
		super("JAVA MESSAGE PROGRAM");
		this.setSize(400, 400);
		this.setLocation(100, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);

		// Creates a new container
		cont = new Container();

		// cont.setBackground(new Color(0, 255, 0));

		// Sets up the "SEND" button and puts it into the container
		sendButton = new JButton("SEND");
		sendButton.setSize(75, 40);
		sendButton.setLocation(this.getWidth() - 100, this.getHeight() - 90);
		sendButton.addActionListener(this);
		cont.add(sendButton);

		// Sets up the text field that allows the user to type in a message
		textMessage = new JTextField();
		textMessage.setSize(250, 40);
		textMessage.setLocation(this.getWidth() - 375, this.getHeight() - 90);
		cont.add(textMessage);

		messageList = new DefaultListModel<>();
		listOfMessages = new JList<>(messageList);
		scrollableListOfMessages = new JScrollPane(listOfMessages);
		scrollableListOfMessages.setSize(350, 275);
		scrollableListOfMessages.setLocation(this.getHeight() - 375,
				this.getWidth() - 375);
		cont.add(scrollableListOfMessages);

		// Adds the container to the JFrame
		this.add(cont);
		
		// Makes it visible
				this.setVisible(true);

		// Sets up the client program itself
		System.out.println("CONTRUCTOR");
		try {
			client = new Client(username, address, portNumber);
			client.clientListenThread();
		} catch (UnknownHostException e) {
			// Server cannot be found error
			throwError("Server not found");
		} catch (IOException e) {
			// Disconnected error
			throwError("Disconnected from server");
		}

		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(sendButton)) {
			// System.out.println("BUTTON PRESSED");
			sendMessage();
		}

	}
	
	// Private helper method
	// Sends message to the server
	private void sendMessage() {
		try {
			// If the message length is not 0
			if (textMessage.getText().length() != 0) {
				//System.out.println("MESSAGE IS SENT");
				//System.out.println("Sent - " + textMessage.getText());
				client.clientSendMessage(textMessage.getText());
				sentMessage(textMessage.getText());
				textMessage.setText("");
			} else {
				// If the space is 0, then do nothing
			}
		} catch (IOException e) {
			throwError("Cannot connect to server/Other user disconnected");
		}
	}

	// Adds an error to the chat dialogue and stops typing
	public static void throwError(String error) {
		textMessage.setEditable(false);
		textMessage.setText("");
		addTextToList("ERROR - " + error);
	}

	// Adds the string to the chat log
	private static void addTextToList(String text) {
		messageList.addElement(text);
	}
	
	// Takes in the received message and displays it
	public static void recievedMessage(String username, String message){
		addTextToList(username + ": " + message);
	}
	
	//Takes in the sent message and displays it
	public static void sentMessage(String message){
		addTextToList("Me: " + message);
	}
}
