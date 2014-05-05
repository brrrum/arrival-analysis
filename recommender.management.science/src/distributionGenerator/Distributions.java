package distributionGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.math3.distribution.ZipfDistribution;

public class Distributions 
{
    private static FileWriter dataGenerator;
    private static double sampledata;
           
   // all distributions in a same file
    public void d_Distributions(int size, int numberofElements, double exponent, double mean, double variance, String path) throws IOException
    {
	try
	{
	    dataGenerator = new FileWriter(path + File.separator + "distrbutions.csv");
		BufferedWriter out = new BufferedWriter(dataGenerator);
		out.write("Zipf\n");
		
		for(int i = 0; i < size; i++)
		{
		    /* out.write(Math.rint(new NormalDistribution(mean,variance).sample()) + ", " + 
			    new ZipfDistribution(numberofElements,exponent).sample() + ", " + 
			    Math.floor(new LogNormalDistribution(mean,variance).sample()) + "\n");
			   // Math.floor(Math.exp(new LogNormalDistribution(mean,variance).sample())) + "\n");*/
		    out.write(new ZipfDistribution(numberofElements,exponent).sample() + "\n");
		}
		out.close();
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
		
    }
    
}
