package ServerV2_0;

import java.io.IOException;

import javax.swing.JFrame;

public class ServerGraphics extends JFrame{
	public ServerGraphics(){
		super("Server");
		this.setSize(200, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		try {
			System.out.println("Server Started");
			WellKnownPort ports = new WellKnownPort();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in main");
		}
	}
}
