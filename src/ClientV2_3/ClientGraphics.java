package ClientV2_3;

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

	private JMenuBar menuBar;
	private JMenu help;
	private JMenuItem instructions;
	private JMenuItem about;

	private JList<String> listOfMessages;
	private DefaultListModel<String> messageList;
	private JScrollPane scrollableListOfMessages;

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
		window = new JFrame("uMe Client Version 2.3");
		window.setSize(600, 500);
		window.setLocation(100, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		// Creates a new container
		cont = new Container();
		window.add(cont);

		menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);

		help = new JMenu("Help");
		menuBar.add(help);

		instructions = new JMenuItem("Instructions");
		instructions.addActionListener(this);
		help.add(instructions);

		about = new JMenuItem("About");
		about.addActionListener(this);
		help.add(about);

		usernameLabel = new JLabel();
		usernameLabel.setText("Username: ");
		usernameLabel.setSize(200, 20);
		usernameLabel.setLocation(10, 370);
		cont.add(usernameLabel);

		serverAddressLabel = new JLabel();
		serverAddressLabel.setText("Server Address: ");
		serverAddressLabel.setSize(200, 20);
		serverAddressLabel.setLocation(10, 390);
		cont.add(serverAddressLabel);

		portNumberLabel = new JLabel();
		portNumberLabel.setText("Port Number: ");
		portNumberLabel.setSize(200, 20);
		portNumberLabel.setLocation(10, 410);
		cont.add(portNumberLabel);

		// Sets up the "SEND" button and puts it into the container
		sendButton = new JButton("SEND");
		sendButton.setSize(75, 40);
		sendButton.setLocation(window.getWidth() - 100,
				window.getHeight() - 120);
		sendButton.addActionListener(this);
		cont.add(sendButton);

		// Sets up the text field that allows the user to type in a message
		textMessage = new JTextField();
		textMessage.setSize(250, 40);
		textMessage.setLocation(window.getWidth() - 375,
				window.getHeight() - 120);
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

		// defaultListOfUserModel = new DefaultComboBoxModel<>(userList);
		// defaultListOfUserModel.addElement("Online Users");
		// listOfUsers = new JComboBox<>(defaultListOfUserModel);
		listOfUsers = new JComboBox<>();
		listOfUsers.addItem("Online Users");
		listOfUsers.setSize(200, 75);
		listOfUsers.setLocation(20, 20);
		listOfUsers.addActionListener(this);
		cont.add(listOfUsers);

		// userList.set(0, "hello");
		/*
		 * for (int i = 0; i < 100; i++) { userList.addElement("LOL " + i); }
		 */

		// Sets up the message list
		messageList = new DefaultListModel<>();
		listOfMessages = new JList<>(messageList);
		scrollableListOfMessages = new JScrollPane(listOfMessages);
		scrollableListOfMessages.setSize(350, 350);
		scrollableListOfMessages.setLocation(225, 20);
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
			System.out.println("CHANGE USER");

			/*
			 * if (((String) listOfUsers.getSelectedItem()).length() > 14) {
			 * System.out.println("Stuff: " + ((String)
			 * listOfUsers.getSelectedItem()) .substring(((String) listOfUsers
			 * .getSelectedItem()).length() - 14)); }
			 */

			if (((String) listOfUsers.getSelectedItem()).length() > 14
					&& ((String) listOfUsers.getSelectedItem())
							.substring(
									((String) listOfUsers.getSelectedItem())
											.length() - 14).equals(
									" - New Message")) {
				String temp = ((String) listOfUsers.getSelectedItem())
						.substring(0, ((String) listOfUsers.getSelectedItem())
								.length() - 14);

				// System.out.println(temp);

				int tempIndex = listOfUsers.getSelectedIndex();

				listOfUsers.removeItemAt(listOfUsers.getSelectedIndex());

				listOfUsers.insertItemAt(temp, tempIndex);

				listOfUsers.setSelectedIndex(tempIndex);

				changeUser(temp);

				// listOfUsers.repaint();
			} else

				changeUser(listOfUsers
						.getItemAt(listOfUsers.getSelectedIndex()));

		} else if (e.getSource().equals(listOfUsers)
				&& listOfUsers.getSelectedItem().equals("Online Users")
				&& listOfUsers.getSelectedItem() != null) {
			removeAllFromMessageList();
			textMessage.setText("");
			textMessage.setEnabled(false);

		} else if (e.getSource().equals(about)) {
			JOptionPane.showMessageDialog(window, new ImageIcon(
					"Images\\logo.jpg"), "About", JOptionPane.PLAIN_MESSAGE);

		} else if (e.getSource().equals(instructions)) {
			String message = "This is a simple messaging program that \n connects to the server using TCP port 19000. \n "
					+ "Created and maintained by Peter Zhu. \n \n ©Peter Zhu 2015. DO NOT DISTRIBUTE.";
			JOptionPane.showMessageDialog(window, message);
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
			// System.out.println("MESSAG	E IS SENT");

			messageToUser = textMessage.getText();
			sendToUsername = (String) listOfUsers.getSelectedItem();

			// System.out.println(sendToUsername);
			// System.out.println(listOfMessageObject.containsKey(sendToUsername));

			// listOfMessageObject.get(sendToUsername).addMessage(true,messageToUser);

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
		// System.out.println("Message added: " + message);
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

		if (username == "Me")
			listOfMessageObject.get(listOfUsers.getSelectedItem()).addMessage(
					username + " : " + message);
		else
			listOfMessageObject.get(username).addMessage(
					username + " : " + message);

		System.out.println(username + " : " + message);
		// System.out.println("Message put from me " + message)

		// If the message is from the current user selected
		// then add it to the messages window
		/*
		 * || ((String) listOfUsers.getSelectedItem()).substring(0, ((String)
		 * listOfUsers.getSelectedItem()).length() - 14) .equals(username)
		 */

		if (((String) listOfUsers.getSelectedItem()).equals(username)) {
			addTextToMessageList(username + " : " + message);

		} else if (username == "Me"
				&& !((String) listOfUsers.getSelectedItem())
						.equals("Online Users"))
			addTextToMessageList("Me : " + message);
		else {
			System.out.println("ELSE");
			for (int i = 0; i < listOfUsers.getItemCount(); i++) {
				if (listOfUsers.getItemAt(i).equals(username)) {
					String temp = listOfUsers.getItemAt(i) + " - New Message";

					listOfUsers.removeItemAt(i);

					listOfUsers.insertItemAt(temp, i);

					System.out.println("Changed to: "
							+ listOfUsers.getItemAt(i));
					// System.out.println(userList.get(i));
					break;
				} else if (listOfUsers.getItemAt(i).length() > 14
						&& listOfUsers
								.getItemAt(i)
								.substring(
										listOfUsers.getItemAt(i).length() - 14)
								.equals(username)) {
					System.out.println("BReak");
					break;
				}
			}
		}
	}

	// Adds an online user
	public void addOnlineUser(List<String> users) {
		// Checks if the user already exists
		// listOfUsers.removeAllItems();

		// listOfUsers.addItem("Online Users");

		// System.out.println(listOfUsers.getItemAt(1));

		ArrayList<String> usersList = new ArrayList<>();

		for (int i = 1; i < listOfUsers.getItemCount(); i++) {
			usersList.add(listOfUsers.getItemAt(i));
		}

		for (String x : users) {
			if (!listOfMessageObject.containsKey(x)) {
				// System.out.println("Added " + x);
				listOfMessageObject.put(x, new MessageContainerObject());

				listOfUsers.addItem(x);

				// System.out.println("User added: " + x);
			} else {
				usersList.remove(x);
				usersList.remove(x + " - New Message");
			}
		}

		for (String x : usersList) {
			listOfUsers.removeItem(x);
			listOfUsers.removeItem(x + " - New Message");
			listOfMessageObject.remove(x);
		}
	}

	private void changeUser(String username) {
		textMessage.setText("");
		textMessage.setEnabled(true);

		// Clears the message list
		removeAllFromMessageList();

		// Gets all the messages for the conversation
		// System.out.println(username);

		ArrayList<String> x = listOfMessageObject.get(username).getMessages();

		for (String i : x) {
			addTextToMessageList(i);
		}

		/*
		 * System.out.println(username); System.out.println(messages.size());
		 * 
		 * for (String y : messages) { System.out.println("Message in list is "
		 * + y); }
		 * 
		 * // Reverses the `array so that time order is correct //
		 * Collections.reverse(messages);
		 * 
		 * // Adds every element in the array to the list of messages for
		 * (String i : messages) { addTextToMessageList(i); }
		 */
	}
}
