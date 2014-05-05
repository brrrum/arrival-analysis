package Digg;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class nyPlot extends ApplicationFrame
{
    private static String FILE_PATH = 
	    "C" + ":" + File.separator + "academic" + File.separator+"recommender system 2"+File.separator
	    +"Management Science"+File.separator;
    private ArrayList<diggRecord> data = new ArrayList<diggRecord>();
    private ArrayList<diggRecord> markedData = new ArrayList<diggRecord>();
    private ArrayList<TimeSeries> subdata = new ArrayList<TimeSeries>();
    
    TreeSet<String> tree = new TreeSet<String>();
    
    public nyPlot(String title) 
    {
	super(title);
	prepareData();
	//timeNormal();
	clickPlot();
    }
    
    private void prepareData()
    {
	try
	{
	    BufferedReader in = new BufferedReader(new FileReader(FILE_PATH + "ny-clicks" + File.separator + "ny_clicksplot.csv"));
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
		    
		    a = b + 1;
		    String cumcount = line.substring(a);
		    
		    line = in.readLine();
		    if( line == null)
		    {
			break;
		    }
		    // to get cumulative counts
		    String nextid = line.substring(0, line.indexOf(",", 0)); 
		    if(articleid.compareTo(nextid) == 0)
		    {
			diggRecord nyrecord = new diggRecord(articleid);
			nyrecord.setTime(timestamp);
			nyrecord.setcumcount(Integer.parseInt(cumcount));			
			data.add(nyrecord);
			count++;
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
    
    public void clickPlot()
    {
	// generating random ids
	ArrayList<String> ids = new ArrayList<String>(tree);
	ArrayList<String> selectid = new ArrayList<String>();
	Collections.shuffle(ids);
	System.out.println();
	for(int i = 0; i < 6; i++)
	{
	    selectid.add(ids.get(i));
	    System.out.print(ids.get(i) + ", "); 
	}
	
	TimeSeries series = new TimeSeries(" ");
	for(String id: selectid)
	{
	    for(diggRecord dg: markedData)
	    {
		if(id.compareTo(dg.getId()) == 0)
		{
		    String time = dg.getTime();
		    String[] temp = null;
		    temp = time.split("\\s|/|\\:");
		    int count = dg.getcumcount();
		    series.addOrUpdate(new Minute(Integer.parseInt(temp[4]), Integer.parseInt(temp[3]), 
			    Integer.parseInt(temp[1]), Integer.parseInt(temp[0]), Integer.parseInt(temp[2])), count);
		    
		}
	    }
	    
	    try
	    {
		TimeSeries series2 = series.createCopy(0, series.getItemCount() - 1);
		subdata.add(series2);
		series.delete(0, series.getItemCount() - 1);
	    }
	    catch(Exception ex)
	    {
		ex.printStackTrace();
	    }
	}
	
	TimeSeriesCollection dataset = new TimeSeriesCollection();
	for(TimeSeries ts: subdata)
	{	   
	    dataset.addSeries(ts);
	}
	
	JFreeChart chart = ChartFactory.createTimeSeriesChart(
		"NY random-6", 
		"Hours (0-23 hour format) ", 
		"Views", 
		dataset, 
		true, 
		true, 
		false);
	 chart.setBorderPaint(Color.white);
	 XYPlot plot = chart.getXYPlot();
	 plot.setBackgroundPaint(Color.white);
	 plot.setOutlinePaint(null);
	 
	 DateAxis axis = (DateAxis)plot.getDomainAxis();
	 axis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 20));
	 
	 ValueAxis yaxis = plot.getRangeAxis();	 
	 yaxis.setAutoRangeMinimumSize(100);
	 //yaxis.setMinorTickCount(5);
	 
	 axis.setDateFormatOverride(new SimpleDateFormat("H"));
	 ChartPanel chartPanel = new ChartPanel(chart);
	 chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
	 setContentPane(chartPanel);	 
	
    }
    
    public void timeNormal()
    {
	ArrayList<String> ids = new ArrayList<String>(tree);
	try 
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter(FILE_PATH + "ny-clicks" + File.separator + "ny_clicksnormal.csv"));
	    for(int i = 0; i < ids.size(); i++)
	    {
		String beginTime = null;
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy kk:mm");		
		for(diggRecord dg: markedData)
		{
		    if(ids.get(i).compareTo(dg.getId()) == 0)
		    {
			beginTime = dg.getTime();
			
			break;
		    }		    
		}
		//String sDate = df.format(beginTime);
		Date newdate = df.parse(beginTime);		
		
		// beginTime has been marked
		for(diggRecord dg : markedData)
		{
		    
		    if(ids.get(i).compareTo(dg.getId()) == 0)
		    {			
			Date newsdate = df.parse(dg.getTime());
			long xx = newsdate.getTime() - newdate.getTime();
			// milliseconds to date
			Date strdate = new Date(xx);
			String cdate = Integer.toString(strdate.getMonth()) + "/" +
			Integer.toString(strdate.getDay()) + "/" +
				Integer.toString(strdate.getYear()) + " " + 
			Integer.toString(strdate.getHours()) + ":" +
				Integer.toString(strdate.getMinutes());
			
			if(dg.getTime().compareTo(beginTime) == 0)
			{
			    continue;
			    //System.out.println(cdate);
			}
			else
			{
			    int adjustcount = dg.getcumcount() - 1;
			    out.write(dg.getId() + "," + dg.getTime() + "," + beginTime + "," + adjustcount + "\n");
			}		
						
		    }
		}
	    }
	    out.close();
	} 
	catch (Exception e) 
	{	    
	    e.printStackTrace();
	}
    }

    

}
