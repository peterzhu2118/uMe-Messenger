package ClientV2_0;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.common.collect.Multimap;

public class ClientGraphics implements ActionListener {
	private JFrame window;

	private JButton sendButton;

	private JList<String> listOfMessages;
	private DefaultListModel<String> messageList;
	private JScrollPane scrollableListOfMessages;

	private Vector<String> userList;
	private DefaultComboBoxModel<String> defaultListOfUserModel;
	private JComboBox<String> listOfUsers;

	private JTextField textMessage;
	private Container cont;

	private String sendToUsername;
	private String messageToUser;

	private JLabel usernameLabel;
	private JLabel serverAddressLabel;
	private JLabel portNumberLabel;

	private HashMap<String, MessageContainerObject> listOfMessageObject;

	public ClientGraphics() {
		// Sets up the frame
		window = new JFrame("JAVA MESSAGE PROGRAM");
		window.setSize(600, 500);
		window.setLocation(100, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		// Creates a new container
		cont = new Container();
		window.add(cont);

		usernameLabel = new JLabel();
		usernameLabel.setText("Username: ");
		usernameLabel.setSize(200, 20);
		usernameLabel.setLocation(10, 400);
		cont.add(usernameLabel);

		serverAddressLabel = new JLabel();
		serverAddressLabel.setText("Server Address: ");
		serverAddressLabel.setSize(200, 20);
		serverAddressLabel.setLocation(10, 420);
		cont.add(serverAddressLabel);

		portNumberLabel = new JLabel();
		portNumberLabel.setText("Port Number: ");
		portNumberLabel.setSize(200, 20);
		portNumberLabel.setLocation(10, 440);
		cont.add(portNumberLabel);

		// Sets up the "SEND" button and puts it into the container
		sendButton = new JButton("SEND");
		sendButton.setSize(75, 40);
		sendButton
				.setLocation(window.getWidth() - 100, window.getHeight() - 90);
		sendButton.addActionListener(this);
		cont.add(sendButton);

		// Sets up the text field that allows the user to type in a message
		textMessage = new JTextField();
		textMessage.setSize(250, 40);
		textMessage.setLocation(window.getWidth() - 375,
				window.getHeight() - 90);
		textMessage.setEnabled(false);
		cont.add(textMessage);

		// Sets up the user list
		/*
		 * userList = new DefaultListModel<>(); listOfUsers = new
		 * JList<>(userList); scrollableListOfUsers = new
		 * JScrollPane(listOfUsers); scrollableListOfUsers.setSize(200, 400);
		 * scrollableListOfUsers.setLocation(10, 50);
		 * cont.add(scrollableListOfUsers);
		 */

		userList = new Vector<>();
		defaultListOfUserModel = new DefaultComboBoxModel<>(userList);
		defaultListOfUserModel.addElement("Online Users");
		listOfUsers = new JComboBox<>(defaultListOfUserModel);
		listOfUsers.setSize(200, 75);
		listOfUsers.setLocation(20, 50);
		listOfUsers.addActionListener(this);
		cont.add(listOfUsers);

		/*
		 * for (int i = 0; i < 100; i++) { userList.addElement("LOL " + i); }
		 */

		// Sets up the message list
		messageList = new DefaultListModel<>();
		listOfMessages = new JList<>(messageList);
		scrollableListOfMessages = new JScrollPane(listOfMessages);
		scrollableListOfMessages.setSize(350, 350);
		scrollableListOfMessages.setLocation(225, 50);
		cont.add(scrollableListOfMessages);

		listOfMessageObject = new HashMap<>();

		sendToUsername = "";
		messageToUser = "";

		// Makes it visible
		window.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(sendButton)) {
			// System.out.println("BUTTON PRESSED");

			if (textMessage.getText().length() > 0) {
				sendMessage();
			}
		} else if (e.getSource().equals(listOfUsers)
				&& listOfUsers.getSelectedItem() != "Online Users"
				&& listOfUsers.getSelectedItem() != null) {
			changeUser((String) listOfUsers.getSelectedItem());
		} else if (e.getSource().equals(listOfUsers)
				&& listOfUsers.getSelectedItem().equals("Online Users")
				&& listOfUsers.getSelectedItem() != null) {
			removeAllFromMessageList();
			textMessage.setText("");
			textMessage.setEnabled(false);
		}
	}

