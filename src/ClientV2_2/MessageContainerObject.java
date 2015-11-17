/*
 * In the multimap, true is put if the message is from this current user
 */
package ClientV2_2;

import java.util.ArrayList;

public class MessageContainerObject {
	private ArrayList<String> messages;
	
	public MessageContainerObject(){
		messages = new ArrayList<>();
	}
	
	public void addMessage(String message){
		messages.add(message);
		
		/*for(Map.Entry<Boolean, String> x : messages.entries()){
			System.out.println((x.getKey() ? "Me " : "User ") + x.getValue());
		}
		System.out.println();*/
	}
	
	public ArrayList<String> getMessages(){
		return messages;
	}
}
