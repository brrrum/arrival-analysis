package Digg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class timeSort 
{
    private String filepath = "C" + ":" + File.separator
	    + "academic" + File.separator + "recommender system 2" + File.separator
	    + "Management Science" + File.separator + "digg_votes" + 
	    File.separator;
    
    private ArrayList<String> header = new ArrayList<String>();
    private ArrayList<diggRecord> data = new ArrayList<diggRecord>();
    private int[] indices = {19, 25, 77, 149, 126, 100, 84, 27, 136, 85};
    
    public timeSort()
    {
	getsorted();
	
    }

    private void getsorted() 
    {
	try
	{
	    BufferedReader in = new BufferedReader(new FileReader(filepath + "digg_votes1sorted.csv"));
	    String line = in.readLine();
	    int a = 0;
	    int b = line.indexOf(",", a);
	    String articleid = line.substring(a, b);
	    header.add(articleid);
	    
	    a = b + 1;
	    String timestamp = line.substring(a);
	    header.add(timestamp);
	    
	    line = in.readLine(); // first line of data
	    while(line != null)
	    {
		if(line != null)
		{
		    int a1 = 0;
		    int b1 = line.indexOf(",", a1);
		    //System.out.println(b1);
		    articleid = line.substring(a1, b1);
		    
		    a1 = b1 + 1;
		    timestamp = line.substring(a1);
		    
		    diggRecord diggrecord = new diggRecord(articleid);
		    diggrecord.setTime(timestamp);
		    data.add(diggrecord);
		    line = in.readLine();		    	    
		    
		}
	    }
	    in.close();
	    
	    int[] ids = new int[174];
	    BufferedWriter out = new BufferedWriter(new FileWriter(filepath + "digg_votesnormal.csv"));
	    for(int i = 0; i < ids.length; i++)
	    {
		ids[i] = i+1;
		String beginTime = null;
		for(diggRecord dr: data)
		{
		    if(Integer.toString(ids[i]).compareTo(dr.getId()) == 0)
		    {
			beginTime = dr.getTime();
			break;
		    }
		}

		for(diggRecord dr: data)
		{
		    if(Integer.toString(ids[i]).compareTo(dr.getId()) == 0)
		    {
			out.write(dr.getId() + "," + dr.getTime() + "," + beginTime + "\n");

		    }
		}

	    }
	    out.close();
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}	
    }    
   
}
