package extension.copy;

import java.io.File;

public interface CProjectVariables {
	
	String PROJECT_PATH = "C" + ":" + File.separator + "RepastSimphony-2.1" + File.separator + "workspace" 
	+ File.separator + "recommender.management.science" + File.separator;
	
	public static final int na = 200;
	public static final int np = 10;
	public static final int range = 100;
	public static final int itr = 10;
	public static final int index = 10;
	 
	 final double ZEXPONENT = 1.4;
	 final double webExponent = 5/7; // for exponent value = 2.4
	 public boolean randomModel = true;
	 static int inspection_in = 2000;
	 
	 // inverse guass distribution
	 public static double inMu = 3;
     public static double inLambda = 6.3;
     // new Constants
     public static final int icategoryCount = 50; // initial number of articles in each category

}
