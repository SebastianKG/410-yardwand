package models;

public class Stat {
	/*
	 * NOTE: it seems that Play Framework will automatically translate
	 * private with getters/setters.
	 * 
	 * I do not feel comfortable with accessing fields directly, so I
	 * wrote getters and setters but kept the fields public so as to not
	 * break other code.
	 */
	public Double weight;
	public Double stepSpeed;
	public Double collaboration;
	
	public Stat() {}
	
	public Stat(Double weight, Double stepSpeed, Double collaboration) {
		this.weight = weight;
		this.stepSpeed = stepSpeed;
		this.collaboration = collaboration;
	}
	
	/**
	 * Get the weight.
	 * @return weight
	 * 		The weight.
	 */
	public Double getWeight() {
		return weight;
	}
	
	/**
	 * Get the step speed.
	 * @return stepSpeed
	 * 		The step speed.
	 */
	public Double getStepSpeed() {
		return stepSpeed;
	}
	
	/**
	 * Get the collaboration metric. See <code>StatListBuilder</code>
	 * for how this is calculated.
	 * @return collaboration
	 * 		A measurement of density of recent commit activity.
	 * 
	 */
	public Double getCollaboration() {
		return collaboration;
	}
	
	/**
	 * Print out the stat fields to the console.
	 */
	public void print() {
		System.out.println("weight: " + weight + ", speed: " + stepSpeed
			+ ", collab: " + collaboration);
	}
}
