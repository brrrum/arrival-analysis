package concept.test;

import java.util.ArrayList;
import java.util.LinkedList;
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

	public ProbUpdate(int refreshRNG) {
		this.seed = seed;
	}

	public void frontPageSelection(double[] prob, ArrayList<DynamicArticleProperties> mpa,
			LinkedList<DynamicArticleProperties> fda, ArrayList<LinkedList<DynamicArticleProperties>> allArticles,
			ArrayList<DynamicArticleProperties> pmpa) {
		
		rand = new Random(seed);
		narticles = (int) Math.floor(inv.inverseF(rand.nextDouble())+1);
		totalCount += narticles;
		if( narticles > 15) {
			narticles = 15;
		}
		
		for (int i = 0; i < narticles; i++) {
			double sp = rand.nextDouble();
			if(sp <= prob[0]){ 		
				
			} else if((sp > prob[0]) & (sp <= prob[0] + prob[1])) {
				
			} else {
				
			}
		}
	}

	

}
