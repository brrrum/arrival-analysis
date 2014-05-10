package concept.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import arrivalUtilities.BasicUtilities;
import revision13.DynamicArticleProperties;
import umontreal.iro.lecuyer.probdist.InverseGaussianDist;

public class ProbUpdate {
	
	private static InverseGaussianDist inv = new InverseGaussianDist(2.16, 2);
	private int seed;
	Random rand;
	private BasicUtilities bsu = new BasicUtilities();
	private DynamicArticleProperties pdp;
	private int narticles;
	private static int totalCount;

	public ProbUpdate(int seed) {
		this.seed = seed;
	}

	public void frontPageSelection(double[] prob, ArrayList<DynamicArticleProperties> mpa,
			LinkedList<DynamicArticleProperties> fda, ArrayList<LinkedList<DynamicArticleProperties>> allArticles,
			ArrayList<DynamicArticleProperties> pmpa) {
		
		rand = new Random(seed);
		//narticles = (int) Math.floor(inv.inverseF(rand.nextDouble())+1);
		narticles = 1;
		totalCount += narticles;
		if( narticles > 15) {
			narticles = 15;
		}
		
		for (int i = 0; i < narticles; i++) {
			double sp = rand.nextDouble();
			seed = rand.nextInt();
			if(sp <= prob[0]){ 		
				DynamicArticleProperties dp = updateCount(mpa, seed, true, 10, false, allArticles, pmpa);
				updateInAll(dp, allArticles);				
			} else if((sp > prob[0]) & (sp <= prob[0] + prob[1])) {				
				DynamicArticleProperties dp = updateCount(fda, seed, false, 20, false, allArticles, pmpa);
				updateInAll(dp, allArticles);
			} else {				
				updateCount(fda, seed, false, 5, true, allArticles, pmpa); 
			}
		}
	}
	
	public DynamicArticleProperties updateCount(List<DynamicArticleProperties> dap, int seed, boolean mp, int displayed, 
			boolean categorized, ArrayList<LinkedList<DynamicArticleProperties>> allArticles, ArrayList<DynamicArticleProperties> pmpa) {
		
		DynamicArticleProperties dp = null;
		Random random = new Random(seed);
		if(mp) {			
			int index = random.nextInt(displayed);
			dp = dap.get(index);
			pdp = pmpa.get(index);	
			
			double pCount = pdp.getPcurrentClicks() + 1;
			pdp.setPcurrentClicks(pCount); 
		}  else if(categorized) {			
			int catindex = random.nextInt(BasicUtilities.getCategories().size());
			LinkedList<DynamicArticleProperties> scat = allArticles.get(catindex);
			int index = bsu.readPattern(displayed, seed);
			dp = scat.get(index);			
			} else {
				int index = bsu.readPattern(displayed, seed); 
				dp = dap.get(index);
				
			}
		int count = dp.getCurrentClicks() + 1;		
		dp.setCurrentClicks(count);		
		return dp;		
	}
	
public void updateInAll(DynamicArticleProperties dp, ArrayList<LinkedList<DynamicArticleProperties>> allArticles) {
		
		int catindex = catnum(dp.getCategory()); 		
		LinkedList<DynamicArticleProperties> updating = allArticles.get(catindex);
		for(int j = 0; j < updating.size(); j++){
			if(dp.getID().equalsIgnoreCase(updating.get(j).getID())){
				updating.get(j).setCurrentClicks(dp.getCurrentClicks()); 
			}
		}// Now for probabilistic
		
		if(pdp != null) {
			int pCatindex = catnum(pdp.getCategory());		
			LinkedList<DynamicArticleProperties> pUpdating = allArticles.get(pCatindex);
			for(int j = 0; j < pUpdating.size(); j++) {
				if(pdp.getID().equalsIgnoreCase(pUpdating.get(j).getID())) {
					pUpdating.get(j).setPcurrentClicks(pdp.getPcurrentClicks()); 
				}
			}
		}
		
	}

	private int catnum(String category) {
		int catindex = 0;
		ArrayList<String> categories = BasicUtilities.getCategories();	
		for (int i = 0; i < categories.size(); i++) {
			if(category.equalsIgnoreCase(categories.get(i))) {
				catindex = i;
				break;
			}
		}
		return catindex;
	}
	
	public void printTotal() {
		System.out.println("totalCount : " + totalCount);
	}

	

}
