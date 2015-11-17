package ServerV0_3;

import java.io.IOException;
import java.util.ArrayList;

public class ServerMain {

	public static void main(String[] args) {
		ArrayList<Server> servers = new ArrayList<>();
		
		for(int i = 9900; i < 10000; i++){
			try {
				Server temp = new Server(i, "Client " + i);
				
				temp.start();
				
				servers.add(temp);
				} catch (IOException e) {
				
			}
		}
		

	}

}
