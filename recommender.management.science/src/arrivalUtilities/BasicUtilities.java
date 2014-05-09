package arrivalUtilities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import revision13.DynamicArticleProperties;

public class BasicUtilities {
	
	public static ArrayList<String> getCategories(){
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("LocalNews");
		categories.add("World");
		categories.add("Weather");
		categories.add("Sports");
		categories.add("Health");
		categories.add("Business");
		categories.add("Technology");
		categories.add("Politics");
		return categories;
	}
	
	public void sortTime(List<DynamicArticleProperties> categoryIdn) {
		DynamicArticleProperties temp;
		for(int i=0; i<categoryIdn.size()-1; i++) {
			for(int j=0; j<categoryIdn.size()-1-i; j++) {
				if(categoryIdn.get(j).getCreationTime() < categoryIdn.get(j+1).getCreationTime()) {
					temp = categoryIdn.get(j+1);
					categoryIdn.set(j+1, categoryIdn.get(j));
					categoryIdn.set(j, temp);
				}
			}
		}
	}
	
	public void sortCont(List<DynamicArticleProperties> soa) {
		DynamicArticleProperties temp;
		for(int i=0; i<soa.size()-1; i++) {
			for(int j=0; j<soa.size()-1-i; j++) {
				if(soa.get(j).getCurrentClicks() < soa.get(j+1).getCurrentClicks()) {
					temp = soa.get(j+1);
					soa.set(j+1, soa.get(j));
					soa.set(j, temp);
				}
			}
		}
	}

	public ArrayList<DynamicArticleProperties> mostPopular(List<DynamicArticleProperties> countSort, 
			List<DynamicArticleProperties> articleList, boolean initial) {
		ArrayList<DynamicArticleProperties> popular_articles = new ArrayList<DynamicArticleProperties>();
		int n;
		if(initial) {
			n = 11;
		} else {
			n = 10;
		}
		for(int i = 0; i<n; i++){
			popular_articles.add(countSort.get(i));
			if(n < 11) {
			countSort.get(i).setHardPopular(true);
			}
			
			for(DynamicArticleProperties dpr: articleList){
				if(countSort.get(i).getID().equalsIgnoreCase(dpr.getID())){
					if(n < 11) {
					dpr.setHardPopular(true);
					}
					break;
				}
			}
		}
		return popular_articles;		 
	}
	
	public ArrayList<DynamicArticleProperties> pMostPopular(int seed, List<DynamicArticleProperties> countSort, List<DynamicArticleProperties> convertList, 
			double exp) {
		
		ArrayList<DynamicArticleProperties> prob_articles = new ArrayList<DynamicArticleProperties>();
		int size = countSort.size();		
		int n = 10; // select 10 articles probabilistically; 
		ArrayList<ArrayList<Double>> amodified = new ArrayList<ArrayList<Double>>(size); 
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < size; i++) {
			ArrayList<Double> value = new ArrayList<Double>(2); // for exponent and value, exponent for influence limiter
			value.add(exp); value.add(Math.pow(countSort.get(i).getPcurrentClicks(), exp));
			amodified.add(value);
			sb.append(value.get(1) + " "); 
		}		

		ArrayList<Double> tempcount = new ArrayList<Double>(size + 1);
		tempcount.add((double)-1);
		
		for(int i = 1; i < size + 1; i++) {
			if(i == 1) {
				double temp = amodified.get(i-1).get(1);
				tempcount.add(tempcount.get(i-1) + temp + 1); 
				//System.out.println(tempcount.get(i)); 
			} else {
				double temp = amodified.get(i-1).get(1);
				tempcount.add(tempcount.get(i-1) + temp); 
				//System.out.println(tempcount.get(i)); 
			}
		}		

		ArrayList<DynamicArticleProperties> darticle = new ArrayList<DynamicArticleProperties>(size);		
		for(int i = 0; i < size; i++) {
			DynamicArticleProperties dp = countSort.get(i);
			darticle.add(dp);
		}
		
		Random rand = new Random(seed);
		for(int it = 0; it < n ; it++) {
			double scount = (double) (rand.nextDouble() * tempcount.get(tempcount.size()-1));
			//double scount = (double) (Math.random() * tempcount.get(tempcount.size()-1));			
			//System.out.println("scount : " + (int)scount + " tempcount : " + tempcount.get(tempcount.size()-1)); 
			int i = 0;	
			int done = 0;	
			while (done == 0) {				
				if(scount  <= tempcount.get(i + 1) && scount > tempcount.get(i)) {
					done = 1;
					double j = amodified.get(i).get(1);					
					prob_articles.add(darticle.get(i));
					darticle.remove(i);
					amodified.remove(i);
					tempcount.remove(i+1);

					while (i + 1 <= tempcount.size() - 1) {
						tempcount.set(i+1, tempcount.get(i+1) - j);
						i++;
					}
				}
				i++;
			}
		}
		
