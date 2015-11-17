/*
 * In the multimap, true is put if the message is from this current user
 */
package ClientV2_0;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MessageContainerObject {
	private Multimap<Boolean, String> messages;
	
	public MessageContainerObject(){
		messages = ArrayListMultimap.create();
	}
	
	public void addMessage(boolean fromCurrentUser, String message){
		messages.put(new Boolean(fromCurrentUser), message);
	}
	
	public Multimap<Boolean, String> getMessages(){
		return messages;
	}
}
