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
import java.util.Map;
import java.util.Map.Entry;

import analysis.Activity;
import analysis.CollaboratorsActivities;
import analysis.NewCollaboratorDetector;

public class CommitMetricPairingModule {
	
	private static NewCollaboratorDetector ncd1;
	private static NewCollaboratorDetector ncd2;
	private ArrayList<Activity> weeklyActivity1;
	private ArrayList<Activity> weeklyActivity2;
	
	private CommitMetricPairingModule() {}
	
	private static class Instantiator {
		private static final CommitMetricPairingModule INSTANCE = new CommitMetricPairingModule();
	}
	
	public static CommitMetricPairingModule getInstance() {
		return Instantiator.INSTANCE;
	}
	
	public void compare(String url1, String url2) {
		ncd1 = new NewCollaboratorDetector(url1);
		ncd2 = new NewCollaboratorDetector(url2);
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
        	weeklyActivity1.add(a);
        	//System.out.println(pairs.getKey().toString() + " " + 
        	//		a.getAddition() + " " + a.getDeletion() + " " + a.getCommits());
        }
        
        while (it2.hasNext()) {
        	@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it2.next();
        	Activity a = (Activity) pairs.getValue();
        	weeklyActivity2.add(a);
        	//System.out.println(pairs.getKey().toString() + " " + 
        	//		a.getAddition() + " " + a.getDeletion() + " " + a.getCommits());
        }
	}
	
	// TODO: delete this
	// Written for debugging purposes.
	public static void main(String[] args) {
		CommitMetricPairingModule cmpm = CommitMetricPairingModule.getInstance();
		cmpm.compare("https://api.github.com/repos/zxing/zxing/stats/contributors",
				"https://api.github.com/repos/SebastianKG/410-yardwand/stats/contributors");
	}
}
