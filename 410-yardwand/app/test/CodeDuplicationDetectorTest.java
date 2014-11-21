package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import analysis.CodeDuplicationDetector;

public class CodeDuplicationDetectorTest {

	@Test(expected=NullPointerException.class)
	public void simianInvalidInputs() {
		CodeDuplicationDetector cd = new CodeDuplicationDetector();

		//cd.method1(0);
		//cd.method1(10);
		//cd.method1(100);
	}
	
	public void simianRunCheck() {
		CodeDuplicationDetectorTest testStruct = new CodeDuplicationDetectorTest();
		String cmd = "java -jar 410-yardwand/lib/simian-2.3.35.jar";
		String compareValue = "Similarity Analyser 2.3.35 - http://www.harukizaemon.com/simian\nCopyright (c) 2003-2013 Simon Harris.  All rights reserved.\nSimian is not free unless used solely for non-commercial or evaluation purposes.\nUsage: [options] [files]\n    -balanceCurlyBraces[+|-]         Accounts for curly braces when breaking lines\n    -balanceParentheses[+|-]         Accounts for parentheses when breaking lines\n    -balanceSquareBrackets[+|-]      Accounts for square brackets when breaking lines\n    -config=FNAME                    Reads the configuration from the specified file\n    -defaultLanguage=LANG            Assumes files are in the specified language if none can be inferred\n    -excludes=SPEC                   Excludes files matching the specified pattern\n    -failOnDuplication[+|-|%]        Exits with a failure return code if duplication detected\n    -formatter=TYPE[:FNAME]          Uses the specified output format when reporting\n    -ignoreBlocks=START:END          Ignores all lines between START/END\n    -ignoreCharacterCase[+|-]        Matches character literals irrespective of case\n    -ignoreCharacters[+|-]           Completely ignores character literals\n    -ignoreCurlyBraces[+|-]          Completely ignores curly braces\n    -ignoreIdentifierCase[+|-]       Matches identifiers irresepctive of case\n    -ignoreIdentifiers[+|-]          Completely ignores identifiers\n    -ignoreLiterals[+|-]             Completely ignores all literals (strings, numbers and characters)\n    -ignoreModifiers[+|-]            Ignores modifiers (public, private, static, etc.)\n    -ignoreNumbers[+|-]              Completely ignores numbers\n    -ignoreOverlappingBlocks[+|-]    Ignores blocks that wholly or partially overlap\n    -ignoreRegions[+|-]              Ignores all lines between #region/#endregion\n    -ignoreStringCase[+|-]           Matches string literals irrespective of case\n    -ignoreStrings[+|-]              Completely ignores the contents of strings\n    -ignoreSubtypeNames[+|-]         Matches on similar type names (eg. Reader and FilterReader)\n    -ignoreVariableNames[+|-]        Completely ignores variable names (fields, parameters and locals)\n    -includes=SPEC                   Including files matching the specified pattern\n    -language=LANG                   Assumes ALL files are in the specified language\n    -reportDuplicateText[+|-]        Prints the duplicate text in reports\n    -threshold=COUNT                 Matches will contain at least the specified number of lines";		
		
		assertEquals(testStruct.ProcessBuilder(cmd), compareValue);
				
		
	}
	
	public String ProcessBuilder(String cmd){
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
		return result;
		
	}
	

}
