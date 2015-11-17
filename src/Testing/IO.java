package Testing;

import java.io.*;

public class IO {

	public static void main(String[] args) throws FileNotFoundException {

		try {
			File file = new File("D:\\test1.txt");

			System.out.println("FILE CREATEd");

			if (!file.exists())
				if (file.createNewFile()) {
					System.out.println("File created");
				} else
					System.out.println("FIle exists");

			PrintWriter output = new PrintWriter(new FileWriter(file, true));

			for (int i = 0; i < 2; i++) {
				output.println("hello");
				output.flush();
			}
			
			
		} catch (IOException e) {
			System.out.println("File isnt found");
		}

		// System.out.println(System.getProperty("user.home"));
	}

}
