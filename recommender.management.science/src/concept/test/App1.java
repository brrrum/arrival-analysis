package concept.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class App1 implements Callable<List<String>> { 

	@Override	
	public List<String> call() throws Exception {
		List<String> names = new ArrayList<String>();
		names.add("prank");
		names.add("shankar");
		names.add("Bill");
		Thread.sleep(100); 
		return names;
	}

}
