package extension;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class ControlCenter extends DataGeneration
{
	private static String PROJECT_PATH = "C" + ":" + File.separator
			+ "RepastSimphony-2.1" + File.separator
			+ "workspace" + File.separator + "recommender.management.science"
			+ File.separator;
	private static String SOURCE_PATH;

	public static void main(String args[]) throws Exception 
	{		
		double[] reading_prob = { 0.1, 0.25, 0.5, 0.9 };
		double[] beta = { 0, 0.5, 1 };
		double exponent = 1;		
		int rep = 1;
		repetition(beta, exponent, reading_prob, rep);
		//MAKE SURE ALL CALLS ARE CORRECT!, some declaration need to be moved at class level		

	}

	private static void repetition(double[] beta, double exponent,
			double[] reading_prob, int rep) throws Exception {
		 
		int na = 200;
		int np = 10;
		int itr = 100;
		String inarticles[] = new String[na];
		String[] pInarticles = new String[na]; // copy of inarticles
		String file_path = "C:/eclipse-projects/workspace/recommender.management.science/counts.txt";
		
		//int countx[] = new int[na];		
		ProbSelection mprobInfluence;
		Exponent mexponent;
		ManipulationCount mcount;		
		String[] aryLines = readFile(file_path);		
		Integer[] countxy = new Integer[na];		
		int[] anArray;
		String istr;
		
		//reading data
		
		
		for (int i = 0; i < na; i++) 
		{
			//countx[i] = (int)(Math.random()*1000);
			istr = Integer.toString(i + 1);
			inarticles[i] = "A".concat(istr);
		}
		
		for (int i = 0; i < pInarticles.length; i++) 
		{
			pInarticles[i] = inarticles[i];
			// pCount[i] = countxy[i];
		}
		
		for( int i = 0; i < rep; i++) {
						
			mprobInfluence = new ProbSelection();
			mexponent = new Exponent();
			mcount = new ManipulationCount();
			anArray = mcount.manipulated(mexponent.inspection_in, 50);
			
			for(int j = 3; j < reading_prob.length; j++) {
				
				for( int k = 0; k < aryLines.length; k++) {
					countxy[k] = Integer.valueOf(aryLines[k]); 
					
				}
				
				Arrays.sort(countxy, Collections.reverseOrder());
				
				String strRep = Integer.toString(rep+1) + "_";
				String strDirectory = Double.toString(reading_prob[i]);
				String crDirectory = "prob_".concat(strDirectory);
				String finalDirectory = strRep.concat(crDirectory);
				boolean success = (new File(finalDirectory).mkdir());
				
				if (success) 
				{
					System.out.println("Directory: " + finalDirectory
							+ " created");
				}
				
				SOURCE_PATH = PROJECT_PATH + finalDirectory + "/";
				mprobInfluence.print1(anArray, inarticles, countxy, np,
						reading_prob[i], SOURCE_PATH, exponent, itr, beta);
				
				for (int i1 = 0; i1 < pInarticles.length; i1++) 
				{
					inarticles[i1] = pInarticles[i1];
					
				}
				System.out.println("\nfinished");
			}
		}
	}

}
