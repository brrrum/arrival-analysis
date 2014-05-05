package revision13;

import java.util.Random;

import umontreal.iro.lecuyer.probdist.InverseGaussianDist;
import extension.copy.CExponent;

public class frontPageAndUpcoming extends CExponent {
	    
	private static InverseGaussianDist igt = new InverseGaussianDist(inMu, inLambda);
	private static int seedvalue;
	private static Random rand;
	
	public frontPageAndUpcoming(int seed) {
		this.seedvalue = seed;
		rand = new Random(seedvalue);
	}
	
	public int numberOfArticles() {
		double u = rand.nextDouble();
		float sample = (float) igt.inverseF(u);
		int toberead = Math.round(sample);
		return toberead;
	}
	
	

}
