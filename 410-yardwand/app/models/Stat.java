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
	
	public Stat() {}
	
	public Stat(Double weight, Double stepSpeed) {
		this.weight = weight;
		this.stepSpeed = stepSpeed;
	}
	
	/**
	 * Set the weight.
	 * @param weight
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	/**
	 * Set the step speed.
	 * @param stepSpeed
	 */
	public void setStepSpeed(Double stepSpeed) {
		this.stepSpeed = stepSpeed;
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
}
