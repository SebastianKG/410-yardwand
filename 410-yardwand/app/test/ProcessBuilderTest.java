package test;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import org.junit.Test;

public class ProcessBuilderTest {

	@Test
	public void test() {

		// test comment
		String[] commands = new String[5];
		commands[0] = "java";
		commands[1] = " -jar";
		commands[2] = " 410-yardwand";

		commands[3] = "/lib";
		commands[4] = "/simian-2.3.35.jar";
		// TEST COMMENT:
		System.out.print("arrived at commands\n");

		String xmlFormatOutput = " -formatter=plain ";

		String finalCommand = "java -jar simian";

		String currDirectory = "\"410-yardwand/lib/";
		// String currDirectory = "\"";

		// String parseFileType = "**/*.java\"";
		String parseFileType = "";

		String cmd = commands[0] + commands[1] + commands[2] + commands[3]
				+ commands[4]; // + xmlFormatOutput+ currDirectory +
								// parseFileType;
		System.out.print(cmd + "\n");

		System.out.print("PROCESS BUILDER: \n");

		// THE PROCESS BUILDER:

		ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
		Process b = null;
		try {
			b = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				b.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = builder.toString();
		System.out.print(result);

	}

}
