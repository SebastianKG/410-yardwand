/*
 * CommitMetricPairingModule - A singleton that does the actual fusion of the analysis
 * components. Part of the UBC CPSC 410 yardwand project.
 * 
 * Author: Eric Furugori
 */

package fusion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Stat;
import analysis.Activity;
import analysis.CollaboratorsActivities;
import analysis.MockCodeDuplicationDetector;
import analysis.NewCollaboratorDetector;

public class CommitMetricPairingModule {
	
	// DUPLICATION_WEIGHT refers to how heavily lines of duplicated code will be weighted
	// in comparison to blocks of code.
	private static final double DUPLICATION_WEIGHT = 0.1;
	
	private static String REPO_URL1;
	private static String REPO_URL2;
	// REPO_SET: a "just in case". We require that setRepositoryURLs is run BEFORE generateStats.
	private static Boolean REPO_SET = false;
	
	private NewCollaboratorDetector ncd1;
	private NewCollaboratorDetector ncd2;
	private MockCodeDuplicationDetector mcdd1;
	private MockCodeDuplicationDetector mcdd2;
	private List<Integer> weeklyCommits1 = new ArrayList<Integer>();
	private List<Integer> weeklyCommits2 = new ArrayList<Integer>();
	private List<Stat> repo1StatList = new ArrayList<Stat>();
	private List<Stat> repo2StatList = new ArrayList<Stat>();
	private SimianOutputParser parser = SimianOutputParser.getInstance();
	
	private CommitMetricPairingModule() {};
	
	private static class Instantiator {
		private static final CommitMetricPairingModule INSTANCE = new CommitMetricPairingModule();
	}
	
	public static CommitMetricPairingModule getInstance() {
		return Instantiator.INSTANCE;
	}
	
	public void setRepositoryURLs(String url1, String url2) {
		REPO_URL1 = url1;
		REPO_URL2 = url2;
		REPO_SET = true;
	}
	
	/**
	 * Reads from the <code>CodeDuplicationDetector</code>s and the <code>
	 * NewCollaboratorDetector</code>s to populate a list of <code>Stat</code>
	 * to be used in the Play Framework.
	 */
	public void generateStats() {
		assert(REPO_SET);
		// debug
		// System.out.println(getNcdURL(REPO_URL1));
		// System.out.println(getNcdURL(REPO_URL2));
		ncd1 = new NewCollaboratorDetector(getNcdURL(REPO_URL1));
		ncd2 = new NewCollaboratorDetector(getNcdURL(REPO_URL2));
		CollaboratorsActivities ca1 = ncd1.getAnalysis();
		CollaboratorsActivities ca2 = ncd2.getAnalysis();
		HashMap<Double, Activity> map1 = ca1.getActivities();
		HashMap<Double, Activity> map2 = ca2.getActivities();
	    Iterator<Entry<Double, Activity>> it1 = map1.entrySet().iterator();
	    Iterator<Entry<Double, Activity>> it2 = map2.entrySet().iterator();
	    
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
        
        // TODO: Ideally, we will pass the REPO_URLs to these classes to receive
        // the simian output. For now we will use hardcoded results.
        mcdd1 = new MockCodeDuplicationDetector();
        mcdd2 = new MockCodeDuplicationDetector();
        int duplicateLineCount1 = 0;
        int duplicateLineCount2 = 0;
        int duplicateBlockCount1 = 0;
        int duplicateBlockCount2 = 0;
        try {
			parser.parse(mcdd1.getDuplicationString(1));
			duplicateLineCount1 = parser.getDuplicateLineCount();
			duplicateBlockCount1 = parser.getDuplicateBlockCount();
			parser.parse(mcdd2.getDuplicationString(2));
			duplicateLineCount2 = parser.getDuplicateLineCount();
			duplicateBlockCount2 = parser.getDuplicateBlockCount();
		} catch (UnexpectedSimianContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // debug
        // System.out.println("b1: " + duplicateBlockCount1);
		// System.out.println("l1: " + duplicateLineCount1);
		// System.out.println("b2: " + duplicateBlockCount2);
		// System.out.println("l2: " + duplicateLineCount2);
        
        double duplications1 = DUPLICATION_WEIGHT * duplicateLineCount1 +
        					   (1 - DUPLICATION_WEIGHT) * duplicateBlockCount1;
        double duplications2 = DUPLICATION_WEIGHT * duplicateLineCount2 +
				   (1 - DUPLICATION_WEIGHT) * duplicateBlockCount2;
        
        int commits1 = getTotalCommits(weeklyCommits1);
        int commits2 = getTotalCommits(weeklyCommits2);
        
        // TODO: for now we will simply populate the stat lists with an average velocity
        // over the entire time frame of the project. So every stat will look the same.
        // We must be able to run simian on __weekly commits__ so that we can find a weekly
        // velocity. So here are our makeshift formulas for velocity.
        // We will ignore the no duplications case for now. The remedy could be as simple as
        // adding a number (probably 1) to the denominator.
        double velocity1 = commits1/duplications1;
        double velocity2 = commits2/duplications2;
        
        for (int i=0; i<weeklyCommits1.size(); i++)
        	repo1StatList.add(new Stat(duplications1, velocity1));
        for (int i=0; i<weeklyCommits2.size(); i++)
        	repo2StatList.add(new Stat(duplications2, velocity2));
        // debug
        //for (Stat s : repo1StatList)
        //	System.out.println("stat: " + s.getWeight() + ", " + s.getStepSpeed());
	}
	
	/**
	 * Get the list of <code>Stat</code> objects containing weights and
	 * step sizes in weekly intervals corresponding to repository 1.
	 * @return
	 */
	public List<Stat> getRepo1StatList() {
		return repo1StatList;
	}
	
	/**
	 * Get the list of <code>Stat</code> objects containing weights and
	 * step sizes in weekly intervals corresponding to repository 2.
	 * @return
	 */
	public List<Stat> getRepo2StatList() {
		return repo2StatList;
	}
	
	/**
	 * Helper function to get the total number of commits given a list of
	 * weekly commits.
	 * @param weeklyCommits
	 * @return
	 */
	private int getTotalCommits(List<Integer> weeklyCommits) {
		int commits = 0;
		for (int c : weeklyCommits) {
			commits += c;
		}
		return commits;
	}
	
	/**
	 * Get a URL in the format that <code>NewCollaboratorDetector</code> expects.
	 * Change a URL like: https://github.com/SebastianKG/410-yardwand
	 * to: https://api.github.com/repos/SebastianKG/410-yardwand/stats/contributors
	 * @param url
	 */
	private String getNcdURL(String url) {
		String[] fragments = url.split("github.com/");
		fragments[0] += "api.";
		fragments[1] = "repos/" + fragments[1];
		return fragments[0] + "github.com/" + fragments[1] + "/stats/contributors";
	}
	
	// TODO: delete this
	// Written for debugging purposes.
	// Also an example of instantiation.
	public static void main(String[] args) {
		CommitMetricPairingModule cmpm = CommitMetricPairingModule.getInstance();
		cmpm.setRepositoryURLs("https://github.com/spring-projects/spring-framework",
							   "https://github.com/junit-team/junit");
		cmpm.generateStats();
		// At this point you can run the getters for stat lists
	}
}
