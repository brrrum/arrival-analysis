package extension;

import java.util.Random;

public class ReadPattern 
{
    public int readPattern(int n)
    {
	double[] d1 = new double[n];
	double[] d2 = new double[n];
	double[] sumd = new double[n+1];
	sumd[0] = -1;

	for(int j=0; j<n; j++)
	{
	    d2[j] = j+1;
	}

	double temp;
	for(int j=1; j<=n-1; j++)
	{
	    temp = d2[j-1];
	    d2[j] = d2[j]+temp;

	}

	for(int i=0; i<n; i++)
	{
	    d1[i]= (n-i)/d2[n-1];
	    if(i>0)
	    {
		temp = d1[i-1];
		d1[i] = d1[i]+temp;

	    }
	    sumd[i+1] = d1[i];
	}
	//selection of articles
	Random random = new Random();
	double rn = random.nextDouble();
	int done = 0;
	int i = 0;
	while(done==0)
	{
	    if(rn>sumd[i]&&rn<sumd[i+1])
	    {
		done=1;
		break;
	    }
	    i=i+1;
	}
	return i;

    }

}
