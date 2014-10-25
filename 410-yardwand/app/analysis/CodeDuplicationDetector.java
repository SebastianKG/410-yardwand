package analysis;

import java.io.IOException;
import java.io.InputStream;

public class CodeDuplicationDetector {

public static void main(String[] args){
		
		//test comment
		String[] commands = new String[5];
		commands[0] = "java";
		commands[1] = " -jar";
		commands[2] = " /Users/";
		// THIS IS USING MY OWN DIRECTORY TO ACCESS SIMIAN
		// WILL MODIFY ONCE PLAYFRAMEWORK IS WORKING
		commands[3] = "ericlee"; //ERIC: to test change your the name here
		commands[4] = "/simian-2.3.35.jar";
		
		
		String xmlFormatOutput = " -formatter=plain";
		
		String finalCommand = "java -jar simian";
		
		String curr_Directory = " \"*.java\"";
		String cmd= commands[0] + commands[1] + commands[2] + commands[3] + commands[4] + xmlFormatOutput + curr_Directory;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream is= p.getInputStream();
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
			String val="";
			if(s.hasNext())
			{
				val=s.next();
			}else
				val="";
			
			System.out.print(val);
			



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
