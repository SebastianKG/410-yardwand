package test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import models.Stat;

import org.junit.Test;
import org.junit.Before;

import fusion.InvalidRepositoryURLException;
import fusion.MetricComparator;
import fusion.Repository;

public class MetricComparatorTest {

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
	
	private String URL1 = "https://github.com/junit-team/junit";
	private String URL2 = "https://github.com/spring-projects/spring-framework";
	private Repository repo1;
	private Repository repo2;
	
	/**
	 * Ensure that the Repositories are made.
	 * @throws InvalidRepositoryURLException
	 */
	@Before
	public void testValidRepos() throws InvalidRepositoryURLException {
		repo1 = new Repository(URL1);
		repo2 = new Repository(URL2);
		assertEquals(URL1, repo1.getUrl());
		assertEquals(URL2, repo2.getUrl());
	}
	
	@Test
	public void testMetricComparator() {
		MetricComparator mc = new MetricComparator(repo1, repo2);
		List<Stat> stat1 = mc.getRepo1StatList();
		List<Stat> stat2 = mc.getRepo2StatList();
		double avg1 = 0;
		double avg2 = 0;
		for (Stat s : stat1) {
			avg1 += s.getWeight();
			//System.out.println("JUNIT W: " + s.getWeight());
		}
		avg1 /= stat1.size();
		for (Stat s : stat2) {
			avg2 += s.getWeight();
			//System.out.println("SPRING W: " + s.getWeight());
		}
		avg2 /= stat2.size();
		System.out.println("JUNIT AVG WEIGHT:  " + avg1);
		System.out.println("SPRING AVG WEIGHT: " + avg2);
	}
}
