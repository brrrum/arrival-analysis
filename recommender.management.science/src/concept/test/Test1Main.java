package concept.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
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

import revision13.DynamicArticleProperties;
import revision13.frontPageAndUpcoming;
import umontreal.iro.lecuyer.probdist.InverseGaussianDist;

public class Test1Main {	
	
	public static void main(String[] args) {
		InverseGaussianDist inv = new InverseGaussianDist(4, 3);
		Random rand = new Random();
		
		for(int i = 0; i < 100; i++) {
			System.out.println(Math.floor(inv.inverseF(rand.nextDouble())+1));  
		}
		
	}
	
	

}
