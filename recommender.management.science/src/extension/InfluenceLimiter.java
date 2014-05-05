package extension;

public class InfluenceLimiter 
{
	public double discount(double RevCount, int Itr, double reading_prob, double exponent)
	{
		double ureputation; // = 3.9;
		// reputation score should not fall below 3
		if(RevCount>=1)
		{
			double reputation;
			reputation = new Double((Itr+1)*reading_prob/(10*RevCount)).doubleValue();
			//modified exponent
			ureputation = exponent*Math.min(1, reputation);								
		}
		else
			ureputation = exponent;
		//System.out.println(ureputation);
		return ureputation;			
	}	

	public double psuccess(String article, double pappear, String[] pappeared)
	{
		int i=0;
		while (i<10)
		{
			if(article.equals(pappeared[i].toString()))
			{
				pappear = pappear + 1;
				break;
			}
			i=i+1;		
		}
		return pappear;
	}
}
