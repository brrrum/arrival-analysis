package Digg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.TreeSet;

import org.jfree.data.time.TimeSeries;
import org.jfree.ui.ApplicationFrame;

public class coloradoPlot extends ApplicationFrame
{
    private static String FILE_PATH = 
	    "C" + ":" + File.separator + "academic" + File.separator+"recommender system 2"+File.separator
	    +"Management Science"+File.separator + "colorado-clicks" + File.separator;
    private ArrayList<diggRecord> data = new ArrayList<diggRecord>();
    private ArrayList<diggRecord> markedData = new ArrayList<diggRecord>();
    private ArrayList<TimeSeries> subdata = new ArrayList<TimeSeries>();

    TreeSet<String> tree = new TreeSet<String>();

    public coloradoPlot(String title) 
    {
	super(title);
	prepareData();
	timeNormal();
	
    }

    private void timeNormal() 
    {
	ArrayList<String> ids = new ArrayList<String>(tree);
	try
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(FILE_PATH + "col_clicksnormal.csv"));
	    int test = 0;
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
			if(dg.getTime().compareTo(beginTime) == 0)
			{			   
			    continue;
			    //System.out.println(cdate);
			}
			else
			{
			    test++;
			    int adjustcount = dg.getcumcount() - 1;
			    out.write(dg.getId() + "," + dg.getTime() + "," + beginTime + "," + adjustcount + "\n");			    
			}
		    }

		}
	    }
	    System.out.println(test);
	    out.close();
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}
	
    }

    private void prepareData() 
    {
	try
	{
	    BufferedReader in = new BufferedReader(new FileReader(FILE_PATH + "cumcounts_col.csv"));
	    //BufferedReader in = new BufferedReader(new FileReader(FILE_PATH + "conn_clickplot.csv"));
	    String line = in.readLine(); // first line of data.
	    int count = 1;
	    while(line != null)
	    {
		if(line != null)
		{
		    //System.out.println(line);
		    int a = 0;
		    int b = line.indexOf(",", a);
		    String articleid = line.substring(a, b);

		    a = b + 1;
		    b = line.indexOf(",", a);
		    String timestamp = line.substring(a, b);		    

		    line = in.readLine();
		    if( line == null)
		    {
			break;
		    }
		    // to get cumulative counts
		    String nextid = line.substring(0, line.indexOf(",", 0)); 
		    if(articleid.compareTo(nextid) == 0)
		    {			
			diggRecord conrecord = new diggRecord(articleid);
			conrecord.setTime(timestamp);
			conrecord.setcumcount(count);
			count++;
			data.add(conrecord);			
		    }
		    else
		    {
			if(count > 100)
			{
			    markedData.addAll(data);
			    tree.add(articleid);			    
			}
			count = 1;
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

}
