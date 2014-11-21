package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;

import models.Activity;
import models.CollaboratorsActivities;
import models.Stat;
import analysis.NewCollaboratorDetector;
import fusion.InvalidRepositoryURLException;
import models.Repository;
import fusion.StatListBuilder;

public class StatListBuilderTest {
	
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

	StatListBuilder slb1;
	StatListBuilder slb2;
	
	@Before
	public void testListBuilder() throws InvalidRepositoryURLException {
		Repository repo1 = new Repository(URL1);
		Repository repo2 = new Repository(URL2);
		
		NewCollaboratorDetector ncd1 = new NewCollaboratorDetector(repo1.getContributorURL());
		NewCollaboratorDetector ncd2 = new NewCollaboratorDetector(repo2.getContributorURL());
		
		CollaboratorsActivities analysis1 = ncd1.getAnalysis();
		CollaboratorsActivities analysis2 = ncd2.getAnalysis();
		
		HashMap<Double, Activity> map1 = analysis1.getActivities();
		HashMap<Double, Activity> map2 = analysis2.getActivities();
	    Iterator<Entry<Double, Activity>> it1 = map1.entrySet().iterator();
	    Iterator<Entry<Double, Activity>> it2 = map2.entrySet().iterator();
	    
	    List<Integer> weeklyCommits1 = new ArrayList<Integer>();
	    List<Integer> weeklyCommits2 = new ArrayList<Integer>();
	    
        while (it1.hasNext()) {
        	@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it1.next();
        	Activity a = (Activity) pairs.getValue();
        	weeklyCommits1.add(a.getCommits());
        }
        
        while (it2.hasNext()) {
        	@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it2.next();
        	Activity a = (Activity) pairs.getValue();
        	weeklyCommits2.add(a.getCommits());
        }
        
        List<String> duplicationStrings1 = new ArrayList<String>();
        List<String> duplicationStrings2 = new ArrayList<String>();
        for (int i=0; i<weeklyCommits1.size(); i++) {
        	duplicationStrings1.add("");
        }
        duplicationStrings1.remove(0);
        duplicationStrings1.add(SAMPLE_OUTPUT1);
        
        for (int i=0; i<weeklyCommits1.size(); i++) {
        	duplicationStrings2.add("");
        }
        duplicationStrings2.remove(0);
        duplicationStrings2.add(SAMPLE_OUTPUT2);
        
  
        slb1 = new StatListBuilder(duplicationStrings1, weeklyCommits1);
        slb2 = new StatListBuilder(duplicationStrings2, weeklyCommits1);
        
        System.out.println("REPO1 STATS:");
        for (Stat s : slb1.getStats()) {
        	s.print();
        }
        
        System.out.println("REPO2 STATS:");
        for (Stat s : slb2.getStats()) {
        	s.print();
        }
	}
}
