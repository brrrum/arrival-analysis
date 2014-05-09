package concept.test;

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
	private static final double numberOfReaders = 500;
	private static double[] exponent = { 1, 2, 3};
	static double[] prob  = { 0.6, 0.4};
	
	public static void main(String[] args) {		
		Generator gr = new Generator();
		gr.setSeed(764545);	gr.setSeed(gr.refreshRNG());
		
		List<DynamicArticleProperties> articleList = new LinkedList<DynamicArticleProperties>();
		List<DynamicArticleProperties> initialTimeSort = new LinkedList<DynamicArticleProperties>();
		List<DynamicArticleProperties> countSort = new ArrayList<DynamicArticleProperties>();
		
		ProbArrival arrivals = new ProbArrival(gr, (int) numberOfReaders, prob);
		arrivals.seedArticles(articleList, initialTimeSort, countSort);
		arrivals.updateArticles(countSort, exponent[0]); 
		
		
	}
	
	

}
