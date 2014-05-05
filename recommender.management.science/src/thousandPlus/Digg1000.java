package thousandPlus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import org.jfree.data.time.TimeSeries;
import org.jfree.ui.ApplicationFrame;

import Digg.diggRecord;

public class Digg1000 extends ApplicationFrame
{
    private String filepath = "C" + ":" + File.separator
	    + "academic" + File.separator + "recommender system 2" + File.separator
	    + "data" + File.separator + "digg_votes" 
	    + File.separator;
    private ArrayList<diggRecord> data = new ArrayList<diggRecord>(); // temporary arraylist to hold
    private ArrayList<diggRecord> markedData = new ArrayList<diggRecord>();
    private ArrayList<TimeSeries> subdata = new ArrayList<TimeSeries>();
    TreeSet<String> tree = new TreeSet<String>();

    public Digg1000() 
    {
	super("1000+");	
	votePlot();
	prepareData();
    }

    private void votePlot() 
    {
	try
	{
	    BufferedReader in = new BufferedReader(new FileReader(filepath + "digg_votes1.csv"));	    
	    String line = in.readLine(); // first line of data
	    System.out.println(line);
	    String timestamp;
	    String voterid;
	    String articleid;
	    int ccount = 1;
	    while(line != null)
	    {
		if(line != null)
		{		    
		    int a1 = 0;
		    int b1 = line.indexOf(",", a1);		    
		    timestamp = line.substring(a1+1, b1-1);		    
		    
		    a1 = b1 + 1;
		    b1 = line.indexOf(",", a1);
		    voterid = line.substring(a1+1, b1-1);		    
		    
		    a1 = b1 + 1;
		    String temp = line.substring(a1);
		    articleid = temp.substring(1, temp.length() - 1);		    
		    //System.out.println(timestamp + ","+voterid+","+articleid);
		    diggRecord diggrecord = new diggRecord(articleid);
		    diggrecord.setTime(timestamp);
		    
		    line = in.readLine();
		    
		    if(line == null)
		    {
			break;
		    }
		    int a = 0;
		    int b = line.indexOf(",", a);
		    a = b + 1;
		    b = line.indexOf(",", a);
		    a = b + 1;
		    String nextid = line.substring(a+1, line.length() - 1);
		    //System.out.println(nextid);
		    if(articleid.compareTo(nextid) == 0)
		    {		
			diggrecord.setcumcount(ccount);
			data.add(diggrecord);
			ccount++;
		    }
		    else
		    {
			// copying more than 1000 votes			
			if(ccount > 2000)
			{
			    ArrayList<Double> times = new ArrayList<Double>();			    
			    String id = new String();
			    for(diggRecord dr: data)
			    {
				//System.out.println(dr.getTime());
				times.add(Double.parseDouble(dr.getTime()));
				id = dr.getId();
			    }
			    
			    Collections.sort(times);
			    data.clear();
			    
			    for(Double ts: times)
			    {
				diggRecord diggrecord1 = new diggRecord(id);
				diggrecord1.setTime(ts.toString()); 
				data.add(diggrecord1);
			    }
			    
			    markedData.addAll(data);
			    tree.add(id);
			    //System.out.println(id);
			}			
			ccount = 1;
			data.clear();
		    }
		}
	    }
	    in.close();	    
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}
	
    }

    private void prepareData() 
    {
	ArrayList<String> ids = new ArrayList<String>(tree);
	try
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(filepath + "digg_2000p.csv"));
	    for(int i = 0; i < ids.size(); i++)
	    {
		String beginTime = null;
		for(diggRecord dg: markedData)
		{
		    if(ids.get(i).compareTo(dg.getId()) == 0)
		    {
			beginTime = dg.getTime();
			break;
		    }		    
		}

		for(diggRecord dg : markedData)
		{
		    if(ids.get(i).compareTo(dg.getId()) == 0)
		    {			

			out.write(dg.getId() + "," + dg.getTime() + "," + beginTime + "\n");

		    }

		}
	    }
	    out.close();
	    System.out.println("data created"); 
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}
	
	
    }
    

}
