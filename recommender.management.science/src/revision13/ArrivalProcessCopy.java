package revision13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import extension.copy.CExponent;
import extension.copy.CProjectVariables;
import extension.copy.CUtilities;

public class ArrivalProcessCopy extends PoissonDistribution implements CProjectVariables{

	private static int sampleSize;
	private static String resultsPath = "C:/academic/recommender system 2/Management Science/review/simulation-Results/";
	private CUtilities utilities = new CUtilities();
	private List<Double> sampleList;
	private List<Double> clicks = new ArrayList<Double>(); 
	private Double[] sampleArray, countxy;	
	private List<String> nameList, copyofnameList;
	private String[] nameArray, copynameArray;
	private CExponent exponent = new CExponent();
	private static int initialCounts = 1000;
	private static double powerexponent = 1.4;
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	private static double lambda = 0.002;
	public ArrivalProcessCopy(int sampleSize, double arrivalRate) throws NotStrictlyPositiveException {
		super(arrivalRate);
		// Arrival process of articles, lambda = average number of arrivals during a unit of time.
		// exponential arrival process of 200 articles of new articles while readers arrive in the system
		// break in front-page and upcoming articles	
		this.sampleSize = sampleSize;		
	}
	
	public List<Integer> arrivalofArticles() {
		int[] samples = null; //sample(sampleSize);		
		Integer[] newsamples = new Integer[samples.length];
		int i = 0;
		for(int value: samples) {
			newsamples[i++] = Integer.valueOf(value);
		}		
		//List<Integer> sampleList = new ArrayList<Integer>(Arrays.asList(newsamples));	
		System.out.println(newsamples.length); 
		return Arrays.asList(newsamples);		
	}
	
	public void seedArticles() {		
		//FROM HERE.
		utilities.setIds(); 
		nameList = utilities.getIds();		
		try {
			String[] pInarticles = new String[na];
			countxy = new Double[na];
			String file_path = "C:/RepastSimphony-2.1/workspace/recommender.management.science/counts.txt";
			String[] aryLines = utilities.readFile(file_path);
			
			for( int k = 0; k < aryLines.length; k++) {
				countxy[k] = Double.valueOf(aryLines[k]); 
			}
			sampleList = new ArrayList<Double>(Arrays.asList(countxy));
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void updateArticles(int newindex) {
		Random rand = new Random();
		int count = 10 + rand.nextInt(70);				
		sampleList.add(new Double(count));				
		nameList.add(nameList.size(),newIds(newindex));
		copyofnameList.add(copyofnameList.size(),newIds(newindex)); 
		copynameArray = copyofnameList.toArray(new String[nameList.size()]);
		// list to array
		sampleArray = sampleList.toArray(new Double[sampleList.size()]);				
		nameArray = nameList.toArray(new String[nameList.size()]);
		utilities.count_sort(sampleArray, sampleList.size(), nameArray);
		//sampleList.remove(sampleList.size()-1);	
	}
	
	public void previousReadModel(int ps, double reading_prob) throws IOException {
		double[] clickcounts;
		Random rand = new Random();		
		List<Integer> arrivals = arrivalofArticles();
		copyofnameList = nameList;
		sampleArray = sampleList.toArray(new Double[sampleList.size()]);		 
		nameArray = nameList.toArray(new String[nameList.size()]);
		copynameArray = nameList.toArray(new String[nameList.size()]);
		// writing heading of csv file.
		List<String> subheading = copyofnameList;
		int newindex = 0;
		for(int i = 0; i < sampleSize; i++) {
			if(arrivals.get(i) != 0) {
				newindex++;
				subheading.add(newIds(newindex));
			}			
		}		
		try {
			utilities.makecsv(subheading.toArray(new String[subheading.size()]), resultsPath + "preReader.csv");
		} catch(Exception ex) {
			ex.printStackTrace();
		}			
		
		newindex = 0;
		for(int i = 0; i < sampleSize; i++) {
			//calSum();
			double pu = rand.nextDouble();
			// arrival of articles
			if(arrivals.get(i) != 0) {
				newindex++;
			updateArticles((newindex));			
			}
			// article being read			
			clickcounts = ArrayUtils.toPrimitive(clicks.toArray(new Double[clicks.size()])); // NOT IMPLEMENTED
			// nameList and clicks have same order
			exponent.read(copynameArray, nameArray, ps, sampleArray, clickcounts, reading_prob, pu);
			utilities.count_sort(sampleArray, sampleList.size(), nameArray);
			sampleList = new ArrayList<Double>(Arrays.asList(sampleArray)); 
			nameList = new ArrayList<String>(Arrays.asList(nameArray)); 
			//List update for count and ids			
			utilities.writeRow(copynameArray, nameArray, sampleArray); 
			
		//	System.out.println(forloopData(sampleArray)); 
			
		} 		 
		// write csv file in last after all calculations are performed
		utilities.closefile();
		System.out.println();		
	}
	
	public static String forloopData(Object[] nameArray) {
		String ss = new String(); 
		for(int i = 0; i < nameArray.length; i++) {
			ss = ss.concat(nameArray[i].toString() + ", ");
		}
		return ss;
	}
	
	private String newIds(int i) {
		return "A".concat(Integer.toString(na+i));
		
	}
	
	private void calSum() {
		int sum = 0;
		for(int i = 0; i < sampleArray.length; i++) {
			sum+=sampleArray[i];
		}
		System.out.println(sum);
	}

}
