package DailyMePlot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Plotcsv extends ApplicationFrame
{
    private String FILE_PATH = 
	    "C" + ":" + File.separator + "academic" + File.separator+"recommender system 2"+File.separator+
	    "Management Science"+File.separator;
    private ArrayList<TimeSeries> serieslist = new ArrayList<TimeSeries>();
    TreeSet<String> tree = new TreeSet<String>();
    
    public Plotcsv(String title)
    {
	super(title);
	repetition();
    }
        
    public void makePlot(int j)
    {
	try
	{
	        TimeSeries series = new TimeSeries("per minute");		
		BufferedReader in = new BufferedReader(new FileReader(FILE_PATH + "id_ny.csv"));
		BufferedReader inc = new BufferedReader(new FileReader(FILE_PATH + "cumcounts_ny.csv"));		
		String line = in.readLine();		
		String linec = inc.readLine();				
		String id, time, count;		
		
		while(line != null)
		{
		    if(line != null)
		    {
			tree.add(line);
			line = in.readLine();			
		    }
		}
		in.close();
		ArrayList<String> idList = new ArrayList<String>(tree);				
		// use list of series to store different replications of series.		
		for(int i = 0; i < idList.size(); i++)
		{
		    if( i == j)
		    {
			while(linec != null)
			{
			    if(linec != null)
			    {			
				int a = 0;
				int b = linec.indexOf(",", a);
				id = linec.substring(a, b);
				
				a = b + 1;
				b = linec.indexOf(",", a);
				time = linec.substring(a, b);
				
				a = b + 1;
				count = linec.substring(a);	
				
				if(idList.get(i).compareTo(id) == 0)
				{				   
				    String[] temp = null;
				    temp = time.split("\\s|/|\\:");
				    // add series in a list box
				    series.addOrUpdate(new Minute(Integer.parseInt(temp[4]), Integer.parseInt(temp[3]), 
					    Integer.parseInt(temp[1]), Integer.parseInt(temp[0]), 
					    Integer.parseInt(temp[2])), Double.parseDouble(count));			    		    
				}
			    }
			    linec = inc.readLine();
			}		
			serieslist.add(series);
		    }
		    
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
			"New York", 
			"Time", 
			"Count", 
			dataset, 
			false, 
			true, 
			false);
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setDomainCrosshairVisible(false);
		plot.setRangeCrosshairVisible(false);		
		
		DateAxis axis = (DateAxis)plot.getDomainAxis();		
		axis.setDateFormatOverride(new SimpleDateFormat("dd-MM"));		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
		setContentPane(chartPanel);		
		//inc.close();
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}		
    }
    
    public void getIndices()
    {
	ArrayList<Integer> selected = new ArrayList<Integer>();
	// different indices based on articles receiving more than 1000
	// articles receiving more than 100 clicks and less than 500
	// articles receiving more than 500 and less than 1000
	
	try
	{	    
	    BufferedWriter high = new BufferedWriter(new FileWriter(FILE_PATH + "highindex.csv"));
	    BufferedWriter middle = new BufferedWriter(new FileWriter(FILE_PATH + "middleindex.csv"));
	    BufferedWriter low = new BufferedWriter(new FileWriter(FILE_PATH + "lowindex.csv"));
	    
	    //BufferedReader inc = new BufferedReader(new FileReader(FILE_PATH + "cumcounts_ny.csv"));	    
	    ArrayList<String> idList = new ArrayList<String>(tree);
	    String id, time, count;	    
	    
	    for(int i = 0; i < idList.size(); i++)
	    {
		BufferedReader inc = new BufferedReader(new FileReader(FILE_PATH + "cumcounts_ny.csv"));	    
		String linec = inc.readLine();
		int totalcount = 0;
		while(linec != null)
		{
		    if(linec != null)
		    {			
			int a = 0;
			int b = linec.indexOf(",", a);
			id = linec.substring(a, b);
			
			a = b + 1;
			b = linec.indexOf(",", a);
			time = linec.substring(a, b);
			
			a = b + 1;
			count = linec.substring(a);	
			
			if(idList.get(i).compareTo(id) == 0)
			{			   
			    totalcount++;
			    
			}
		    }
		    linec = inc.readLine();
		}
		//System.out.println(totalcount + " rest"); 
		if(totalcount > 1000)
		{
		    high.write(i + "\n");
		     //selected.add(i);
		    //System.out.println(i); 
		}
		else if( totalcount > 500 && totalcount <= 1000)
		{
		    middle.write(i + "\n");		    
		}
		else if( totalcount > 100 && totalcount <= 500)
		{
		    low.write(i + "\n");		    
		}
		inc.close();
	    }
	    high.close();
	    middle.close();
	    low.close();
	    
	    
	}
	catch(Exception ex)
	{
	    ex.printStackTrace();
	}
	
    }
    
    public void repetition()
    {
	BufferedReader indexreader;
	try 
	{
	    indexreader = new BufferedReader(new FileReader(FILE_PATH + "lowindex.csv"));
	    String index = indexreader.readLine();
	    ArrayList<Integer> indexList = new ArrayList<Integer>();
	    while(index != null)
		{
		    if(index != null)
		    {			
			indexList.add(new Integer(index));
			index = indexreader.readLine();
		    }
		}			
		indexreader.close();
		Collections.shuffle(indexList);
		int indices[] = {1722, 1469, 2210, 423, 483, 2007, 289, 2118, 2217, 2212};
		makePlot(indices[9]);
		for(int i = 0; i < 10; i++)
		{
		    //System.out.println(indexList.get(i)); 
		} 			    
	    
	}
	catch (Exception e) 
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	
    }
    
    public ArrayList<TimeSeries> getData()
    {
	return serieslist;
    }
}
