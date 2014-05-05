package extension;

import java.io.FileWriter;
import java.io.IOException;

public class CountInfluence 
{
	private newCodes counts = new newCodes();
	private projectVariables variables = new projectVariables();
	private AdditionalReader additionalreader = new AdditionalReader();
	private Exponent exponent = new Exponent();
	private FileWriter iwriter;
	
	public double discount(double RevCount, int Itr, double reading_prob)
	{
		double ureputation; 
		if(RevCount>=2)
		{
			double reputation;
			reputation = new Double((Itr+1)*reading_prob/(10*(RevCount-1))).doubleValue();
			//modified exponent
			ureputation = Math.min(1, reputation);								
		}
		else
			ureputation = 1;
		//System.out.println(ureputation);
		return ureputation;			
	}
	
	public void discountMatch(String[] article, int index, Double[] dccx, double[] newclicks, int pitr, double rp)
		throws IOException
	{
		exponent.setValue();
		int i = 0;
		int found = 0;
		while (found == 0) 
		{
			if (article[i].equals(exponent.trackarticle[index].toString()))
			{
				found = 1;
				newclicks[index] = newclicks[index] + 1;
				double disc = discount(newclicks[index], pitr, rp);
				double incount = disc*1;
				dccx[i] = dccx[i] + incount;
			}
			i = i + 1;
		}
		exponent.count_sort(dccx, dccx.length, article);

		for(int i1 = 0; i1 < dccx.length; i1++)
		{
			boolean done = true;
			int j = 0;
			while(done)
			{
				if(exponent.trackarticle[i1].equals(article[j]))
				{
					done =  false;
					iwriter.append(Double.toString(dccx[j]));
					iwriter.append(',');										
				}
				j++;
			}
		}
		iwriter.append('\n');
	}
	
	public void count_iselection(int pitr, double[] newClicks, Double[] a, int m, String art[], double reading_prob, String abp[])
			throws IOException
	{
		//update newClicks
		double U = Math.random();
		if(U < reading_prob)
		{
			int index = counts.readerIndex(variables.randomModel, m);
			int found = findArticles(art[index], abp);
			newClicks[found] = newClicks[found] + 1;
			
			double reputation = discount(newClicks[found], pitr, reading_prob);
			double count = 1*reputation;
			a[index] = a[index]+count;	
			//System.out.println(a[index] +"," + art[index] + "," + found);
		}
		else
		{
			int ZipfIndex = additionalreader.ZipfReader(m, art);
			int found = findArticles(art[ZipfIndex], abp);
			newClicks[found] = newClicks[found] + 1;
			
			double reputation = discount(newClicks[found], pitr, reading_prob);
			double count = 1*reputation;
			a[ZipfIndex] = a[ZipfIndex]+count;
			//System.out.println(a[ZipfIndex] +"," + art[ZipfIndex] + "," + newClicks[found]);
		}
		exponent.count_sort(a, a.length, art);
		
		for(int i = 0; i < abp.length; i++)
		{
			boolean done = true;
			int j = 0;
			while(done)
			{
				if(abp[i].equals(art[j]))
				{
					done =  false;					
					iwriter.append(Double.toString(a[j]));
					iwriter.append(',');					
				}
				j++;
			}
		}
		iwriter.append('\n');		
	}
	
	public int findArticles(String article, String[] abp)
	{
		boolean done = true;
		int j = 0, index = 0;
		while(done)
		{
			if(article.equals(abp[j]))
			{
				done = false;
				index = j;
			}
			j++;
		}
		return index; 
	}
	
	public void makeFile(String[] abp)
	{
		String path = variables.SOURCE_PATH;
		try 
		{
			iwriter = new FileWriter(path+"hcount1.csv");
			for(int j = 0; j < abp.length; j++)
			{
				iwriter.append(abp[j]);
				iwriter.append(',');
			}	    
			iwriter.append('\n');
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void closeit()
	{
		try {
			iwriter.flush();
			iwriter.close();			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
