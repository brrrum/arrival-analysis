package extension;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Exponent 
{
	FileWriter hardwriter;
	private projectVariables variables = new projectVariables();   
	Double[] expcounts = new Double[200];
	Double[] nexpcounts = new Double[200];
	Double[] rexpcounts = new Double[200];
	Double[] hShare = new Double[200];
	double ncount;
	double[] clickcounts = new double[10];
	double ratio;
	double[] amodified = new double[200];
	String trackarticle[] = new String[200];
	InfluenceLimiter influence = new InfluenceLimiter();
	int found;
	int inspection_in = 2000;
	double modr;
	private AdditionalReader additionalreader = new AdditionalReader();  
	private newCodes counts = new newCodes();

	public void makecsv(String[] abp)
	{
		String path = variables.SOURCE_PATH;
		try 
		{
			hardwriter = new FileWriter(path+"hcount.csv");
			for(int j = 0; j < abp.length; j++)
			{
				hardwriter.append(abp[j]);
				hardwriter.append(',');
			}	    
			hardwriter.append('\n');
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
			hardwriter.append('\n');
			hardwriter.flush();
			hardwriter.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

	public void setValue()
	{
		String istr;
		for (int i = 0; i < 200; i++) 
		{
			istr = Integer.toString(i + 1);
			trackarticle[i] = "A".concat(istr);

		}
	}

	public Double[] feedBack(int index, double reading_prob, int pitr, double[] revclick, double a, double b, String[] article, Double[] count)
	{
		setValue();
		for(int i=0; i<amodified.length; i++)
		{
			amodified[i]=a;

		}
		// if pitr is greater than index maintain current reputation
		// influence limiter for the indexed article

		// commented the section below to run simulation without influence limiter algorithm.

		/*
	if(pitr<=inspection_in)
	{
	    amodified[index] = influence.discount(revclick[index], pitr, reading_prob, a);
	    //modr = influence.discount(revclick[index], pitr, reading_prob, a);
	    modr = amodified[index];
	}
	else
	{
	    amodified[index] = modr;// final modified exponent
	    //System.out.println(amodified[index]);
	}
		 */
		if((pitr%400) == 0)
		{
			System.out.println(amodified[index]);
		}

		int donep = 0;
		for(int i=0; i<count.length;i++) 
		{
			int i1 = 0;
			double power;
			while (donep == 0)
			{
				if (trackarticle[i].equals(article[i1].toString())) 
				{
					donep = 1;
					power = amodified[i];
					ncount = count[i1]/b;
					// expcounts[i] = Math.pow(count[i], a);
					//System.out.println(Math.pow(ncount, power));
					expcounts[i] = Math.pow(ncount, power);

				}
				i1 = i1 + 1;
			}
			donep = 0;

		}	

		// to maintain the order of remaining computation.
		int donep1 = 0;
		for(int i=0; i<count.length;i++) 
		{
			int i1 = 0;
			while (donep1 == 0)
			{
				if (article[i].equals(trackarticle[i1].toString())) 
				{
					donep1 = 1;
					nexpcounts[i]=expcounts[i1];					
				}
				i1 = i1 + 1;
			}
			donep1 = 0;			
		}	
		return nexpcounts;		
	}

	public Double[] revFeedBack(Double[] modcount, String[] indices, double a)
	{
		// code for getting the original count in relevant order
		int done = 0;
		for(int i=0; i<trackarticle.length; i++)
		{
			int j = 0;
			while (done == 0)
			{
				if(indices[i].equals(trackarticle[j].toString()))
				{
					done = 1;
					rexpcounts[i]=Math.pow(modcount[i], 1/amodified[j]);
					//rexpcounts[i]=Math.pow(modcount[i], 1/a);
				}
				j=j+1;
			}			
			done = 0;
		}		
		return rexpcounts;
	}

	//sorting of articles
	public void count_sort(Double[] count, int n, String b[])
	{
		int temp;

		for(int i=0; i<n-1; i++)
		{
			for(int j=0; j<n-1-i; j++)
			{
				if(count[j]<count[j+1])
				{
					temp = count[j+1].intValue();
					count[j+1] = count[j];
					count[j] = (double) temp;
					// transforming corresponding articles
					String switchId = b[j+1];
					b[j+1] = b[j];
					b[j] = switchId;
				}			

			}
		}
	}

	public double[] count_selection(Double[] a, int m, String art[], double reading_prob, String abp[]) throws IOException
	{
		double[] sum = new double[m];
		String[] b = new String[m];	

		for(int i=0; i<m; i++)
		{	    
			b[i]=abp[i];	    

		}

		double U = Math.random();
		if(U < reading_prob)
		{
			int index = counts.readerIndex(variables.randomModel, m);			
			a[index] = a[index]+1;
			
			int donep =0; 
			int j=0;
			while(donep==0 & j <b.length)
			{
				if(art[index].equals(b[j]))
				{
					donep=1;
					clickcounts[j] = clickcounts[j]+1; 
				}
				j = j+1;
			}
			donep=0;

		}
		else
		{
			int ZipfIndex = additionalreader.ZipfReader(m, art);
			a[ZipfIndex] = a[ZipfIndex] + 1;
			// int randomIndex = generator.nextInt(a.length-m)+m;
			// a[randomIndex] = a[randomIndex]+1;    

			int donep = 0;
			int j=0;
			while(donep==0 & j <b.length)
			{
				if(art[ZipfIndex].equals(b[j]))
				{
					donep=1;
					clickcounts[j] = clickcounts[j]+1;

				}
				j = j+1;
			}
			donep=0;
		}

		count_sort(a,a.length,art);
		// think of implementing Zipf distribution
		
		double total = 0;
		for(int i = 0; i < abp.length; i++)
		{
			total += a[i];
		}
		//System.out.println(total);

		for(int i = 0; i < abp.length; i++)
		{
			boolean done = true;
			int jj = 0;
			while(done)
			{
				if(abp[i].equals(art[jj]))
				{
					done = false;
					hShare[i] = a[jj];
					//hardwriter.append(Double.toString(a[jj]/total));
					hardwriter.append(Double.toString(hShare[i])); 
					hardwriter.append(',');		    
				}
				jj++;
			}
		}
		hardwriter.append('\n');

		double nu = new Double(a[m-1]);
		double du = new Double(a[m]);
		ratio = Math.log(nu/du); // the measures M1 & M2 can be obtained here.
		//track the top-10 articles

		int donep =0; 
		for(int i=0; i<m; i++)
		{
			int j=0;
			while(donep==0)
			{
				if(b[i].equals(art[j]))
				{
					donep=1;
					sum[i] = a[j];					
				}
				j = j+1;
			}
			donep=0;
		}		

		/*for(int i=0; i<m; i++)
		{
			System.out.print(clickcounts[i] + " ");

		}
		System.out.println();*/
		return sum;			

	}

	public void fileupdate(Double[] a, String[] art, String[] abp) throws IOException
	{
		double sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		
		for(int i = 0; i < abp.length; i++)
		{
			boolean done = true;
			int j = 0;
			while(done)
			{
				if(abp[i].equals(art[j]))
				{
					done = false;
					hardwriter.append(Double.toString(a[j])); 
					hardwriter.append(',');		    
				}
				j++;
			}
		}
		hardwriter.append('\n');
	}

}











