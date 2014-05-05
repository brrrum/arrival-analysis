package extension.copy;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class CcontrolCenter extends CUtilities implements CProjectVariables {
	
	private static String SOURCE_PATH;

	public static void main(String args[]) throws Exception 
	{		
		double[] reading_prob = { 0.1, 0.25, 0.5, 0.9 };
		double[] beta = { 0, 0.5, 1 };
		double exponent = 1;		
		int rep = 1;
		repetition(beta, exponent, reading_prob, rep);		

	}

	private static void repetition(double[] beta, double exponent,
			double[] reading_prob, int rep) throws Exception {		
		
		String[] pInarticles = new String[na]; // copy of inarticles
		String file_path = "C:/RepastSimphony-2.1/workspace/recommender.management.science/counts.txt";		
		
		ProbSelectionC mprobInfluence;
		CExponent mexponent;
		CManipulationCount mcount;		
		String[] aryLines = readFile(file_path);		
		Integer[] countxy = new Integer[na];		
		int[] anArray;
		String istr;		
		//reading data		
		
		for (int i = 0; i < na; i++) {
			//countx[i] = (int)(Math.random()*1000);
			// pCount[i] = countxy[i];
			setIds();
			pInarticles[i] = trackarticles[i];
		}		
		
		for( int i = 0; i < rep; i++) {
						
			mprobInfluence = new ProbSelectionC();
			mexponent = new CExponent();
			mcount = new CManipulationCount();
			anArray = mcount.manipulated(range, 0);
			
			for(int j = 3; j < reading_prob.length; j++) {
				
				for( int k = 0; k < aryLines.length; k++) {
					countxy[k] = Integer.valueOf(aryLines[k]); 
					
				}
				
				Arrays.sort(countxy, Collections.reverseOrder());
				
				String strRep = Integer.toString(rep+1) + "_";
				String strDirectory = Double.toString(reading_prob[j]);
				String crDirectory = "prob_".concat(strDirectory);
				String finalDirectory = strRep.concat(crDirectory);
				boolean success = (new File(finalDirectory).mkdir());
				
				if (success) 
				{
					System.out.println("Directory: " + finalDirectory
							+ " created");
				}
				
				SOURCE_PATH = PROJECT_PATH + finalDirectory + "/";
				mprobInfluence.print1(anArray, pInarticles, countxy, np,
						reading_prob[i], SOURCE_PATH, exponent, itr, beta);
				
				for (int i1 = 0; i1 < pInarticles.length; i1++) 
				{
					pInarticles[i] = trackarticles[i];
					
				}
				System.out.println("\nfinished");
			}
		}
	}

}
