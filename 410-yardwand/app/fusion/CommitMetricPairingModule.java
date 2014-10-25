/*
 * CommitMetricPairingModule - A singleton that does the actual fusion of the analysis
 * components. Part of the UBC CPSC 410 yardwand project.
 * 
 * Author: Eric Furugori
 */

package fusion;

public class CommitMetricPairingModule {
	
	private CommitMetricPairingModule() {}
	
	private static class Instantiator {
		private static final CommitMetricPairingModule INSTANCE = new CommitMetricPairingModule();
	}
	
	public static CommitMetricPairingModule getInstance() {
		return Instantiator.INSTANCE;
	}
	
	public static void compare() {
		
	}

}
