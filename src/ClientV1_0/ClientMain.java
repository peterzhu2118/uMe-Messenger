package ClientV1_0;

import java.util.Scanner;

public class ClientMain {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		/*System.out.println("Whats the server address?");
		String address = scan.nextLine();*/
		
		System.out.println("Whats the port number?");
		int portNumber = scan.nextInt();
		
		ClientGraphics g = new ClientGraphics("localhost", portNumber);

	}

}
