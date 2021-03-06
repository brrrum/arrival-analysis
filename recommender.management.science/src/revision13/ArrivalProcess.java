package revision13;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.StatUtils;

import arrivalUtilities.BasicUtilities;
import cc.mallet.util.Maths;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import extension.copy.CExponent;
import extension.copy.CProjectVariables;
import extension.copy.CUtilities;
import umontreal.iro.lecuyer.probdist.InverseGaussianDist;

public class ArrivalProcess implements CProjectVariables{	
	
	private int sampleSize;
	private double arrivalRate;
	private String id10, id11;	
	private static int INITIAL_COUNTS = 1000;	
	private static double POWER_EXPONENT = 1.4;
	private static boolean randSelect = true;
	private double[] threshold;
	private ArrayList<LinkedList<DynamicArticleProperties>> allArticles= new ArrayList<LinkedList<DynamicArticleProperties>>();
	ArrayList<ArrayList<Double>> datapoints = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> JSdistortion = new ArrayList<ArrayList<Double>>();	
	ArrayList<Double> JSHdistortion = new ArrayList<Double>();
	ArrayList<ArrayList<Double>> pm2Points = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> hm2Points = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> pdatapoints = new ArrayList<ArrayList<Double>>();
	private ArrayList<Double> accLosses = new ArrayList<Double>();
	private ArrayList<Double> exAccLoss = new ArrayList<Double>();
	private static ArrayList<Double> normAccLoss = new ArrayList<Double>();
	private ArrayList<String> initialIds = new ArrayList<String>();
	private BasicUtilities bsu = new BasicUtilities();
	private static EthernetAddress addr = EthernetAddress.fromInterface();
	private static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(addr);
	//private static ZipfDistribution zpf = new ZipfDistribution(initialCounts,powerexponent);
	//private ZipfDistribution zpf;
	private LinkedList<DynamicArticleProperties> ltempcat; // all list of articles to be used in simulation 
	private LinkedList<DynamicArticleProperties> fda; // front page list for simulation 
	private ArrayList<DynamicArticleProperties> mpa; // most popular list for simulation
	private ArrayList<DynamicArticleProperties> impa;
	private ArrayList<DynamicArticleProperties> pmpa; // probabilistic popular list for display.
	private UpdateReader upr;	
	private Generator gr;
	private Random rand = new Random(7000); 	
	private int newClicks = 0;
	private int rpclicks; private int hnclicks;
	HashMap<String, Double[]> sample1 = null;
	static boolean once = true;
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;	
	public ArrivalProcess(int sampleSize, double arrivalRate, double[] threshold) throws NotStrictlyPositiveException {		
		this.arrivalRate = arrivalRate;
		this.sampleSize = sampleSize;
		this.threshold = threshold;
	}
	
	public ArrivalProcess(Generator rng, int sampleSize, double arrivalRate, double[] threshold) throws NotStrictlyPositiveException {
		// Arrival process of articles, lambda = average number of arrivals during a unit of time.
		// exponential arrival process of 200 articles of new articles while readers arrive in the system
		// break in front-page and upcoming articles
		this.arrivalRate = arrivalRate;
		this.sampleSize = sampleSize;
		this.threshold = threshold;
		this.gr = rng;		
	}
	
	public ArrivalProcess() {
		
	}

	public List<Integer> arrivalofArticles() {		
		
		Integer[] newsamples = new Integer[sampleSize];
		for(int i = 0; i < sampleSize; i++) {
			gr.setSeed(gr.refreshRNG()); 
			PoissonDistribution psd = 
					new PoissonDistribution(gr, arrivalRate, PoissonDistribution.DEFAULT_EPSILON, PoissonDistribution.DEFAULT_MAX_ITERATIONS);
			newsamples[i] = psd.sample();
		}		
		
		//List<Integer> sampleList = new ArrayList<Integer>(Arrays.asList(newsamples));	
		//System.out.println(newsamples.length); 
		return Arrays.asList(newsamples);		
	}
	
	private UUID getInitials(){		
		return uuidGenerator.generate();
	}
	
