package simulation.metrics;

import java.util.HashMap;

public class MetricTest {

	public static void main(String[] args) {
		//Key corresponds to node degree
		// value = the number of nodes corresponding to key i.e. frequency of the occurrence of a degree in a graph
		HashMap<String, Double> sample1 = new HashMap<String, Double>();		
		sample1.put("10", (double) 2); sample1.put("12", (double) 1); sample1.put("15", (double) 1);		
		HashMap<String, Double> sample2 = new HashMap<String, Double>();
		sample2.put("10", (double) 7); sample2.put("12", (double) 2); sample2.put("17", (double) 3);
		
		SmoothKL smk = new SmoothKL();
		System.out.println(smk.computeKL(sample1, sample2));	 	

	}

}
