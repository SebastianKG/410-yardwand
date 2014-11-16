/*
 * CommitMetricPairingModule - A singleton that does the actual fusion of the analysis
 * components. Part of the UBC CPSC 410 yardwand project.
 * 
 * Author: Eric Furugori
 */

package fusion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Stat;
import analysis.Activity;
import analysis.CollaboratorsActivities;
import analysis.NewCollaboratorDetector;

public class MockCommitMetricPairingModule {
	
	private static NewCollaboratorDetector ncd1;
	private static NewCollaboratorDetector ncd2;
	
	private MockCommitMetricPairingModule() {}
	
	private static class Instantiator {
		private static final MockCommitMetricPairingModule INSTANCE = new MockCommitMetricPairingModule();
	}
	
	public static MockCommitMetricPairingModule getInstance() {
		return Instantiator.INSTANCE;
	}
	
	public List<Stat> getRepo1StatList() {
		Stat testStat1 = new Stat();
    	Stat testStat2 = new Stat();
    	Stat testStat3 = new Stat();
    	Stat testStat4 = new Stat();
    	
    	testStat1.weight = 30.;
    	testStat2.weight = 599.;
    	testStat3.weight = 600.;
    	testStat4.weight = 10.;
    	
    	testStat1.stepSpeed = 0.2;
    	testStat2.stepSpeed = 0.3;
    	testStat3.stepSpeed = 0.45;
    	testStat4.stepSpeed = 0.6;
    	
    	List<Stat> statList1 = new LinkedList<Stat>();
    	statList1.addAll( 
			Arrays.asList(
				new Stat[] { testStat1, testStat2, testStat3, testStat4 }
			)  
		);
    	
    	return statList1;
	}
	
	public List<Stat> getRepo2StatList() {
		Stat testStat1 = new Stat();
    	Stat testStat2 = new Stat();
    	Stat testStat3 = new Stat();
    	Stat testStat4 = new Stat();
    	
    	testStat1.weight = 10.;
    	testStat2.weight = 15.;
    	testStat3.weight = 600.0;
    	testStat4.weight = 10.;
    	
    	testStat1.stepSpeed = 0.2;
    	testStat2.stepSpeed = 0.3;
    	testStat3.stepSpeed = 0.45;
    	testStat4.stepSpeed = 0.6;
		
    	List<Stat> statList2 = new LinkedList<Stat>();
    	statList2.addAll( 
			Arrays.asList(
				new Stat[] { testStat1, testStat2, testStat3, testStat4 }
			)
		);
		
		return statList2;
	}
}
