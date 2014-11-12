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

public class SimianOutputParser {
	
	/*
	 * Sample simian output looks like:
	 * [...Processing text...]
	 * Found 9105 duplicate lines in 668 blocks in 275 files
	 * Processed a total of 41593 significant (120899 raw) lines in 813 files
	 */
	private static final Pattern SIMIAN_REGEX =
			Pattern.compile("Found (\\d*) duplicate lines in (\\d*) blocks in (\\d*) files");
	private Matcher matcher;
	
	// Fields to hold Simian statistics
	private int duplicateFileCount = 0;
	private int duplicateLineCount = 0;
	private int duplicateBlockCount = 0;
	
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
	 * 		The Simian analysis output in <code>String</code> format.
	 * @throws UnexpectedSimianContentException
	 * 		If the matcher cannot find the summary line in the Simian output,
	 * 		throw an exception.
	 */
	public void parse(String simianOutput) throws UnexpectedSimianContentException {
		matcher = SIMIAN_REGEX.matcher(simianOutput);
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
}
