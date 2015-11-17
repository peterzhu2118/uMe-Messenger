package ServerV2_0;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ServerControl {
	private static List<Server> servers;

	static {
		servers = new ArrayList<>();
		keepServerListCleanThread();
	}

	// Adds a new server using the specified socket object
	public static void addServer(Socket x) throws IOException {
		Server s = new Server(x);
		s.start();
		servers.add(s);
	}

	// Cleans the list every 100 miliseconds
	private static void keepServerListCleanThread() {
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					// System.out.println("Server list cleaned");
					keepServerListClean();
					try {
						sleep(100);
					} catch (InterruptedException e) {

					}
				}
			}
		};
		t.start();
	}

	// Removes the unactive connections
	private static void keepServerListClean() {
		for (int i = 0; i < servers.size(); i++) {
			// System.out.println(servers.get(i).checkConnection());
			if (servers.get(i).checkConnection()) {
				System.out.println("A connection is cleaned " + servers.get(i).getUsername());
				servers.remove(i);
			}
		}
	}
}
