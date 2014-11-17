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
	
	/**
	 * Run the metric comparator.
	 */
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
			//System.out.println(s.getWeight() + " " + s.getStepSpeed()
			//		+ " " + s.getCollaboration());
			avg2 += s.getWeight();
			//System.out.println("SPRING W: " + s.getWeight());
		}
		avg2 /= stat2.size();
		//System.out.println("JUNIT AVG WEIGHT:  " + avg1);
		//System.out.println("SPRING AVG WEIGHT: " + avg2);
	}
}