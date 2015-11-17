package ServerV3_0;

public class MessageObject {
	private String message;
	private String srcUsername;
	
	public MessageObject(String srcUsername, String message){
		this.srcUsername = srcUsername;
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getSrcUsername(){
		return srcUsername;
	}
}
