package ServerV0_1;

import java.io.IOException;

import ServerV0_2.Server;

public class ServerMain {

	public static void main(String[] args) {
		Server s;
		try {
			//System.out.println("in");
			s = new Server(9998, 9997);
			System.out.println("new server created");
			s.startServer();
			System.out.println("startserver ended");
		} catch (IOException e) {
			System.out.println("Error");
		}
		

	}

}
