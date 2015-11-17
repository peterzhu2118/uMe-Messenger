package ServerV3_0;

import java.awt.Font;
import java.io.IOException;

import javax.swing.*;

public class ServerGraphics extends JFrame{
	private JLabel startedLabel;
	
	public ServerGraphics(){
		super("Server");
		this.setSize(400, 200);
		this.setLocation(20, 20);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		startedLabel = new JLabel("Server Running");
		startedLabel.setFont(new Font("Times New Roman", 0, 50));
		startedLabel.setLocation(50, 20);
		startedLabel.setSize(150, 50);
		this.add(startedLabel);
		
		this.setVisible(true);
		
		try {
			System.out.println("Server Started");
			WellKnownPort ports = new WellKnownPort();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception in main");
		}
	}
}
