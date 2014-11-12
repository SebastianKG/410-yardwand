package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fusion.SimianOutputParser;
import fusion.UnexpectedSimianContentException;

public class SimianOutputParserTest {
	
	private SimianOutputParser parser = SimianOutputParser.getInstance();
	
	/*
	 * sampleOutput1 is the last few lines of Simian being run on the repository:
	 * https://github.com/junit-team/junit
	 */
	private static final String SAMPLE_OUTPUT1 =
			"Found 58 duplicate lines in the following files: \n" +
			" Between lines 24 and 131 in /Users/ericlee/junit-master-simian/junit-master/src/test/java/org/junit/tests/running/methods/TestMethodTest.java\n" +
			" Between lines 25 and 132 in /Users/ericlee/junit-master-simian/junit-master/src/test/java/org/junit/tests/running/methods/ParameterizedTestMethodTest.java\n" +
			"Found 1236 duplicate lines in 137 blocks in 44 files\n" + 
			"Processed a total of 17200 significant (38130 raw) lines in 392 files\n" +
			"Processing time: 0.327sec";
	
	/*
	 * sampleOutput2 is the last few lines of Simian being run on the repository:
	 * https://github.com/spring-projects/spring-framework
	 */
	private static final String SAMPLE_OUTPUT2 =
		    "Found 202 duplicate lines in the following files:\n" +
		    " Between lines 308 and 538 in /Users/ericlee/spring-framework/spring-framework-master/spring-context/src/test/java/org/springframework/beans/factory/support/QualifierAnnotationAutowireContextTests.java" +
		    " Between lines 288 and 518 in /Users/ericlee/spring-framework/spring-framework-master/spring-context/src/test/java/org/springframework/beans/factory/support/InjectAnnotationAutowireContextTests.java" +
		    "Found 65952 duplicate lines in 5673 blocks in 1566 files" +
		    "Processed a total of 349869 significant (904159 raw) lines in 5888 files" +
		    "Processing time: 2.774sec";
	
	private static final String LINE_BREAK = "Found 65952 duplicate lines\n in 5673 blocks in 1566 files";
	private static final String NULL = null;
	private static final String EMPTY = "";
	
	/**
	 * Show examples of good input and check that getters give the correct numbers.
	 * @throws UnexpectedSimianContentException
	 */
	@Test
	public void testSamples() throws UnexpectedSimianContentException {
		parser.parse(SAMPLE_OUTPUT1);
		assertEquals(parser.getDuplicateLineCount(), 1236);
		assertEquals(parser.getDuplicateBlockCount(), 137);
		assertEquals(parser.getDuplicateFileCount(), 44);
		parser.parse(SAMPLE_OUTPUT2);
		assertEquals(parser.getDuplicateLineCount(), 65952);
		assertEquals(parser.getDuplicateBlockCount(), 5673);
		assertEquals(parser.getDuplicateFileCount(), 1566);
	}
	
	/**
	 * We do not want a string such that the summary section has a line break in it.
	 * @throws UnexpectedSimianContentException
	 */
	@Test(expected = UnexpectedSimianContentException.class)
	public void testLineBreak() throws UnexpectedSimianContentException {
		parser.parse(LINE_BREAK);
	}
	
	/**
	 * Null case. Should get <code>NullPointerException</code> and not
	 * <code>UnexpectedSimianContentException</code>.
	 * @throws UnexpectedSimianContentException
	 */
	@Test(expected = NullPointerException.class)
	public void testNull() throws UnexpectedSimianContentException {
		parser.parse(NULL);
	}
	
	/**
	 * Empty string case. Should get <code>UnexpectedSimianContentException</code>
	 * and not <code>NullPointerException</code>.;
	 * @throws UnexpectedSimianContentException
	 */
	@Test(expected = UnexpectedSimianContentException.class)
	public void testEmpty() throws UnexpectedSimianContentException {
		parser.parse(EMPTY);
	}
}
