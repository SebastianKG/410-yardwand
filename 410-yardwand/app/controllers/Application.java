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
    	String r1name = "JUnit";
    	String r2name = "Spring Framework";
    	
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
    				r1name, r2name, 															// paths
    				r1weightStates, r2weightStates,												// weights
					getRoundedSpeeds(r1StatList), getRoundedSpeeds(r2StatList),					// step speeds
					getIntegerCollaborations(r1StatList), getIntegerCollaborations(r2StatList) 	// collaboration quantities
				) 
    		);
    }
    
    public static List<Integer> statify(List<Double> weights, double mean, double stdDev) {
    	List<Integer> stateList = new LinkedList<Integer>();
    	
    	double stateOneLine = stdDev;
		double stateTwoLine = mean + stdDev;
		
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
    
    public static List<Integer> getIntegerCollaborations( List<Stat> statList ) {
    	List<Integer> collabs = new LinkedList<Integer>();
    	
    	for (Stat stat: statList) {
    		Double floorDoubleValue = new Double(Math.floor(stat.weight));
    		collabs.add( floorDoubleValue.intValue() );
    	}
    	
    	return collabs;
    }
    
    public static List<Double> getWeights( List<Stat> statList ) {
    	List<Double> weights = new LinkedList<Double>();
    	
    	for (Stat stat: statList) {
    		weights.add( stat.weight );
    	}
    	
    	return weights;
    }
    
    public static List<Double> getRoundedSpeeds( List<Stat> statList ) {
    	List<Double> speeds = new LinkedList<Double>();
    	
    	for (Stat stat: statList) {
    		Double speed = stat.stepSpeed;
    		Double roundedspeed = new Double((double) Math.round(speed * 10) / 10);
    		speeds.add( roundedspeed );
    	}
    	
    	return speeds;
    }
}
