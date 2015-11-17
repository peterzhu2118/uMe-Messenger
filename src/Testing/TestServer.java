package Testing;

import java.io.IOException;
import java.net.*;

public class TestServer {
	public static void main(String args[]){
		try {
			ServerSocket serverSocket = new ServerSocket(19000);
			System.out.println("Server Socket created");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Client Socket created");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
