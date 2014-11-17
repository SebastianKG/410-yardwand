package analysis;

import java.io.IOException;
import java.io.InputStream;


public class CodeDuplicationDetector {

public static void main(String[] args){
		
		//test comment
		String[] commands = new String[5];
		commands[0] = "java";
		commands[1] = " -jar";
		commands[2] = " 410-yardwand";
		// THIS IS USING MY OWN DIRECTORY TO ACCESS SIMIAN

		commands[3] = "/lib"; //ERIC: to test change your the name here
		commands[4] = "/simian-2.3.35.jar";
		//TEST COMMENT:
		System.out.print("arrived at commands\n");
		
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
