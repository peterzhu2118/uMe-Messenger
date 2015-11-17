package Testing;

import java.io.*;
import java.net.*;

public class TestingClient {

	public static void main(String[] args) {
		try {
			Socket clientSocket = new Socket("localhost", 9998);
			
			DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
			
			outputStream.writeUTF("hello");
			
			while(true){
				System.out.println(inputStream.readUTF());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
