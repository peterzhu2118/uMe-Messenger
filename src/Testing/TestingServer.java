package Testing;

import java.io.*;
import java.net.*;

public class TestingServer {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(19000);
			Socket connectionToClient = serverSocket.accept();
			DataOutputStream output = new DataOutputStream(
					connectionToClient.getOutputStream());
			DataInputStream dataInput = new DataInputStream(
					connectionToClient.getInputStream());

			while (true) {
				System.out.println(dataInput.readUTF());
			}
			/*
			 * while (true){ output.writeUTF("Testing"); try {
			 * Thread.sleep(100); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } }
			 */

		} catch (IOException e) {
			System.out.println("IOE");
			// e.printStackTrace();
		}

	}

}