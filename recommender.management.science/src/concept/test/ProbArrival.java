package concept.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.math3.distribution.ZipfDistribution;

import arrivalUtilities.BasicUtilities;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import revision13.DynamicArticleProperties;
import revision13.Generator;
import revision13.UpdateReader;

public class ProbArrival {
	
	private static int INITIAL_COUNTS = 1000;	
	private static double POWER_EXPONENT = 1.4;
	private String id10, id11;
	private Generator gr;
	private int sampleSize;
	private double[] prob;
	private LinkedList<DynamicArticleProperties> fda;
	private ArrayList<DynamicArticleProperties> mpa;
	private ArrayList<DynamicArticleProperties> impa;
	private ArrayList<DynamicArticleProperties> pmpa;
	private ProbUpdate upr;
	private static EthernetAddress addr = EthernetAddress.fromInterface();
	private static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(addr);
	private ArrayList<String> initialIds = new ArrayList<String>();
	ArrayList<ArrayList<Double>> datapoints = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> pdatapoints = new ArrayList<ArrayList<Double>>();
	private BasicUtilities bsu = new BasicUtilities();
	private Random rand = new Random(7000); 
	private ArrayList<LinkedList<DynamicArticleProperties>> allArticles= new ArrayList<LinkedList<DynamicArticleProperties>>();

	public ProbArrival(Generator gr, int numberofreaders, double[] prob) {
		this.gr = gr;	
		this.sampleSize = numberofreaders;
		this.prob = prob;
	}
	
	private UUID getInitials() {		
		return uuidGenerator.generate();
	}
	
	public void seedArticles(List<DynamicArticleProperties> articleList, List<DynamicArticleProperties> initialTimeSort,
			List<DynamicArticleProperties> countSort) {
		List<String> categories = BasicUtilities.getCategories();
		for(int k = 0; k < 80; k++) {
			gr.setSeed(gr.refreshRNG());
			ZipfDistribution zpf = new ZipfDistribution(gr, INITIAL_COUNTS, POWER_EXPONENT);
			int count = zpf.sample();
			String time = Long.toString(getInitials().timestamp());
			time = time.substring(6);
			DynamicArticleProperties dnp = new DynamicArticleProperties(getInitials().toString(), count, Long.parseLong(time));
			initialIds.add(dnp.getID());
			dnp.setCurrentClicks(count);
			dnp.setPcurrentClicks(count); 
			articleList.add(dnp);			
		}
		
		Random srand = new Random(gr.refreshRNG());	
		Collections.shuffle(articleList, srand);
		int i,j = 0,update=0;		
		for(i=0;i<categories.size();i++) {
			String category = categories.get(i);
			update += j;
			List<DynamicArticleProperties> categoryIdn= new ArrayList<DynamicArticleProperties>();	
			for(j=0;j<10;j++) {
				articleList.get(update+j).setCategory(category);	
				categoryIdn.add(articleList.get(update+j));	
				initialTimeSort.add(articleList.get(update+j));
				countSort.add(articleList.get(update+j));
			}
			
			bsu.sortTime(categoryIdn);
			allArticles.add(new LinkedList<DynamicArticleProperties>(categoryIdn)); 
			for(int n = 0; n < 5; n++) {
				DynamicArticleProperties darp = categoryIdn.get(n);
				darp.setFrontcat(true);
				for(DynamicArticleProperties dpr: articleList) {
					if(darp.getID().equalsIgnoreCase(dpr.getID())) {
						dpr.setFrontcat(true);
					}
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < allArticles.size(); k++) {
			sb.append(allArticles.get(k).size()+"\t");
		}
		System.out.println(sb);
		
		bsu.sortTime(initialTimeSort);
		bsu.sortCont(countSort);
		fda = bsu.frontProminent(initialTimeSort, articleList);
		
		mpa = bsu.mostPopular(countSort, articleList, true);
		DynamicArticleProperties ele = mpa.remove(10);
		id11 = ele.getID();
		DynamicArticleProperties ten = mpa.get(9);
		impa = mpa;
		ten.setCurrentClicks(ele.getCurrentClicks() + 1);
		ten.setPcurrentClicks(ele.getPcurrentClicks() + 1);
		ten.setInitialClicks(ele.getCurrentClicks() + 1); 
		id10 = ten.getID();
		
		ArrayList<Double> datapoint = new ArrayList<Double>();
		ArrayList<Double> pdatapoint = new ArrayList<Double>();
		datapoint.add((double) 0); pdatapoint.add((double) 0); 
		datapoint.add((Math.log((double)ten.getCurrentClicks()/(double)ele.getCurrentClicks()))); 
		datapoints.add(datapoint); 
		pdatapoint.add((Math.log((double)ten.getPcurrentClicks()/(double)ele.getPcurrentClicks())));
		pdatapoints.add(pdatapoint);
		bsu.matchingUpdate(allArticles, articleList);		
	}
	
	public void updateArticles(List<DynamicArticleProperties> countSort,
			double exp) {
		BufferedWriter bw = createBufferedWriter(exp, false);
		BufferedWriter bwh = createBufferedWriter(exp, true); 
		for(int it = 0; it < sampleSize; it++) {	
			int tempseed = refreshRNG();
			pmpa = bsu.pMostPopular(tempseed, countSort, bsu.convertList(allArticles), exp);
			upr = new ProbUpdate(refreshRNG());
			upr.frontPageSelection(prob, mpa, fda, allArticles, pmpa);
		}
		
	}

	private int refreshRNG() {
		int seed = rand.nextInt();
		return seed;
	}

	private BufferedWriter createBufferedWriter(double exp, boolean count) {
		String filepath = null;
		if(count) {
			filepath = "C:/academic/" + "counts-h" + ".csv";
		} else {
			filepath = "C:/academic/" + "counts" + (int)exp + ".csv"; 
		}
		
		BufferedWriter fr = null;		
		try {
			fr = new BufferedWriter(new FileWriter(filepath));
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return fr;
	}

}
