package extension;

import java.io.FileWriter;
import java.io.IOException;

public class giniInequality 
{
	FileWriter giniwriter;
	projectVariables variables = new projectVariables();


	public void makecsv()
	{
		String path = variables.SOURCE_PATH;
		try 
		{
			giniwriter = new FileWriter(path+"gini.csv");
			giniwriter.append("hard");
			giniwriter.append(',');	    	    
			giniwriter.append("prob");
			giniwriter.append('\n');
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

	public void closefile()
	{
		try
		{
			giniwriter.append('\n');
			giniwriter.flush();
			giniwriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

	public void countPass(Double[] probCount, Double[] hardCount)
	{
		/*

		try 
		{
			giniwriter.append(new Double(writeGini(hardCount)).toString());
			giniwriter.append(',');
			giniwriter.append(new Double(writeGini(probCount)).toString());
			giniwriter.append('\n');

		} 
		catch (IOException e) 
		{	    
			e.printStackTrace();
		}
		*/
	}

	public double writeGini(Double[] count)
	{
		Double[] giniContainer = new Double[count.length];
		double sum = 0;
		for(int i = 0; i < count.length; i++)
		{
			sum += count[i];	    
		}

		for(int i = 0; i < count.length; i++)
		{
			giniContainer[i] = count[i]/sum;
		}

		double total = 0;	
		for(int i = 0; i < count.length; i++)
		{

			double rowsum = 0;
			for(int j = 0; j < count.length; j++)
			{
				rowsum = rowsum + Math.abs(giniContainer[i]-giniContainer[j]);
			}	    
			total += rowsum;
		}
		return total /= (2*count.length);
	}

}