		// marking most popular list in probabilistic selection. //REVISE THIS CALLING
		for(DynamicArticleProperties dp: convertList) {
			for(DynamicArticleProperties dpc : prob_articles) {
				if(dp.getCreationTime() == dpc.getCreationTime()) {
					dp.setPmostPopular(true); dpc.setPmostPopular(true);
				} else {
					dp.setPmostPopular(false); dpc.setPmostPopular(false);
				}
			}
		}
		BasicUtilities bu = new BasicUtilities();
		bu.sortCont(prob_articles);		
			
		return prob_articles;
	}
	
	public ArrayList<DynamicArticleProperties> tenEleven(String ten, String ele, List<DynamicArticleProperties> articleList) {
		ArrayList<DynamicArticleProperties> idf = new ArrayList<DynamicArticleProperties>();
		for(DynamicArticleProperties dpr: articleList) {
			if(ten.equalsIgnoreCase(dpr.getID())) {
				idf.add(dpr);
				break;
			}
		}
		
		for(DynamicArticleProperties dpr: articleList) {
			if(ele.equalsIgnoreCase(dpr.getID())) {
				idf.add(dpr);
				break;
			}
		}
		
		return idf;
	}
	
	public LinkedList<DynamicArticleProperties> frontProminent(List<DynamicArticleProperties> initialTimeSort, 
			List<DynamicArticleProperties> articleList) {
		LinkedList<DynamicArticleProperties> pro_articles = new LinkedList<DynamicArticleProperties>();
		
		for(int i = 0; i < 20; i++){
			pro_articles.add(initialTimeSort.get(i));
			//initialTimeSort.get(i).setFront(true);
			initialTimeSort.get(i).setBreakingNews(true);
			
			for(DynamicArticleProperties dpr: articleList){
				if(initialTimeSort.get(i).getID().equalsIgnoreCase(dpr.getID())){
					//dpr.setFront(true);
					dpr.setBreakingNews(true);
					break;
				}
			}
		}		
		return pro_articles;
	}

	public void matchingUpdate(
			ArrayList<LinkedList<DynamicArticleProperties>> allArticles,
			List<DynamicArticleProperties> articleList) {

			for(int i = 0; i < getCategories().size(); i++){
				String gc = getCategories().get(i);
				List<DynamicArticleProperties> al = allArticles.get(i);
				List<DynamicArticleProperties> data = submatch(gc, articleList);
				for(DynamicArticleProperties dap: al){					
					dap.setCategory(gc);
					for(int j = 0; j < data.size(); j++){
						
						if(dap.getCreationTime() == data.get(j).getCreationTime()){
							dap.setBreakingNews(data.get(j).getBreakingNews());
							dap.setCurrentClicks(data.get(j).getCurrentClicks());
							dap.setFrontcat(data.get(j).getFrontcat());
							dap.setHardPopular(data.get(j).getPopularMark());
							dap.setPcurrentClicks(data.get(j).getPcurrentClicks()); 
							break;
						}
						
					}
					
				}				
			}		
	}
	
	public List<DynamicArticleProperties> submatch(String group, List<DynamicArticleProperties> articleList){
		List<DynamicArticleProperties> sublist = new ArrayList<DynamicArticleProperties>();
		for(DynamicArticleProperties dp: articleList){
			if(dp.getCategory().equalsIgnoreCase(group)){
				sublist.add(dp);
			}
		}
		return sublist;
	}
	
	public int readPattern(int n, int seed) {
		double[] d1 = new double[n];
		double[] d2 = new double[n];
		double[] sumd = new double[n+1];
		sumd[0] = -1;
		
		for(int j=0; j<n; j++) {
			d2[j] = j+1;
		}
		
		double temp;
		for(int j=1; j<=n-1; j++) {
			temp = d2[j-1];
			d2[j] = d2[j]+temp;						
		}
				
		for(int i=0; i<n; i++) {
			d1[i]= (n-i)/d2[n-1];
			if(i>0) {
				temp = d1[i-1];
				d1[i] = d1[i]+temp;				
			}
			sumd[i+1] = d1[i];
		}
		//selection of articles
		Random random = new Random(seed);
		double rn = random.nextDouble();
		int done = 0;
		int i = 0;
		while(done==0) {
			if(rn>sumd[i]&&rn<sumd[i+1]) {
				done=1;
				break;
			}
			i=i+1;			
		}
		return i;			
	}
	
	public List<DynamicArticleProperties> convertList(ArrayList<LinkedList<DynamicArticleProperties>> allArticles){
		List<DynamicArticleProperties> da = new ArrayList<DynamicArticleProperties>();
		for(List<DynamicArticleProperties> d : allArticles){
			da.addAll(d);
		}
		return da;
	}
	
	public void printResult(ArrayList<LinkedList<DynamicArticleProperties>> allArticles){
		System.out.println("breaking"+"\t"+"current"+"pcurrent"+"\t"+"initial"+"\t"+"front"+"\t"+"popular"+"\t"+"category"+"\t"+"time"+"\t"+"ID" + "\t"+ "pmp");
		for(DynamicArticleProperties dy : convertList(allArticles)){
			
			System.out.println(dy.getBreakingNews()+"\t"+dy.getCurrentClicks()+"\t"+dy.getPcurrentClicks()+"\t"+dy.getClicks()+
					"\t"+dy.getFrontcat()+"\t"+dy.getPopularMark()+"\t"+dy.getCategory()+"\t"+dy.getCreationTime()+"\t"+dy.getID()+"\t"+dy.getPmostPopular());
		}
	}

	public int getRClicks(ArrayList<DynamicArticleProperties> impa,
			List<DynamicArticleProperties> convertList) {
		int sum = 0;
		for( DynamicArticleProperties dap : impa) {
			for(DynamicArticleProperties dapcheck : convertList) {
				if(dap.getCreationTime() == dapcheck.getCreationTime()) {
					int clicks = (int) (dapcheck.getPcurrentClicks() - dapcheck.getClicks());					
					// also need to be calculated for topN selection
					sum += clicks;
					break;
				}
			}
			
		}
		return sum;
	}
	
	public int getHRClicks(ArrayList<DynamicArticleProperties> impa,
			List<DynamicArticleProperties> convertList) {
		int sum = 0;
		for( DynamicArticleProperties dap : impa) {
			for(DynamicArticleProperties dapcheck : convertList) {
				if(dap.getCreationTime() == dapcheck.getCreationTime()) {
					int clicks = (int) (dapcheck.getCurrentClicks() - dapcheck.getClicks());
					// also need to be calculated for topN selection
					sum += clicks;
					break;
				}
			}
			
		}
		return sum;
	}
	
	public String writeValue(List<DynamicArticleProperties> convertList, boolean initial) {
		
		StringBuilder sb = new StringBuilder();
		for(DynamicArticleProperties dp : convertList) {
			if(initial) {
				sb.append(dp.getClicks() + ", "); 
			} else {
				sb.append((int)dp.getPcurrentClicks() + ", ");  
			}
		}		
		return sb.toString();
	}

	public double accuracyLoss(ArrayList<DynamicArticleProperties> mpa,
			ArrayList<DynamicArticleProperties> pmpa) {
		//System.out.println("mpa : " + mpa.size() + "pmap : " + pmpa.size()); 
		double topN = 0; double probN = 0;
		int n = mpa.size();
		for(int i = 0; i < n; i++) {
			double hCount = mpa.get(i).getCurrentClicks();
			topN += hCount;
			
			double pCount = pmpa.get(i).getPcurrentClicks();
			probN += pCount;
			
		}
		//System.out.println("topN : " + topN + " probN : " + probN); 		
		return ((double)1/(double)n)*(topN/probN);		
	}
	
	public double printSum(List<DynamicArticleProperties> articles) {
		double sum = 0;
		for(DynamicArticleProperties da: articles) {
			sum += da.getCurrentClicks();
		}
		return sum;
	}
	
	public double printPSum(List<DynamicArticleProperties> list) {
		double sum = 0;
		for(DynamicArticleProperties da: list) {
			sum += da.getPcurrentClicks();
		}
		return sum;
	}
	
	public HashMap<String, Double[]> getHashMaps(List<DynamicArticleProperties> list) {
		HashMap<String, Double[]> sample = new HashMap<String, Double[]>();
		
		for(DynamicArticleProperties dnp : list) {
			Double[] data= {(double) dnp.getClicks(), dnp.getPcurrentClicks(), (double) dnp.getCurrentClicks()}; //all kind of counts
			sample.put(dnp.getID(), data);			
			 
		}
		return sample;
		
	}
	
	public void writeCounts(BufferedWriter bw, String data) {
		try {
			bw.write(data); bw.newLine();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
