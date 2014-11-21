package analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CodeDuplicationDetector {

	private static final String repo1 = "JUnit";
	private static final String repo2 = "Spring";
	private LinkedHashMap<String, ArrayList<Double>> repoMap;

	public CodeDuplicationDetector() {
		repoMap = new LinkedHashMap<String, ArrayList<Double>>();
		ArrayList<Double> repo1ListDates = new ArrayList<Double>();
		ArrayList<Double> repo2ListDates = new ArrayList<Double>();
		// REFERENCE DATES (JUnit Code Base): use these dates as a reference to
		// corresponding weeks in fusion module
		repo1ListDates.add((double) 1239584400);
		repo1ListDates.add((double) 1248656400);
		repo1ListDates.add((double) 1256605200);
		repo1ListDates.add((double) 1255654800);
		repo1ListDates.add((double) 1256864400);
		repo1ListDates.add((double) 1260234000);
		repo1ListDates.add((double) 1258351200);
		repo1ListDates.add((double) 1239152400);
		repo1ListDates.add((double) 1294016400);
		repo1ListDates.add((double) 1309914000);
		repo1ListDates.add((double) 1313110800);
		repo1ListDates.add((double) 1313974800);
		repo1ListDates.add((double) 1317258000);
		repo1ListDates.add((double) 1350349200);
		repo1ListDates.add((double) 1352854800);
		repo1ListDates.add((double) 1406422800);
		repo1ListDates.add((double) 1411520400);
		repo1ListDates.add((double) 1410397200);

		repoMap.put(repo1, repo1ListDates);

		repo2ListDates.add((double) 1229648400);
		repo2ListDates.add((double) 1249866000);
		repo2ListDates.add((double) 1260925200);
		repo2ListDates.add((double) 1266541200);
		repo2ListDates.add((double) 1287536400);
		repo2ListDates.add((double) 1313629200);
		repo2ListDates.add((double) 1323738000);
		repo2ListDates.add((double) 1341622800);
		repo2ListDates.add((double) 1351990800);
		repo2ListDates.add((double) 1358902800);
		repo2ListDates.add((double) 1368666000);
		repo2ListDates.add((double) 1377738000);
		repo2ListDates.add((double) 1386723600);
		repo2ListDates.add((double) 1392685200);
		repo2ListDates.add((double) 1400547600);
		repo2ListDates.add((double) 1405645200);
		repo2ListDates.add((double) 1409792400);
		repo2ListDates.add((double) 1415581200);

		repoMap.put(repo2, repo2ListDates);

	}
	
	/**
	 * Runs Simian on repo1 versions (JUnit) and returns a <code>HashMap</code>
	 * of <code>Double</code> and <code>String</code> that contains the date in
	 * weeks, and the corresponding Simian output as a string on the version released
	 * on that week.
	 */
	public HashMap<Double, String> getRepo1DuplicationAnalysis() {
		return analyzeRepository(1);
	}
	
	/**
	 * Runs Simian on repo2 versions (Spring) and returns a <code>HashMap</code>
	 * of <code>Double</code> and <code>String</code> that contains the date in
	 * weeks, and the corresponding Simian output as a string on the version released
	 * on that week.
	 */
	public HashMap<Double, String> getRepo2DuplicationAnalysis() {
		return analyzeRepository(2);
	}
	
	private HashMap<Double, String> analyzeRepository(int i) {
		String[] commands = new String[5];
		String repoName = "";
		String[] analysisList = new String[18];
		LinkedHashMap<Double, String> hashMap = new LinkedHashMap<Double, String>();

		if (i == 1) {
			repoName = repo1;

		} else if (i == 2) {
			repoName = repo2;
		}

		commands[0] = "java";
		commands[1] = " -jar";
		commands[2] = " 410-yardwand";
		commands[3] = "/lib";
		commands[4] = "/simian-2.3.35.jar";
		String xmlFormatOutput = " -formatter=plain ";

		System.out.println("Running duplication analysis on repo " + i + "...");

		// THE PROCESS BUILDER:

		for (int j = 0; j < 18; j++) {
			String currDirectory = "\"410-yardwand/lib/code-bases/" + repoName
					+ "/release" + j + "/";
			String parseFileType = "**/*.java\"";

			String cmd = commands[0] + commands[1] + commands[2] + commands[3]

			+ commands[4] + xmlFormatOutput + currDirectory + parseFileType;

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
			Process b = null;

			try {
				b = pb.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					b.getInputStream()));

			StringBuilder builder = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					builder.append(System.getProperty("line.separator"));
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			analysisList[j] = builder.toString();
			ArrayList<Double> list = repoMap.get(repoName);

			hashMap.put(list.get(j), analysisList[j]);

			// System.out.println(list.get(j) + "  " + analysisList[j]);
			// System.out.println("******************************************");

		}
		// TEST PRINT OUT:
		/*
		 * for(Object objname:hasmap.keySet()) { System.out.println(objname);
		 * System.out.println(hasmap.get(objname)); }
		 */
		System.out.println("Finished processing repo " + i + ".");
		return hashMap;
	}

	public static void main(String[] arg) {
		// Tester code:

		//CodeDuplicationDetector c = new CodeDuplicationDetector();

		//c.method1(1);

	}

}