package concept.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang.ArrayUtils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import revision13.ArrivalProcess;
import revision13.DynamicArticleProperties;
import revision13.Generator;
import revision13.frontPageAndUpcoming;
import umontreal.iro.lecuyer.probdist.InverseGaussianDist;

public class Test1Main {	
	private static final double numberOfReaders = 30000;
	private static double[] exponent = { 1, 2, 3, 4, 5, 6};
	static double[] prob  = { 1, 0, 0}; 
	
	public static void main(String[] args) {		
		Generator gr = new Generator();
		gr.setSeed(764545);	gr.setSeed(gr.refreshRNG());
		
		List<DynamicArticleProperties> articleList = new LinkedList<DynamicArticleProperties>();
		List<DynamicArticleProperties> initialTimeSort = new LinkedList<DynamicArticleProperties>();
		List<DynamicArticleProperties> countSort = new ArrayList<DynamicArticleProperties>();
		
		ProbArrival arrivals = new ProbArrival(gr, (int) numberOfReaders, prob);
		arrivals.seedArticles(articleList, initialTimeSort, countSort);
		double exp = 1; 
		arrivals.updateArticles(countSort, exp); 
				
		writing2Column(arrivals.getJSDistortion(), arrivals.getJHSDistortion(),
				"C:/academic/" + "JSDistortion" + "-" + exp + ".csv");		
		writing2Column(arrivals.getPSimulationPoints(), arrivals.getHSimulationPoints(),
				"C:/academic/" + "M1" + "-" + exp + ".csv");
		writing2Column(arrivals.getpm2Plot(), arrivals.gethm2Plot(),
				"C:/academic/" + "M2" + "-" + exp + ".csv");
	}
	
	private static void writing2Column(ArrayList<ArrayList<Double>> data1, 
			ArrayList<ArrayList<Double>> data2,String path) {
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter(path));
			bw.write("iteration" + ","+ "prob" + "," + "hard"); 
			bw.newLine();
			for(int i = 0; i < data1.size(); i++) {				
				bw.write(Double.toString(data1.get(i).get(0))+","+Double.toString(data1.get(i).get(1))
						+","+Double.toString(data2.get(i).get(1)));	
				bw.newLine();
			}
			bw.flush(); bw.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	private static void writing1Column(ArrayList<Double> data, String path) {
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter(path));
			for(int i = 0; i < data.size(); i++) {
				bw.write(Double.toString(data.get(i)));	
				bw.newLine();
			}
			bw.flush(); bw.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}

}
