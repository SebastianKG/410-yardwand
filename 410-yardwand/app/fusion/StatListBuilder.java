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
	 * A DECAY of 0 corresponds to looking at all commits so far equally.
	 */
	private static final double DECAY = 0.420;
	
	private List<Stat> stats = new ArrayList<Stat>();
	private SimianOutputParser parser = SimianOutputParser.getInstance();
	private List<Integer> weeklyCommits;
	
	/**
	 * Calculates <code>Stat</code>s from the outputs of analysis tools.
	 * Call this constructor when you only have a single simian output to
	 * create stats from. Weight is then given as an average and is constant
	 * throughout the stat list.
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
	 * Calculates <code>Stat</code>s from the outputs of analysis tools.
	 * Call this constructor when you have a list of simian outputs.
	 * REQUIRES: expects weeks where the data has yet to change to be
	 * empty strings - will interpret empty strings as "no change".
	 * @param duplicationStrings
	 * @param weeklyCommits
	 */
	public StatListBuilder(List<String> duplicationStrings, List<Integer> weeklyCommits) {
		this.weeklyCommits = weeklyCommits;
		
		List<Integer> weeklyLineDuplications = new ArrayList<Integer>();
		List<Integer> weeklyBlockDuplications = new ArrayList<Integer>();
		List<Double> weeklyBloat = new ArrayList<Double>();
		
		for (int i = 0; i<weeklyCommits.size(); i++) {
			weeklyLineDuplications.add(0);
			weeklyBlockDuplications.add(0);
		}
		
		for (int i = 0; i<duplicationStrings.size(); i++) {
			weeklyLineDuplications.remove(i);
			weeklyBlockDuplications.remove(i);
			if (!duplicationStrings.get(i).equals("")) {
				try {
					parser.parse(duplicationStrings.get(i));
					weeklyLineDuplications.add(i, parser.getDuplicateLineCount());
					weeklyBlockDuplications.add(i, parser.getDuplicateBlockCount());
				} catch (UnexpectedSimianContentException e) {
					System.err.println("In StatBuilder: " + e.getMessage());
				}
			} else {
				if (i > 0) {
					weeklyLineDuplications.add(i, weeklyLineDuplications.get(i-1));
					weeklyBlockDuplications.add(i, weeklyBlockDuplications.get(i-1));
				} else {
					weeklyLineDuplications.add(0);
					weeklyBlockDuplications.add(0);
				}
			}
		}
		System.out.println("SIZE: " + weeklyLineDuplications.size() + ", "
				+ weeklyCommits.size() + ", " + duplicationStrings.size());
		
		// TODO: Generate weekly bloat, velocity, collaboration, possibly pass version to stat
		for (int i=0; i<weeklyLineDuplications.size(); i++) {
			weeklyBloat.add(getBloat(weeklyLineDuplications.get(i),
					weeklyBlockDuplications.get(i)));
		}
		
		List<Double> collaboration = getCollaboration();
		List<Double> velocities = getVelocities(weeklyBloat, getTotalCommits(), collaboration);
		for (int i=0; i<weeklyCommits.size(); i++) {
			if (weeklyBloat.get(i) > 0) {
				stats.add(new Stat(weeklyBloat.get(i), velocities.get(i), collaboration.get(i)));
			}
		}
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
		for (int i=0; i<velocities.size(); i++) {
			stats.add(new Stat(bloat, velocities.get(i), collaboration.get(i)));
		}
	}
	
	/**
	 * A helper routine that calculates a velocity given code bloat,
	 * total commit count, and calculated collaboration measurement.
	 * FORMULA IS:
	 * total_commits / (1 + bloat_metric_value) + collaboration_metric_value
	 * @param bloat
	 * @param commits
	 * @param collaboration
	 * @return the weekly velocities.
	 */
	private List<Double> getVelocities(double bloat, int commits,
			List<Double> collaboration) {
		List<Double> velocities = new ArrayList<Double>();
		double vbase = commits/(1+bloat);
		for (int i=0; i<weeklyCommits.size(); i++) {
			velocities.add(vbase + collaboration.get(i));
		}
		return velocities;
	}
	
	private List<Double> getVelocities(List<Double> bloat, int commits,
				List<Double> collaboration) {
		List<Double> velocities = new ArrayList<Double>();
		for (int i=0; i<bloat.size(); i++) {
			velocities.add(commits/(1+bloat.get(i)) + collaboration.get(i));
		}
		return velocities;
	}

	/**
	 * A helper method that gets the total number of commits made.
	 * @return the total number of commits.
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
	 * Older values "decay" over time by a value of DECAY. Essentially we are
	 * looking at the most recent commit count + deviation.
	 * @return collaborators
	 * 		An <code>ArrayList</code> of <code>Double</code> containing the
	 * 		calculated collaboration measurements. Has size of number of weeks.
	 */
	private List<Double> getCollaboration() {
		List<Double> collaboration = new ArrayList<Double>();
		
		for (int i = 0; i < weeklyCommits.size(); i++) {
			double cdev = 0;
			int counter = 1;
			for (int j = i; j > -1 ; j--) {
				cdev += weeklyCommits.get(j)*Math.pow(DECAY, counter);
				counter++;
			}
			collaboration.add(cdev);
		}
		
		return collaboration;
	}

	/**
	 * Helper function that contains the definition of bloat.
	 * @return a value representing bloat.
	 */
	private double getBloat() {
		return DUPL_WEIGHT * parser.getDuplicateLineCount() +
			   (1 - DUPL_WEIGHT) * parser.getDuplicateBlockCount();
	}
	
	private double getBloat(int duplicatedLineCount, int duplicatedBlockCount) {
		return DUPL_WEIGHT * duplicatedLineCount +
				   (1 - DUPL_WEIGHT) * duplicatedBlockCount;
	}
}