	public void seedArticles(List<DynamicArticleProperties> articleList, List<DynamicArticleProperties> initialTimeSort,
			List<DynamicArticleProperties> countSort, int ids) {		
		List<String> categories = BasicUtilities.getCategories();
		
		// total seed articles 8*50 = 400, among them 20+40+10 are displayed on frontPage; additional 200 articles arrive
		for(int k = 0; k < 400; k++) {
			gr.setSeed(gr.refreshRNG());
			ZipfDistribution zpf = new ZipfDistribution(gr, INITIAL_COUNTS, POWER_EXPONENT);
			int count = zpf.sample();
			
			Random rand2 = new Random(refreshRNG()); 
			count = rand2.nextInt(1000);
			
			String time = Long.toString(getInitials().timestamp());
			time = time.substring(6);
			DynamicArticleProperties dnp = new DynamicArticleProperties(getInitials().toString(), count, Long.parseLong(time));
			initialIds.add(dnp.getID());
			dnp.setCurrentClicks(count);
			dnp.setPcurrentClicks(count); 
			articleList.add(dnp);	 		
			//System.out.println(idone.toString()+"\t"+count+"\t"+Long.toString(idone.timestamp()).substring(6)); 
		}
		
		//temporal sorting for display and count based sorting for "most popular"
		Random srand = new Random(gr.refreshRNG());		
		Collections.shuffle(articleList, srand);		
		//System.out.println("breaking"+"\t"+"current"+"\t"+"initial"+"\t"+"front"+"\t"+"popular"+"\t"+"category"+"\t"+"time"+"\t"+"ID"); 
		int i,j = 0,update=0;		
		for(i=0;i<categories.size();i++){			
			String category = categories.get(i);
			update += j;			
			List<DynamicArticleProperties> categoryIdn= new ArrayList<DynamicArticleProperties>();			
			for(j=0;j<icategoryCount;j++){	//50 articles in each category			
				articleList.get(update+j).setCategory(category);				
				categoryIdn.add(articleList.get(update+j));				
				initialTimeSort.add(articleList.get(update+j));
				countSort.add(articleList.get(update+j));
			}
			// sort all articles for first time and then maintain LinkedList for updating 
			bsu.sortTime(categoryIdn);//sort articles based on timestamp.select five for front page			
			allArticles.add(new LinkedList<DynamicArticleProperties>(categoryIdn)); // locate articles in articleList and setFront true.			
			for(int n = 0; n < 5; n++) {
				DynamicArticleProperties darp = categoryIdn.get(n);
				darp.setFrontcat(true);
				for(DynamicArticleProperties dpr: articleList){
					if(darp.getID().equalsIgnoreCase(dpr.getID())){
						dpr.setFrontcat(true);
					}
				}
				 // when a new article is created its front property is set true.				
			}
					
		}
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < allArticles.size(); k++) {
			sb.append(allArticles.get(k).size()+"\t");
		}
		//System.out.println(sb);
		bsu.sortTime(initialTimeSort);		
		bsu.sortCont(countSort);
		fda = bsu.frontProminent(initialTimeSort, articleList); //20 most recent articles in the prominent page of front page.
		
		mpa = bsu.mostPopular(countSort, articleList, true); // 11 most popular list articles are returned
		DynamicArticleProperties ele = mpa.remove(10);
		id11 = ele.getID();
		DynamicArticleProperties ten = mpa.get(9);
		impa = mpa;
		ten.setCurrentClicks(ele.getCurrentClicks() + 1); //difference is one.
		ten.setPcurrentClicks(ele.getPcurrentClicks() + 1);
		ten.setInitialClicks(ele.getCurrentClicks() + 1); 
		//System.out.println("ten " + ten.getCurrentClicks() + " ele " + ele.getCurrentClicks()); 
		id10 = ten.getID();
		