	public void setUsernameLabel(String username) {
		usernameLabel.setText("Username: " + username);
	}

	public void setServerAddressLabel(String serverAddress) {
		serverAddressLabel.setText("Server Address: " + serverAddress);
	}

	public void setPortNumberLabel(int portNumber) {
		portNumberLabel.setText("Port Number: " + portNumber);
	}

	// Popup with a text box
	public String inputPopup(String message) {
		return (String) JOptionPane.showInputDialog(window, message, "",
				JOptionPane.QUESTION_MESSAGE);
	}

	// Popup with only yes no functionality
	public boolean yesNoPopup(String message) {
		if (JOptionPane.showConfirmDialog(window, message, "",
				JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
			return true;
		}
		return false;
	}

	// Private helper method
	// Sends message to the server
	private void sendMessage() {
		// If the message length is not 0
		if (textMessage.getText().length() != 0) {
			// System.out.println("MESSAGE IS SENT");

			messageToUser = textMessage.getText();
			sendToUsername = (String) listOfUsers.getSelectedItem();

			// System.out.println(sendToUsername);
			// System.out.println(listOfMessageObject.containsKey(sendToUsername));

			listOfMessageObject.get(sendToUsername).addMessage(true,
					messageToUser);

			updateMessages("Me", messageToUser);

			textMessage.setText("");
		}
	}

	// Adds an error to the chat dialogue
	public void throwError(String error) {
		textMessage.setEditable(false);
		textMessage.setText("");
		addTextToMessageList("ERROR - " + error);
	}

	// Gets the message username
	public String getMessageUsername() {
		String temp = sendToUsername;
		sendToUsername = "";
		return temp;
	}

	// Gets the message
	public String getMessage() {
		String temp = messageToUser;
		messageToUser = "";
		return temp;
	}

	// Adds the string to the chat log
	private void addTextToMessageList(String message) {
		System.out.println("Message added: " + message);
		// listOfMessages.removeAll();
		messageList.addElement(message);
		
	}

	// Clears the message list
	private void removeAllFromMessageList() {
		messageList.clear();
	}

	// Takes in the received message and displays it
	public void updateMessages(String username, String message) {
		// Adds it to the list of messages
		// System.out.println(username);
		// System.out.println(listOfMessageObject.containsKey(username));

		if (username.equals("Me"))
			listOfMessageObject.get(listOfUsers.getSelectedItem()).addMessage(true, message);
		
		else
			listOfMessageObject.get(username).addMessage(false, message);

		// If the message is from the current user selected
		// then add it to the messages window
			
		if (username == "Me")
			addTextToMessageList("Me : " + message);
		else
			addTextToMessageList(username + " : " + message);
	}

	// Adds an online user
	public void addOnlineUser(List<String> users) {
		// Checks if the user already exists
		userList.removeAllElements();

		userList.addElement("Online Users");

		for (String x : users) {
			if (!listOfMessageObject.containsKey(x)) {
				// System.out.println("Added " + x);
				listOfMessageObject.put(x, new MessageContainerObject());
			}

			userList.addElement(x);

		}
	}

	private void changeUser(String username) {
		textMessage.setText("");
		textMessage.setEnabled(true);

		// Clears the message list
		removeAllFromMessageList();

		// Gets all the messages for the conversation
		// System.out.println(username);

		Multimap<Boolean, String> x = listOfMessageObject.get(username)
				.getMessages();

		// List of messages
		ArrayList<String> messages = new ArrayList<String>();

		// Turns all the entries in the multimap into a arraylist
		for (Map.Entry<Boolean, String> i : x.entries()) {
			// If the message is from the current user
			if (i.getKey() == true) {
				messages.add("Me : " + i.getValue());
			} else {
				messages.add(username + " : " + i.getValue());
			}
		}

		// Reverses the array so that time order is correct
		Collections.reverse(messages);

		// Adds every element in the array to the list of messages
		for (String i : messages) {
			addTextToMessageList(i);
		}
	}
}
