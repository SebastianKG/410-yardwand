package test;

import static org.junit.Assert.*;
import models.CollaboratorsActivities;

import org.junit.Test;


import analysis.NewCollaboratorDetector;

public class NewCollaboratorDetectorTest {
	private static final String GOOD_URL = "https://github.com/SebastianKG/410-yardwand";
	private static final String BAD_URL = "XXXXXXXXXXXXXXXX";
	
	@Test(expected=NullPointerException.class)
	public void analysisCheck() {
		NewCollaboratorDetector ncd= new NewCollaboratorDetector(GOOD_URL);
		CollaboratorsActivities ca=ncd.getAnalysis();
		assertTrue(ca.getActivities().size()>0);
	}
	
	@Test
	public void noAnalysis(){
		NewCollaboratorDetector ncd= new NewCollaboratorDetector(BAD_URL);
		CollaboratorsActivities ca=ncd.getAnalysis();
		assertEquals(ca.getActivities(),null); 
	}
}
