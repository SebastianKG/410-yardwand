package fusion;

import java.util.ArrayList;
import java.util.List;

import models.Stat;

public class StatListBuilder {
	
	/*
	 * DUPL_WEIGHT determines how much we weight line duplications in
	 * comparison to block duplications.
	 * A DUPL_WEIGHT of 1 corresponds to full weight in line duplications.
	 * A DUPL_WEIGHT of 0 corresponds to full weight in block duplications.
	 */
	private static final double DUPL_WEIGHT = 0.1;
	
	/*
	 * DECAY determines how quickly we "forget" about older commit information.
	 * A DECAY of 1 corresponds to only looking at the weekly commits.
	 * A DECAY of 0 corresponds to looking at all commits so far.
	 */
	private static final double DECAY = 0.25;
	
	private List<Stat> stats = new ArrayList<Stat>();
	private SimianOutputParser parser = SimianOutputParser.getInstance();
	private List<Integer> weeklyCommits;
	
	/**
	 * Calculates <code>Stat</code>s from the outputs of analysis tools.
	 * @param simianOutput
	 * 		The plaintext output to a simian analysis in <code>String</code> form.
	 * @param weeklyCommits
	 * 		A <code>List</code> containing the amount of commits per week.
	 */
	public StatListBuilder(String simianOutput, List<Integer> weeklyCommits) {
		try {
			parser.parse(simianOutput);
		} catch (UnexpectedSimianContentException e) {
			System.err.println("In StatBuilder: " + e.getMessage());
		}
		this.weeklyCommits = weeklyCommits;
		build();
	}
	
	/**
	 * Get the list of <code>Stat</code>.
	 * @return
	 */
	public List<Stat> getStats() {
		return stats;
	}
	
	/**
	 * A helper routine that is called when the class is constructed.
	 * Builds the stat lists.
	 */
	private void build() {
		double bloat = getBloat();
		int commits = getTotalCommits();
		List<Double> collaboration = getCollaboration();
		List<Double> velocities = getVelocities(bloat, commits, collaboration);
		for (double v : velocities) {
			stats.add(new Stat(bloat, v));
		}
	}
	
	/**
	 * A helper routine that calculates a velocity given code bloat,
	 * total commit count, and calculated collaboration measurement.
	 * @param bloat
	 * @param commits
	 * @param collaboration
	 * @return
	 */
	private List<Double> getVelocities(double bloat, int commits,
			List<Double> collaboration) {
		List<Double> velocities = new ArrayList<Double>();
		double vbase = bloat/commits;
		for (double c : collaboration) {
			velocities.add(vbase + c);
		}
		return velocities;
	}

	/**
	 * A helper method that gets the total number of commits made.
	 * @return
	 */
	private int getTotalCommits() {
		int commits = 0;
		for (int c : weeklyCommits) {
			commits += c;
		}
		return commits;
	}

	/**
	 * Calculate a collaboration measurement based on how recently
	 * a repository has been committed to and how many commits were performed.
	 * TODO: test this.
	 * TODO: may want to look at weekly line additions and deletions.
	 * @return collaborators
	 * 		An <code>ArrayList</code> of <code>Double</code> containing the
	 * 		calculated collaboration measurements. Has size of number of weeks.
	 */
	private List<Double> getCollaboration() {
		List<Double> collaboration = new ArrayList<Double>();
		List<Double> wc = new ArrayList<Double>();
		
		for (int c : weeklyCommits)
			wc.add((double) c);
		
		for (int i = 0; i < weeklyCommits.size(); i++) {
			double cdev = 0;
			for (int j = i-1; i > -1 ; i--) {
				cdev += wc.get(j)*DECAY;
				wc.add(j, wc.get(j)*DECAY);
			}
			collaboration.add(wc.get(i) + 4*cdev);
		}
		
		return collaboration;
	}

	/**
	 * Helper function that contains the definition of bloat.
	 * @return
	 */
	private double getBloat() {
		return DUPL_WEIGHT * parser.getDuplicateLineCount() +
			   (1 - DUPL_WEIGHT) * parser.getDuplicateBlockCount();
	}
}
