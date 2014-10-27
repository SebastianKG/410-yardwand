/*
 * SimianOutputParser - a singleton that parses the output of a Simian
 * (http://www.harukizaemon.com/simian/index.html) analysis. Part of
 * the UBC CPSC 410 yardwand project.
 * 
 * Author: Eric Furugori
 */

package fusion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import analysis.MockCodeDuplicationDetector;

public class SimianOutputParser {
	
	/*
	 * Sample simian output looks like:
	 * Found 9105 duplicate lines in 668 blocks in 275 files
	 * Processed a total of 41593 significant (120899 raw) lines in 813 files
	 */
	private static final Pattern simianPattern =
			Pattern.compile("Found (\\d*) duplicate lines in (\\d*) blocks in (\\d*) files");
	private static Matcher matcher;
	
	// Fields to hold Simian statistics
	private static int duplicateFileCount = 0;
	private static int duplicateLineCount = 0;
	private static int duplicateBlockCount = 0;
	
	private SimianOutputParser() {}

	private static class Instantiator {
		private static final SimianOutputParser INSTANCE = new SimianOutputParser();
	}
	
	public static SimianOutputParser getInstance() {
		return Instantiator.INSTANCE;
	}
	
	/**
	 * Parses the Simian output string for the duplication summary and retrieves the
	 * duplication amounts.
	 * @param simianOutput
	 * 		The Simian analysis output in String format.
	 * @throws UnexpectedSimianContentException
	 * 		If the matcher cannot find the summary line in the Simian output,
	 * 		throw an exception.
	 */
	public void parse(String simianOutput) throws UnexpectedSimianContentException {
		matcher = simianPattern.matcher(simianOutput);
		if (matcher.find()) {
			duplicateLineCount = Integer.parseInt(matcher.group(1));
			duplicateBlockCount = Integer.parseInt(matcher.group(2));
			duplicateFileCount = Integer.parseInt(matcher.group(3));
		} else {
			throw new UnexpectedSimianContentException("Invalid Simian output format.");
		}
	}
	
	/**
	 * Get the duplicate line count in the most recently parsed Simian output.
	 * @return duplicateLineCount
	 * 		The amount of duplicate lines in the last Simian analysis parse.
	 */
	public int getDuplicateLineCount() {
		return duplicateLineCount;
	}
	
	/**
	 * Get the duplicate block count in the most recently parsed Simian output.
	 * @return duplicateBlockCount
	 * 		The amount of duplicate blocks in the last Simian analysis parse.
	 */
	public int getDuplicateBlockCount() {
		return duplicateBlockCount;
	}
	
	/**
	 * Get the duplicate file count in the most recently parsed Simian output.
	 * @return duplicateFileCount
	 * 		The amount of duplicate files in the last Simian analysis parse.
	 */
	public int getDuplicateFileCount() {
		return duplicateFileCount;
	}
	
	// TODO: remove this
	// Written for debugging purposes.
	// This is also an example of how to instantiate and utilize this class.
	public static void main(String[] args) {
		String s = null;
		try {
			MockCodeDuplicationDetector mcdd = new MockCodeDuplicationDetector();
			s = mcdd.getDuplicationString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SimianOutputParser parser = SimianOutputParser.getInstance();
		try {
			parser.parse(s);
		} catch (UnexpectedSimianContentException e) {
			e.printStackTrace();
		}
		System.out.println("f: " + duplicateFileCount);
		System.out.println("b: " + duplicateBlockCount);
		System.out.println("l: " + duplicateLineCount);
	}
}
