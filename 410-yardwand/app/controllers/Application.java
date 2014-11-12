package controllers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import models.Stat;
import fusion.MetricComparator;
import fusion.MockCommitMetricPairingModule;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	public static MockCommitMetricPairingModule pairModule;
	
    public static Result index() {
    	String repopath1 = "repopath1.html";
    	String repopath2 = "repopath2.html";
    	
    	/*pairingModule = CommitMetricPairingModule.getInstance();*/
    	
    	// mock object
    	pairModule = MockCommitMetricPairingModule.getInstance();
    	
    	List<Stat> r1StatList = pairModule.getRepo1StatList();
    	List<Stat> r2StatList = pairModule.getRepo2StatList();
    	
    	return ok( 
    			index.render(
    				repopath1, repopath2, 								// paths
					getWeights(r1StatList), getWeights(r2StatList),		// weights
					getSpeeds(r1StatList), getSpeeds(r2StatList)		// step speeds
				) 
    		);
    }
    
    private static List<Double> getWeights( List<Stat> statList ) {
    	List<Double> weights = new LinkedList<Double>();
    	
    	for (Stat stat: statList) {
    		weights.add( stat.weight );
    	}
    	
    	return weights;
    }
    
    private static List<Double> getSpeeds( List<Stat> statList ) {
    	List<Double> speeds = new LinkedList<Double>();
    	
    	for (Stat stat: statList) {
    		speeds.add( stat.stepSpeed );
    	}
    	
    	return speeds;
    }
}
