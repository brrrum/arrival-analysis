package concept.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;

import arrivalUtilities.BasicUtilities;
import revision13.Generator;
import umontreal.iro.lecuyer.probdist.InverseGaussianDist;

public class Todelete {	

	public static void main(String[] args) {
		Generator gr = new Generator();
		gr.setSeed(764545);
		gr.setSeed(gr.refreshRNG());
		InverseGaussianDist inv = new InverseGaussianDist(4, 3); 
		
		Todelet1 arrivals = new Todelet1(gr);
		ArrayList<String> ids = new ArrayList<String>();
		
		arrivals.showRefreshRNG(7000); 

	}
	
	public static void rest() {
		
	}

}

class Todelet1 {
	
	private Generator gr;
	private Random rand = new Random(7000);
	
	public Todelet1(Generator rng) {
		this.gr = rng;
	}
	
	public void showSeeds() {
		for (int i = 0; i < 1000; i++) {
			gr.setSeed(gr.refreshRNG());
			PoissonDistribution psd = new PoissonDistribution(gr, 0.002, PoissonDistribution.DEFAULT_EPSILON, PoissonDistribution.DEFAULT_MAX_ITERATIONS);
			//ZipfDistribution zpf = new ZipfDistribution(gr, 1000, 1.4);
			int count = psd.sample();
			System.out.println(count); 
		}
	}
	
	public void showRandom() {
		for(int i = 0; i < 10; i++) {
			int catindex = rand.nextInt(BasicUtilities.getCategories().size());
			System.out.println(catindex); 
		}
	}
	
	public void showRefreshRNG(int seed) {
		Random rand = new Random(seed);
		for(int i = 0; i < 10; i++) {
			System.out.println(rand.nextDouble());
		}		
	}
}
