/*
 * List of commands sent to client
 * "m" <Src Username> <Message> : message sent to client
 * "l" <T/F Boolean> : tells client if the log in succeeded
 * "o" <List of Online Users>... : tells the client the users that are online
 * 
 * List of commands sent from client
 * "l" <String Username> <String password> : Logs in the user
 * "m" <Dest Username> <Message> : Message to another client
 */


package ServerV1_0;

import java.io.IOException;
import java.util.ArrayList;

public class ServerMain {

	public static void main(String[] args) {
		try {
			System.out.println("Server Started");
			WellKnownPort ports = new WellKnownPort();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in main");
		}

	}

}
