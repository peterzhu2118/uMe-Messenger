package ServerV1_0;

import java.io.*;
import java.net.*;

public class WellKnownPort {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private final int portNumber;
	private final int queueLength;

	public WellKnownPort() throws IOException{
		this(19000);
	}
	
	public WellKnownPort(int portNumber) throws IOException{
		this.portNumber = portNumber;
		this.queueLength = 1000;
		
		serverSocket = new ServerSocket(portNumber, queueLength);
		
		startConnectionSetupThread();
	}

	// Tells ServerControl to create a new Server object when a new connection
	// is accepted
	private void startConnectionSetupThread() {
		// Runs in seperate thread
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						clientSocket = serverSocket.accept();
						ServerControl.addServer(clientSocket);
					} catch (IOException e) {

					}

				}
			}
		};
		t.start();
	}

}
