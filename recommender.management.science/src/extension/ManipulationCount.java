package extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class ManipulationCount 
{
	AdditionalReader additionalReader = new AdditionalReader();
	private Exponent exponent = new Exponent();

	public int[] manipulated(int range, int copies) 
	{
		int[] anArray = new int[copies];
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=0; i<range; i++)
		{
			numbers.add(i+1);
		}
		Collections.shuffle(numbers);
		for(int i=0; i<copies; i++)
		{
			//System.out.println(numbers.get(i));
			anArray[i]=numbers.get(i);
		}

		Arrays.sort(anArray);
		return anArray;
	}

	public void match(String[] article, int index, Double[] dccx, double[] newclicks) 
	{
		exponent.setValue();
		int i = 0;
		int found = 0;
		while (found == 0) 
		{
			if (article[i].equals(exponent.trackarticle[index].toString())) 
			{
				found = 1;				
				newclicks[index] = newclicks[index] + 1; // newclicks are in initial orders
				//double discount = modifiedCount(newclicks[index], pitr, reading_prob);
				double incount = 1;
				dccx[i] = dccx[i] + incount; // this is updated one.
			}
			i = i + 1;
		}		

	}
	
	public void simpleMatch(String[] article, int index, Double[] dccx, double[] newclicks, int pitr, double reading_prob)
	{
		exponent.setValue();
		int i = 0, found = 0;
		while(found == 0)
		{
			if(article[i].equals(exponent.trackarticle[index].toString()))
			{
				found = 1;
				//newclicks
			}
		}
	}
	
	public double modifiedCount(double RevCount, int Itr, double reading_prob)
	{
		double mcount;
		if(RevCount >= 1)
		{
			double reputation;
			reputation = (Itr + 1)*reading_prob/(10*RevCount);
			mcount = RevCount*Math.min(1, reputation);
		}
		else
			mcount = RevCount;
		return mcount;
	}

	public void read(double reading_prob, int ps, Double[] cc, String[] abp, String[] dd, double[] newclicks)
	{
		projectVariables variables = new projectVariables();
		newCodes codes = new newCodes();
		double pu = Math.random();		
		if (pu < reading_prob) 
		{
			int index = codes.readerIndex(variables.randomModel, ps);			
			cc[index] = cc[index] + 1;
			
			// for new clicks
			int dow = 0;
			int do1 = 0;
			while (dow == 0) 
			{
				if (dd[index].equals(abp[do1].toString())) // make change, original articles
				{
					dow = 1;
					newclicks[do1] = newclicks[do1] + 1;			
				}
				do1 = do1 + 1;
			}		
		} 
		else 
		{
			//int prandomIndex = pGenerator.nextInt(abp.length - ps)+ ps;
			int ZipfIndex = additionalReader.ZipfReader(ps, abp);
			cc[ZipfIndex] = cc[ZipfIndex] + 1;
			//cc[prandomIndex] = cc[prandomIndex] + 1;
			int dow = 0;
			int do1 = 0;
			while (dow == 0) 
			{
				if (dd[ZipfIndex].equals(abp[do1].toString())) // make, dd original articles
					// change
				{
					dow = 1;
					newclicks[do1] = newclicks[do1] + 1;
				}
				do1 = do1 + 1;
			}
		}
	}	
	
}

