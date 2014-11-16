package test;

import java.io.IOException;
import java.io.InputStream;

public class CommandLineTest {

	public static void main(String[] args) {

		String[] commands = new String[10];
		commands[0] = "ls";
		commands[1] = "pwd";
		commands[2] = "ls";
		commands[3] = "cd 410";
		commands[4] = " \"*.java\"";
		// cd 410\ Project/
		commands[5] = "cd 410\\ Project/";
		// cd 410-yardwand/
		commands[6] = "cd 410-yardwand/";
		// cd lib/
		commands[7] = "cd lib/";
		commands[8] = "ls 410-yardwand/lib/";

		System.out.print("Arrived at portion before command line calls\n");
		// System.out.print(" \"*.java\" \n");
		// System.out.print("\\\\n" );
		// System.out.print("cd 410\\ Project/\n");
		// System.out.print("cd 410-yardwand/\n");

		String cmd = commands[0];
		String cmd2 = commands[2];
		String cmd3 = commands[6] + " && " + commands[7] + " && " + commands[0];
		String cmd4 = commands[6] + " && " + commands[0];

		try {
			System.out.print("Inside try catch block:\n");
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream is = p.getInputStream();
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			String val = "";
			if (s.hasNext()) {
				val = s.next();
			} else
				val = "";

			System.out.print(val);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.print("Inside second try catch block:\n");

			Process p = Runtime.getRuntime().exec(commands[8]);
			InputStream is = p.getInputStream();
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			String val = "";
			if (s.hasNext()) {
				val = s.next();
			} else
				val = "";

			System.out.print(val);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
