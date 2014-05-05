package concept.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App{
	
	private static final int NTHREDS = 1;
	
	public static void main(String[] args) {
		
		long initial = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);		
		List<Future<List<String>>> list = new ArrayList<Future<List<String>>>(); 
		for (int i = 0; i < 200; i++) {
			Future<List<String>> submit = executor.submit(new App1());
			list.add(submit);
		}		
		
		for(Future<List<String>> future : list) {
			try {
				System.out.println("elements are: " + future.get());
			} catch (InterruptedException | ExecutionException e) {
				
				System.out.println(e); 
			}
		}		
		executor.shutdown();
		long end = System.currentTimeMillis();		
		System.out.println("time : " + (end - initial));
	}
	
	

}
