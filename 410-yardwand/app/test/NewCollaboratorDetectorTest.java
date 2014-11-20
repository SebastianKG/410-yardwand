package test;

import static org.junit.Assert.*;
import models.CollaboratorsActivities;

import org.junit.Test;


import analysis.NewCollaboratorDetector;

public class NewCollaboratorDetectorTest {
	private static final String GOOD_URL = "https://api.github.com/repos/SebastianKG/410-yardwand/stats/contributors";
	private static final String BAD_URL = "XXXXXXXXXXXXXXXX";
	
	@Test
	public void analysisCheck() {
		NewCollaboratorDetector ncd= new NewCollaboratorDetector(GOOD_URL);
		CollaboratorsActivities ca=ncd.getAnalysis();
		assertTrue(ca.getActivities().size()>0);
	}
	
	@Test
	public void noAnalysis(){
		NewCollaboratorDetector ncd= new NewCollaboratorDetector(BAD_URL);
		CollaboratorsActivities ca=ncd.getAnalysis();
		assertEquals(ca.getActivities().size(),0);
	}
}
