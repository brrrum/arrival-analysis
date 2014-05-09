package revision13;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import arrivalUtilities.TrajectoryPlots;

public class TemporaryControl {
	
	private static final double numberOfReaders = 10000; //lambda = .005
	private static final double newArticles = 20;
	private static double[] exponent = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };	
	
	private static ArrayList<double[]> rsensitivity() {	
		ArrayList<double[]> probs = new ArrayList<double[]>();
		double[] prob1  = { 0.3, 0.4, 0.3 };// most popular, breaking news,  5 articles on the front page for each category
		double[] prob2  = { 0.3, 0.5, 0.2 };
		double[] prob3  = { 0.4, 0.3, 0.3 };
		double[] prob4  = { 0.4, 0.4, 0.2 };		
		double[] prob5 =  { 0.5, 0.3, 0.2 };
		double[] prob6  = { 0.6, 0.2, 0.2 };
		probs.add(prob1); probs.add(prob2); probs.add(prob3); probs.add(prob4); probs.add(prob5);
		probs.add(prob6);
		
		return probs;
	}
	
	private class RunWithThread implements Runnable {
		
		double[] prob = new double[3];
		int i;
		
		public RunWithThread(double[] prob, int i) {
			this.prob = prob;
			this.i = i;
		}

		@Override
		public void run() {
			
			ArrayList<ArrayList<ArrayList<Double>>> repdatapoints = new ArrayList<ArrayList<ArrayList<Double>>>();
			ArrayList<ArrayList<ArrayList<Double>>> m2datapoints = new ArrayList<ArrayList<ArrayList<Double>>>();
			ArrayList<ArrayList<ArrayList<Double>>> rentropies = new ArrayList<ArrayList<ArrayList<Double>>>();
			ArrayList<ArrayList<Double>> acclosses = new ArrayList<ArrayList<Double>>();
			ArrayList<Double> jsds = new ArrayList<Double>();
			ArrayList<Double> hvalues = new ArrayList<Double>();
			double lambda = 0.002;			
			
			for(int i = 1; i <= 10; i++) {
				Generator gr = new Generator();
				gr.setSeed(764545);	gr.setSeed(gr.refreshRNG());
				
				List<DynamicArticleProperties> articleList = new LinkedList<DynamicArticleProperties>();
				List<DynamicArticleProperties> initialTimeSort = new LinkedList<DynamicArticleProperties>();
				List<DynamicArticleProperties> countSort = new ArrayList<DynamicArticleProperties>();
				ArrayList<Double> losses = new ArrayList<Double>();				
				//ArrivalProcess arrivals = new ArrivalProcess((int) numberOfReaders, lambda, prob);
				
				ArrivalProcess arrivals = new ArrivalProcess(gr, (int) numberOfReaders, lambda, prob);	
				arrivals.seedArticles(articleList,  initialTimeSort, countSort);
				arrivals.updateArticles(countSort, exponent[i-1]); 
				
				if( i == 1) {
					ArrayList<ArrayList<Double>> hard = arrivals.getHSimulationPoints(); /// arraylist with iteration and m1 value.
					ArrayList<ArrayList<Double>> m2hard = arrivals.gethm2Plot();
					repdatapoints.add(hard); m2datapoints.add(m2hard);
					writesingColumn(arrivals.getJHSDistortion()); //we can skip distortion for hardcutoff
				}
				
				ArrayList<ArrayList<Double>> prob = arrivals.getPSimulationPoints();				
				ArrayList<ArrayList<Double>> m2prob = arrivals.getpm2Plot();
				repdatapoints.add(prob); m2datapoints.add(m2prob);
				rentropies.add(arrivals.getJSDistortion()); 
				
				double loss = arrivals.getAverageAccuracyLoss();
				losses.add((double) i); losses.add(loss);
				acclosses.add(losses);
				
				double distortion = arrivals.getAverageJSD();
				jsds.add(distortion);
				System.out.println("-----------new exponent " + i + "----------"); 
			}
			writeFile(repdatapoints, "M1" + "-" + i + ".csv");
			writeFile(m2datapoints, "M2" + "-" + i + ".csv");
			writeFile(rentropies, "JSDistortion" + "-" + i + ".csv");
			writeMetric(acclosses, jsds, "Metrics" + "-" + i + ".csv");
		}	
		
	}
	
	private static void writeMetric(ArrayList<ArrayList<Double>> acclosses, ArrayList<Double> jsds,
			String path) {		
		
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter(path));			
			bw.write("exponent" + "," + "b0"+"," + "b0.1"+"," + "b0.2"+"," + "b0.3"+"," + "b0.4"+"," + "b0.5"+"," + "b0.6"+"," + "b0.7"+"," + "b0.8" + "," + "b0.9"+"," + "b1"+","); 
			bw.newLine();
			
			for(int i = 0; i < acclosses.size(); i++) {
				double exponent = acclosses.get(i).get(0);
				double accloss = acclosses.get(i).get(1);
				double jsd = jsds.get(i);
				
				StringBuilder sb = new StringBuilder();
				for(int j = 0; j <= 10; j++) {
					double beta = (double)j/(double)10;
					double metric = (beta)*accloss + (1-beta)*jsd;
					sb.append(metric + ","); 
				}
				
				bw.write(exponent + "," + sb); 
				bw.newLine();
			}
			
			bw.flush(); bw.close();
			
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		
	}
	
	public static void main(String[] args) throws IOException {
		
		TemporaryControl tmp = new TemporaryControl();
		ExecutorService exs = Executors.newCachedThreadPool();
		ArrayList<Future<?>> futures = new ArrayList<Future<?>>();
		
		for(int i = 5; i < rsensitivity().size(); i++) {			
			TemporaryControl.RunWithThread runth = tmp.new RunWithThread(rsensitivity().get(i), i+1);
			futures.add(exs.submit(runth)); 
			
		}
		System.out.println(" number of threads : " + java.lang.Thread.activeCount());
		
		for (Future<?> f: futures) { 
			
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				
				e.printStackTrace();
			}
		}
		System.out.println("done!");
		exs.shutdown();
	}
	
	public static void main1(String[] args) throws IOException {		
		// number of arrivals in one unit, where interval between ith and (i+1)th arrival has 
		// exponential distribution with same lambda
		ArrayList<ArrayList<ArrayList<Double>>> repdatapoints = new ArrayList<ArrayList<ArrayList<Double>>>();		
		double lambda = 0.002;//newArticles/numberOfReaders;		
		for(int i = 1; i <= 10; i++) {
			List<DynamicArticleProperties> articleList = new LinkedList<DynamicArticleProperties>();
			List<DynamicArticleProperties> initialTimeSort = new LinkedList<DynamicArticleProperties>();
			List<DynamicArticleProperties> countSort = new ArrayList<DynamicArticleProperties>();
			
			ArrivalProcess arrivals = new ArrivalProcess((int) numberOfReaders, lambda, null);		
			arrivals.seedArticles(articleList,  initialTimeSort, countSort);
			arrivals.updateArticles(countSort, exponent[i-1]); 
			
			if( i == 1) {
				ArrayList<ArrayList<Double>> hard = arrivals.getHSimulationPoints();
				repdatapoints.add(hard);
			}
			ArrayList<ArrayList<Double>> prob = arrivals.getPSimulationPoints();
			repdatapoints.add(prob);
			//CHECK FOR ARRIVAL OF ARTICLES
		}
		writeFile(repdatapoints, "M1.csv");
		//TrajectoryPlots plc = new TrajectoryPlots("M1 trajectory plot", repdatapoints);		
		//TrajectoryPlots plcx = new TrajectoryPlots("scatter plot", arrivals.getHSimulationPoints(), 
				//arrivals.getPSimulationPoints());		
		
		//plc.pack();
		//RefineryUtilities.centerFrameOnScreen(plc);
		//plc.setVisible(true);
		System.out.println("done !"); 		
		
		//arrivals.previousReadModel(10, .8);		
	}
	
	private static void writesingColumn(ArrayList<Double> data) {
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter("hardDistortion.csv"));
			for(int i = 0; i < data.size(); i++) {
				bw.write(Double.toString(data.get(i)));	
				bw.newLine();
			}
			bw.flush(); bw.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	private static void writeFile(ArrayList<ArrayList<ArrayList<Double>>> repdatapoints, String path) {
		try {
			BufferedWriter bw  = new BufferedWriter(new FileWriter(path));
			if(path.contains("JSD")) {
				bw.write("iteration" + "," );
				for(int i = 1; i <= repdatapoints.size(); i++) {
					bw.write("exp"+ i + ","); 
				}
			}
			else {
				bw.write("iteration" + "," + "hard"+",");
				for(int i = 1; i < repdatapoints.size(); i++) {
					bw.write("exp"+ i + ","); 
				}
			}						
			bw.newLine();
			
			for(int i = 0; i < repdatapoints.get(0).size()-1; i++) {
				StringBuilder sb = new StringBuilder();
				for( int j = 0; j < repdatapoints.size(); j++) {
					if( j == 0) {
						sb.append(repdatapoints.get(j).get(i).get(0) + ",");  
					}
					sb.append(repdatapoints.get(j).get(i).get(1) + ","); 
				}
				bw.write(sb.toString());
				bw.newLine();
			}			
			
			bw.flush();bw.close(); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