		ArrayList<Double> datapoint = new ArrayList<Double>();
		ArrayList<Double> pdatapoint = new ArrayList<Double>();
		datapoint.add((double) 0); pdatapoint.add((double) 0); 
		datapoint.add((Math.log((double)ten.getCurrentClicks()/(double)ele.getCurrentClicks()))); 
		datapoints.add(datapoint); pdatapoint.add((Math.log((double)ten.getPcurrentClicks()/(double)ele.getPcurrentClicks()))); 
		// pdatapoints appears to be updated.
		bsu.matchingUpdate(allArticles, articleList);
		// get allArticles according to articleList, METHOD matchingUpdate.
		//bsu.printResult(allArticles);	UNCOMMENT TO GET UPDATE		
	}	
   
	public void updateArticles(List<DynamicArticleProperties> countSort, double exp, int ids) {
		List<Integer> arrivals = arrivalofArticles();
		//avoiding expensive writing
		BufferedWriter bw = null, bwh = null; 
		//BufferedWriter bw = createBufferedWriter(exp);
		//BufferedWriter bwh = createHBufferedWriter();
		for(int i = 0; i < countSort.size(); i++) {
			countSort.get(i).setExponent(exp); // this is added on august 29.
		}
		for(int it = 0; it < sampleSize; it++) {			
			int val = arrivals.get(it);
			
			if(!(val == 0)) {
				gr.setSeed(gr.refreshRNG());
				ZipfDistribution zpf = new ZipfDistribution(gr, INITIAL_COUNTS, POWER_EXPONENT);
				int count = zpf.sample(); // NEED TO FIXED IN FUTURE
				String time = Long.toString(getInitials().timestamp());				
				
				time = time.substring(6);
				DynamicArticleProperties dnp = new DynamicArticleProperties(getInitials().toString(), count, Long.parseLong(time));
				dnp.setCurrentClicks(count);
				dnp.setPcurrentClicks(count); 
				int catindex = rand.nextInt(BasicUtilities.getCategories().size());
				dnp.setCategory(BasicUtilities.getCategories().get(catindex));
				dnp.setFrontcat(true);
				dnp.setBreakingNews(true); //breaking news is basically most recent news articles.
				dnp.setExponent(exp); /// modified recently.......
				System.out.println("new article arrived with count : " + dnp.getClicks()); 
				List<DynamicArticleProperties> tempcat = allArticles.get(catindex);
				ltempcat = new LinkedList<DynamicArticleProperties>(tempcat);	
				ArrayDeque<DynamicArticleProperties> fivetemp = new ArrayDeque<DynamicArticleProperties>();	
				for(int i = 0; i < 5; i++) {
					DynamicArticleProperties d = ltempcat.poll(); 
					fivetemp.push(d);
					if(i == 4){	// only for the fifth element.
						d.setBreakingNews(false); 
						d.setFrontcat(false);
					}
				}
				
				for(int i = 0; i < 5; i++) {
					DynamicArticleProperties d = fivetemp.pop();	
					ltempcat.push(d);	
				}
				
				ltempcat.push(dnp);
				allArticles.get(catindex).push(dnp);
				
				DynamicArticleProperties db = fda.removeLast();
				db.setBreakingNews(false);//db.setFrontcat(false);
				fda.push(dnp);				
				countSort.add(dnp);				
			}
			
			bsu.sortCont(countSort); // IMPLEMENT PROBABILISTIC			
			mpa = bsu.mostPopular(countSort, bsu.convertList(allArticles), false); // we need only 10 articles to be displayed			
			//System.out.println("most popular : " + bsu.writeValue(mpa, true)); 
			int tempseed = refreshRNG();
			//pmpa = new MethodT().pMostPopular(countSort, bsu.convertList(allArticles), exp);
			pmpa = bsu.pMostPopular(tempseed, countSort, bsu.convertList(allArticles), exp, ids);
			// get the adjusted count of articles......
			//System.out.println("prob popular : " + bsu.writeValue(pmpa, true)); 
			//List<DynamicArticleProperties> copy = bsu.convertList(allArticles); //bsu.sortCont(copy);	 
			upr = new UpdateReader(refreshRNG());			
			 
			upr.frontPageSelection(threshold, mpa, fda, ltempcat, allArticles, pmpa);
			//System.out.println(" psum : " + bsu.printPSum(bsu.convertList(allArticles)) + "sum : " + bsu.printSum(bsu.convertList(allArticles)));
			sample1 = bsu.getHashMaps(bsu.convertList(allArticles));
			double accLoss = bsu.accuracyLoss(mpa, pmpa);
			double hsum = bsu.accuracyLossCounts(mpa, pmpa, allArticles, meanArticles, threshold, fda, true, randSelect);//here mpa and pmpa are old counts
			double psum = bsu.accuracyLossCounts(mpa, pmpa, allArticles, meanArticles, threshold, fda, false, randSelect);
			//FIX IT
			double expectedAccLoss = ((double)1/(double)10)*(Math.log(hsum/psum));
			//double expectedAccLoss = bsu.expectedAccuracyLoss(mpa, pmpa, allArticles, meanArticles, threshold); //allArticles has been updated			
			// store normalizedValue in a Array List.
			/*************Accuracy Loss Adjustment*****************/
			double distortion = distortionMeasure(sample1, initialIds, false, bw);
			accLosses.add(accLoss);
			exAccLoss.add(expectedAccLoss);
			/*******************Accuracy Loss Normalization***********/
			//updating M1, FROM HERE update M1 for probablistic.
			m1Plot(id10, id11, it);	
			pM1Plot(id10, id11, it);		
			
			ArrayList<Double> ePoint = new ArrayList<Double>();
			ePoint.add((double) (it + 1)); ePoint.add(distortion);
			JSdistortion.add(ePoint); 
			JSHdistortion.add(new AdditionalMethods().distortionHMeasure(sample1, initialIds, false, bwh));
			
			newClicks += upr.getNewClicks();
			rpclicks  = bsu.getRClicks(impa, bsu.convertList(allArticles));
			pm2Plot(newClicks, rpclicks, it);
			
			if(exp == 0) { // we need to execute it once for M2, extreme case
				double normalizedExpectedLoss = ((double)1/(double)10)*(Math.log(hsum/psum));
				normAccLoss.add(normalizedExpectedLoss); 
				hnclicks = bsu.getHRClicks(impa, bsu.convertList(allArticles));
				hm2Plot(newClicks, hnclicks, it);
				randSelect = false;
			}
			
		}
		try {
			upr.printTotal();
			//bw.flush();bw.close(); bwh.flush(); bwh.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
		//bsu.printResult(allArticles);
	}
	
	public BufferedWriter createBufferedWriter(double exp) {
		
		BufferedWriter fr = null;		
		try {
			fr = new BufferedWriter(new FileWriter("counts" + exp + ".csv"));
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return fr;
	}
	
public BufferedWriter createHBufferedWriter() {
		
		BufferedWriter fr = null;		
		try {
			fr = new BufferedWriter(new FileWriter("hard" + ".csv"));
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return fr;
	}

	public void pM1Plot(String d10, String d11, int it) {
		ArrayList<DynamicArticleProperties> ga = bsu.tenEleven(d10, d11, bsu.convertList(allArticles));
		DynamicArticleProperties ten = ga.get(0);
		DynamicArticleProperties ele = ga.get(1);
		ArrayList<Double> datapoint = new ArrayList<Double>();
		datapoint.add((double) it+1);
		datapoint.add(Math.log(((double)ten.getPcurrentClicks()/(double)ele.getPcurrentClicks())));
		pdatapoints.add(datapoint);
		
	}

	public void m1Plot(String d10, String d11, int it) {
		
		ArrayList<DynamicArticleProperties> ga = bsu.tenEleven(d10, d11, bsu.convertList(allArticles));
		DynamicArticleProperties ten = ga.get(0);
		DynamicArticleProperties ele = ga.get(1);
		ArrayList<Double> datapoint = new ArrayList<Double>();
		datapoint.add((double) it+1);
		datapoint.add(Math.log(((double)ten.getCurrentClicks()/(double)ele.getCurrentClicks())));
		datapoints.add(datapoint);	
	}
	
	public void pm2Plot(int ntClicks, int rclicks, int it) {
		double ratio = (double)rclicks/(double)ntClicks;		
		double m2 = (1 - ratio)*100;
		ArrayList<Double> datapoint = new ArrayList<Double>();
		datapoint.add((double) it+1);
		datapoint.add(m2);
		pm2Points.add(datapoint);
	}
	
	public void hm2Plot(int ntClicks, int rclicks, int it) {
		double ratio = (double)rclicks/(double)ntClicks;		
		double m2 = (1 - ratio)*100;
		ArrayList<Double> datapoint = new ArrayList<Double>();
		datapoint.add((double) it+1);
		datapoint.add(m2);
		hm2Points.add(datapoint);
	}
	
	public double distortionMeasure(HashMap<String, Double[]> sample1, ArrayList<String> ids, boolean klornot, BufferedWriter fr) {
		// think it in terms of share distortion.
		// for hardcutoff use index = 2.
		double[] initialc = new double[sample1.size()];
		double[] updated = new double[sample1.size()];
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		try {			
			Iterator<Entry<String, Double[]>> it = sample1.entrySet().iterator();
			int i = 0;
			while(it.hasNext()) {			
				String key = it.next().getKey(); 			
				boolean initial  = false;
				for(String id : ids) {
					if(id.equalsIgnoreCase(key)) {
						initial = true;
						break;
					}
				}
				
				if(initial) {
					initialc[i] = sample1.get(key)[0]; sb1.append(initialc[i] + ", ");
					updated[i] = sample1.get(key)[1];  sb2.append(updated[i] + ", ");  
				} else {
					initialc[i] = (double) 0; sb1.append(initialc[i] + ", "); 
					updated[i] = sample1.get(key)[1]; sb2.append(updated[i] + ", "); 
				}
				i++;
			}
			if(once) {
				//fr.write(sb1.toString());
				//fr.newLine();
				once = false;
			}
			
			//fr.write(sb2.toString());
			//fr.newLine();
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		double initialc1 = StatUtils.sum(initialc);
		double updated1 = StatUtils.sum(updated);
		
		for (int i = 0; i < updated.length; i++) {
			initialc[i] = initialc[i]/initialc1;
			updated[i] = updated[i]/updated1;
		}
		
		if(klornot) {
			return Maths.klDivergence(initialc, updated);
		} else {
			return Maths.jensenShannonDivergence(initialc, updated);
		}		
	}
		
	public int refreshRNG() {
		int seed = rand.nextInt();
		return seed;
	}
	
	public ArrayList<ArrayList<Double>> getHSimulationPoints() {
		return datapoints;
	}
	
	public ArrayList<ArrayList<Double>> getPSimulationPoints() {
		return pdatapoints;
	}
	
	public ArrayList<ArrayList<Double>> getpm2Plot() {
		return pm2Points;
	}
	
	public ArrayList<ArrayList<Double>> gethm2Plot() {
		return hm2Points;
	}
	
	public ArrayList<Double> getaccLosses() {
		return accLosses;
	}
	
	public ArrayList<Double> getExpAccLosses() {
		return exAccLoss;
	}
	
	public ArrayList<Double> getNormAccLoss() {
		return normAccLoss;
	}
	
	public double getAverageAccuracyLoss(ArrayList<Double> accLosses) { //at the end of simulation
		double sum = 0;
		double n = accLosses.size();
		for(int i = 0; i < n; i++) {
			sum += accLosses.get(i);
		}		
		return sum/n; 
	}	
	
	public ArrayList<ArrayList<Double>> getJSDistortion() {
		return JSdistortion;
	}
	
	public double getAverageJSD() {
		double sum = 0;
		double n = JSdistortion.size();
		for(int i = 0; i < n ; i++) {
			sum += JSdistortion.get(i).get(1);
		}
		
		return sum/n;
	}

	public ArrayList<Double> getJHSDistortion() {		
		return JSHdistortion;
	}	

}
