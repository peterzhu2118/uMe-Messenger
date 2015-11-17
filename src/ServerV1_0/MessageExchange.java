package ServerV1_0;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MessageExchange {
	private static HashSet<String> onlineUsers;
	private static Multimap<String, MessageObject> messages;

	static {
		onlineUsers = new HashSet<>();
		messages = ArrayListMultimap.create();
	}
	
	public static synchronized boolean isUserOnline(String username){
		return onlineUsers.contains(username);
	}
	
	public static synchronized HashSet<String> getOnlineUsers(){
		return onlineUsers;
	}

	// Adds a user online to the HashMap
	public static synchronized void addOnlineUser(String username) {
		// Checks if the user is already online
		if (!onlineUsers.contains(username))
			onlineUsers.add(username);
	}

	// Removes a user that is currently online
	public static synchronized void removeUser(String username) {
		// Checks if the user was online
		if (onlineUsers.contains(username))
			onlineUsers.remove(username);
	}

	// Returns if the user is currently online
	public static synchronized boolean isOnline(String username) {
		return onlineUsers.contains(username);
	}

	// Adds a message to the waiting list, takes in the source username,
	// destination username and the message
	public static synchronized void addMessage(String srcUsername,
			String destUsername, String message) {
		// If the user that its sent from and the user that is sending to is
		// online
		
		System.out.println("Message added");
		
		if (onlineUsers.contains(srcUsername) && onlineUsers.contains(destUsername))
			messages.put(destUsername, new MessageObject(srcUsername, message));
	}

	// Returns a multimap with the soruce usernames and the messages for a
	// specific destination username
	public static synchronized Multimap<String, String> getMessages(
			String username) {
		Multimap<String, String> tempMap = ArrayListMultimap.create();

		Collection<MessageObject> tempList = messages.get(username);

		for (MessageObject x : tempList) {
			tempMap.put(x.getSrcUsername(), x.getMessage());
		}
		
		messages.removeAll(username);

		return tempMap;
	}

}
