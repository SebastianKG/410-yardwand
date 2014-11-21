/*
 * MetricComparator - Does the actual fusion of the analysis
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

import models.Activity;
import models.Repository;
import models.Stat;
import models.CollaboratorsActivities;
import analysis.CodeDuplicationDetector;
import analysis.NewCollaboratorDetector;

public class MetricComparator {
	
	private Repository repo1;
	private Repository repo2;
	private List<Stat> repo1StatList;
	private List<Stat> repo2StatList;
	
	/**
	 * Generate velocities and weights (bloat) given two repositories.
	 * MetricComparator sends requests to the analysis Detectors and
	 * fuses them into two <code>Stat</code> lists.
	 * @param repo1
	 * 		A <code>Repository</code> to be compared against repo2.
	 * @param repo2
	 * 		A <code>Repository</code> to be compared against repo1.
	 */
	public MetricComparator(Repository repo1, Repository repo2) {
		this.repo1 = repo1;
		this.repo2 = repo2;
		generateStats();
	}
	
	/**
	 * Reads from the <code>CodeDuplicationDetector</code>s and the <code>
	 * NewCollaboratorDetector</code>s to populate two lists of <code>Stat</code>
	 * to be used in the Play Framework.
	 * 
	 * This routine is called upon the creation of the object.
	 */
	private void generateStats() {
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
	    List<Double> commitWeeks1 = new ArrayList<Double>();
	    List<Double> commitWeeks2 = new ArrayList<Double>();
	    
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
        
        commitWeeks1.addAll(map1.keySet());
        commitWeeks2.addAll(map2.keySet());
        
        // TODO: Ideally, we will pass the Repository objects to these classes to receive
        // the simian output. For now we will use hardcoded results.
        //MockCodeDuplicationDetector mcdd1 = new MockCodeDuplicationDetector();
        //MockCodeDuplicationDetector mcdd2 = new MockCodeDuplicationDetector();
        CodeDuplicationDetector cdd = new CodeDuplicationDetector();
        HashMap<Double, String> duplicationMap1 = cdd.getRepo1DuplicationAnalysis();
        HashMap<Double, String> duplicationMap2 = cdd.getRepo2DuplicationAnalysis();
        
        List<String> duplicationStrings1 = buildDuplicationList(duplicationMap1, commitWeeks1);
        List<String> duplicationStrings2 = buildDuplicationList(duplicationMap2, commitWeeks2);
  
        StatListBuilder slb1 = new StatListBuilder(duplicationStrings1, weeklyCommits1);
        StatListBuilder slb2 = new StatListBuilder(duplicationStrings2, weeklyCommits2);
        repo1StatList = slb1.getStats();
        repo2StatList = slb2.getStats();
	}
	
	public List<String> buildDuplicationList(
			HashMap<Double, String> duplicationMap, List<Double> commitWeeks) {
		List<String> duplicationStrings = new ArrayList<String>();
		double key;
		for (double w : commitWeeks) {
			if (duplicationMap.keySet().iterator().hasNext()) {
				if ((key = duplicationMap.keySet().iterator().next()) < w) {
					duplicationStrings.add(duplicationMap.get(key));
					//System.out.println("key: " + key);
					duplicationMap.remove(key);
				} else {
					duplicationStrings.add("");
				}
			}
		}
		//for (int i=0; i<commitWeeks.size(); i++ ) {
		//	System.out.println(!duplicationStrings.get(i).equals("") + ", " + commitWeeks.get(i));
		//}
		return duplicationStrings;
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
}
