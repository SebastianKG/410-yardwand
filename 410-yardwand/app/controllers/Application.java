package controllers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import models.Repository;
import models.Stat;
import fusion.CommitMetricPairingModule;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	public static CommitMetricPairingModule pairingModule;
	
    public static Result index() {
    	String repopath1 = "repopath1.html";
    	String repopath2 = "repopath2.html";
    	
    	pairingModule = new CommitMetricPairingModule("repopath1.html","repopath2.html");
    	
    	/*return ok( index.render(repopath1, pairingModule.repo1.statList, repopath2, pairingModule.repo2.statList) );*/
    	
    	Stat testStat1 = new Stat();
    	Stat testStat2 = new Stat();
    	Stat testStat3 = new Stat();
    	Stat testStat4 = new Stat();
    	
    	testStat1.weight = testStat2.weight = 5;
    	testStat1.stepSpeed = testStat3.stepSpeed = 20;
    	
    	testStat3.weight = testStat4.weight = 10;
    	testStat2.stepSpeed = testStat4.stepSpeed = 10;
    	
    	Repository testRepo1 = new Repository( "P1.path" );
    	testRepo1.statList = new LinkedList<Stat>();
    	testRepo1.statList.addAll( 
    				Arrays.asList(
    					new Stat[] { testStat1, testStat1, testStat2, testStat4 }
    				)  
    			);
    	
    	Repository testRepo2 = new Repository( "P2.path" );
    	testRepo2.statList = new LinkedList<Stat>();
    	testRepo2.statList.addAll( 
    				Arrays.asList(
    					new Stat[] { testStat2, testStat2, testStat1, testStat3 }
    				)
    			);
    	
    	return ok( 
    			index.render(
					testRepo1.path, testRepo2.path, 									// paths
					getWeights(testRepo1.statList), getWeights(testRepo2.statList),		// weights
					getSpeeds(testRepo1.statList), getSpeeds(testRepo2.statList)		// step speeds
				) 
    		);

    }
    
    private static List<Integer> getWeights( List<Stat> statList ) {
    	List<Integer> weights = new LinkedList<Integer>();
    	
    	for (Stat stat: statList) {
    		weights.add( stat.weight );
    	}
    	
    	return weights;
    }
    
    private static List<Integer> getSpeeds( List<Stat> statList ) {
    	List<Integer> speeds = new LinkedList<Integer>();
    	
    	for (Stat stat: statList) {
    		speeds.add( stat.stepSpeed );
    	}
    	
    	return speeds;
    }
}
