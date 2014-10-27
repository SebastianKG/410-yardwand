/*
 * MockCodeDuplicationDetector - a mock class designed to replicate the
 * expected behaviour of CodeDuplicationDetector in the analysis package.
 * Part of the UBC CPSC 410 yardwand project.
 * 
 * Author: Eric Furugori
 */

package analysis;

public class MockCodeDuplicationDetector {
	
	/*
	 * sampleOutput1 is the last few lines of Simian being run on the repository:
	 * https://github.com/junit-team/junit
	 */
	private static String sampleOutput1 =
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
	private static String sampleOutput2 =
		    "Found 202 duplicate lines in the following files:\n" +
		    " Between lines 308 and 538 in /Users/ericlee/spring-framework/spring-framework-master/spring-context/src/test/java/org/springframework/beans/factory/support/QualifierAnnotationAutowireContextTests.java" +
		    " Between lines 288 and 518 in /Users/ericlee/spring-framework/spring-framework-master/spring-context/src/test/java/org/springframework/beans/factory/support/InjectAnnotationAutowireContextTests.java" +
		    "Found 65952 duplicate lines in 5673 blocks in 1566 files" +
		    "Processed a total of 349869 significant (904159 raw) lines in 5888 files" +
		    "Processing time: 2.774sec";
	
	public MockCodeDuplicationDetector() {
	}
	
	/**
	 * Two sample simian outputs are included.
	 * @param repoNum
	 * 		1: get sampleOutput1 (JUnit repo)
	 * 		else: get sampleOutput2 (Spring repo)
	 * @return
	 */
	public String getDuplicationString(int repoNum) {
		return repoNum == 1 ? sampleOutput1 : sampleOutput2;
	}

}
