package controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import models.Repository;
import models.Stat;
import fusion.InvalidRepositoryURLException;
import fusion.MetricComparator;
import play.*;
import play.mvc.*;
import views.html.*;



public class Application extends Controller {
	
	/*
	 * NOTE: The Play Framework will automatically translate public variables
	 * to private variables with getters/setters.
	 * 
	 * I have taken advantage of this feature and used it throughout this class,
	 * note that this is not bad code and is in fact following the best practices
	 * outlined by the Play Framework's documentation.
	 */
	/*public static MockCommitMetricPairingModule pairModule;*/
	public static MetricComparator pairModule;
	
	/**
	 * Returns the weights, collaboration metrics, and speeds for two repositories,
	 * currently defined to be JUnit and Spring Framework, whenever the "/" route is
	 * visited in the browser. Functions as a "main" function.
	 */
    public static Result index() {
    	String r1name = "JUnit";
    	String r2name = "Spring Framework";
    	
    	Repository r1;
    	Repository r2;
    	
    	try {
    		r1 = new Repository("https://github.com/junit-team/junit");
    		r2 = new Repository("https://github.com/spring-projects/spring-framework");
    	} catch (InvalidRepositoryURLException e) {
    		return ok("<h1>Invalid URLs</h1>");
    	}
    	
    	pairModule = new MetricComparator(r1,r2);
    	
    	List<Stat> r1StatList = pairModule.getRepo1StatList();
    	List<Stat> r2StatList = pairModule.getRepo2StatList();
    	
    	System.out.println(r1StatList.size());
    	System.out.println(r2StatList.size());
    	
    	trim(r1StatList, Math.min(r1StatList.size(), r2StatList.size()));
    	
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
					getNormalizedAndRoundedSpeeds(r1StatList), getNormalizedAndRoundedSpeeds(r2StatList),					// step speeds
					getIntegerCollaborations(r1StatList), getIntegerCollaborations(r2StatList) 	// collaboration quantities
				) 
    		);
    }
    
    /**
     * Trims a list to size n if the list has more than n entries.
     * @param list
     * 			The list in need of trimming.
     * @param n
     * 			The size to trim to, if the list is larger.
     */
    private static void trim( List<?> list, int n ) {
    	while (list.size() > 92) {
    		list.remove(list.size() - 1);
    	}
    }
    
    /**
     * Put all weights which are above mean 
     * but below mean + stdDev into one state (1), 
     * all weights which are above  mean + stdDev in to state (2),
     * and all other weights into state (0)
     * @param weights
     * 		A list of doubles representing weights
     * @param mean
     * 		The mean of weights
     * @param stdDev
     * 		The standard deviation of weights
     * @return
     * 		The list of weights in state form.
     */
    private static List<Integer> statify(List<Double> weights, double mean, double stdDev) {
    	List<Integer> stateList = new LinkedList<Integer>();
    	
    	double stateOneLine = mean;
		double stateTwoLine = mean + stdDev/2;
		
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
    
    /**
     * Convert a list of Double into a list of double
     * @param objectDoubles
     * 		A list of (Object) <code>Double</code>s
     * @return
     * 		A list of (primitive) <code>double</code>s
     */
    private static double[] toPrimitive(Double[] objectDoubles) {
    	double[] primDoubles = new double[objectDoubles.length];
    	for(int i = 0; i < objectDoubles.length; i++) {
    		primDoubles[i] = objectDoubles[i].doubleValue();
    	}
    	
    	return primDoubles;
    }
    
    /**
     * Get the collaboration parameters for a list of <code>Stat</code>
     * in Integer form.
     * @param statList
     * 		A list of <code>Stat</code>, which each contain a 
     * 		collaboration parameter in <code>double</code> form
     * @return
     * 		A list of collaboration information in <code>Integer</code> form
     */
    private static List<Integer> getIntegerCollaborations( List<Stat> statList ) {
    	List<Integer> collabs = new LinkedList<Integer>();
    	
    	for (Stat stat: statList) {
    		Double floorDoubleValue = new Double(Math.floor(stat.collaboration));
    		collabs.add( floorDoubleValue.intValue() );
    	}
    	
    	return collabs;
    }
    
    /**
     * Get the weight parameters for all <code>Stat</code> objects
     * in a list of <code>Stat</code>.
     * @param statList
     * 		A list of <code>Stat</code>, which each contain a 
     * 		weight parameter.
     * @return
     * 		The list of the weight parameters contained in the 
     * 		statList
     */
    private static List<Double> getWeights( List<Stat> statList ) {
    	List<Double> weights = new LinkedList<Double>();
    	
    	for (Stat stat: statList) {
    		weights.add( stat.weight );
    	}
    	
    	return weights;
    }
    
    /**
     * Get the normalized and rounded speed parameters contained
     * within a list of <code>Stat</code>.
     * @param statList
     * 		A list of <code>Stat</code> which contain speeds as <code>double</code>s.
     * @return
     * 		A list of normalized and rounded down speeds in <code>Double</code> form.
     */
    private static List<Double> getNormalizedAndRoundedSpeeds( List<Stat> statList ) {
    	List<Double> speeds = new LinkedList<Double>();
    	double normalization = 15;
    	
    	for (Stat stat: statList) {
    		Double speed = stat.stepSpeed;
    		Double roundedspeed = new Double((double) Math.round((speed/normalization) * 10) / 10);
    		speeds.add( roundedspeed );
    	}
    	
    	return speeds;
    }
    
    // delete this
    private static void printList(List<?> mylist) {
    	for(int i = 0; i < mylist.size(); i += 1) {
    		System.out.println(mylist.get(i));
    	}
    }
}
