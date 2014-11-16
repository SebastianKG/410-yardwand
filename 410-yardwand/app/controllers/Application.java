package controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import models.Stat;
import fusion.MetricComparator;
import fusion.MockCommitMetricPairingModule;
import play.*;
import play.mvc.*;
import views.html.*;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.StatUtils;

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
    	
    	List<Double> combinedWeights = new LinkedList<Double>();
    	combinedWeights.addAll(getWeights(r1StatList));
    	combinedWeights.addAll(getWeights(r2StatList));
    	
    	StandardDeviation stdDevGen = new StandardDeviation();
    	double[] primWeightArr = toPrimitive(combinedWeights.toArray(new Double[combinedWeights.size()]));
    	double stdDev = stdDevGen.evaluate(primWeightArr);
    	double mean = StatUtils.mean(primWeightArr);
    	
    	List<Integer> r1weightStates = statify(getWeights(r1StatList),mean,stdDev);
    	List<Integer> r2weightStates = statify(getWeights(r2StatList),mean,stdDev);
    	
    	return ok( 
    			index.render(
    				repopath1, repopath2, 								// paths
    				r1weightStates, r2weightStates,		// weights
					getSpeeds(r1StatList), getSpeeds(r2StatList)		// step speeds
				) 
    		);
    }
    
    public static List<Integer> statify(List<Double> weights, double mean, double stdDev) {
    	List<Integer> stateList = new LinkedList<Integer>();
    	
    	double stateOneLine = mean + stdDev;
		double stateTwoLine = mean + stdDev * 2;
		
    	for(Double weight : weights) {
    		if (weight.doubleValue() < stateOneLine) {
    			stateList.add(0);
    		} else if (weight.doubleValue() < stateTwoLine) {
    			stateList.add(1);
    		} else {
    			stateList.add(2);
    		}
    	}
    	
    	return stateList;
    }
    
    public static double[] toPrimitive(Double[] objectDoubles) {
    	double[] primDoubles = new double[objectDoubles.length];
    	for(int i = 0; i < objectDoubles.length; i++) {
    		primDoubles[i] = objectDoubles[i].doubleValue();
    	}
    	
    	return primDoubles;
    }
    
    public static List<Double> getWeights( List<Stat> statList ) {
    	List<Double> weights = new LinkedList<Double>();
    	
    	// update for int values
    	
    	for (Stat stat: statList) {
    		weights.add( stat.weight );
    	}
    	
    	return weights;
    }
    
    public static List<Double> getSpeeds( List<Stat> statList ) {
    	List<Double> speeds = new LinkedList<Double>();
    	
    	for (Stat stat: statList) {
    		speeds.add( stat.stepSpeed );
    	}
    	
    	return speeds;
    }
}
