package ServerV0_3;

import java.io.*;
import java.util.*;

public class ServerFileIO {
	File file;
	PrintWriter output;
	Scanner input;
	String user1, user2;

	// Contructor that takes in the usernames of the users
	public ServerFileIO(String username1, String username2) throws IOException {
		// Sets the naming convention for the files
		// The convention is:
		// <Smaller Username><Larger Username>.txt

		user1 = username1;
		user2 = username2;

		if (username1.compareTo(username2) < 0)
			file = new File(System.getProperty("user.home")
					+ "\\Server Message Files\\" + username1 + username2
					+ ".txt");
		else if (username1.compareTo(username2) > 0)
			file = new File(System.getProperty("user.home")
					+ "\\Server Message Files\\" + username2 + username1
					+ ".txt");
		else {
			throw new IllegalArgumentException("Both usernames are the same");
		}

		file.getParentFile().mkdirs();
		file.createNewFile();

		output = new PrintWriter(new FileWriter(file, true));
		input = new Scanner(file);
	}

	// Adds the message to the file
	public void addMessage(String username, String message) {
		// System.out.println("MESSAGE ADDED TO FILE");
		// Checks if the user is one of the 2 users
		if (username == user1 || username == user2) {
			// Writes the message to the file
			output.println(username + " " + message);
			output.flush();
		} else
			throw new IllegalArgumentException("Invalid Username");
	}

	// Gets all messages and returns as a ArrayList
	public ArrayList<String> getAllMessages() {
		ArrayList<String> messages = new ArrayList<>();

		while (input.hasNext()) {
			messages.add(input.nextLine());
		}

		return messages;
	}
}
