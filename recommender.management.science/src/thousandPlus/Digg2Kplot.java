package thousandPlus;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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

import Digg.diggRecord;

public class Digg2Kplot extends ApplicationFrame
{
    private String filepath = "C" + ":" + File.separator
	    + "academic" + File.separator + "recommender system 2" + File.separator
	    + "data" + File.separator + "digg_votes" + 
	    File.separator + "2000pNormal.csv";
    private ArrayList<diggRecord> data = new ArrayList<diggRecord>();
    TreeSet<String> tree = new TreeSet<String>();
    private ArrayList<TimeSeries> subdata = new ArrayList<TimeSeries>();
    private ArrayList<diggRecord> markedData = new ArrayList<diggRecord>();

    public Digg2Kplot(String title) 
    {
	super("More than 2000 votes");
	prepareData();
	votePlot(6);
    }

    private void prepareData() 
    {
	try
	{
	    BufferedReader in = new BufferedReader(new FileReader(filepath));
	    String line = in.readLine();
	    int ccount = 1;
	    String articleid, timestamp;
	    while(line != null)
	    {
		if(line != null)
		{
		    int a1 = 0;
		    int b1 = line.indexOf(",", a1);
		    articleid = line.substring(a1, b1);
		    
		    a1 = b1 + 1;
		    timestamp = line.substring(a1);
		    
		    diggRecord diggrecord = new diggRecord(articleid);
		    diggrecord.setTime(timestamp);
		    line = in.readLine();
		    if(line == null)
		    {
			break;
		    }
		    String nextid = line.substring(0, line.indexOf(",", 0));
		    if(articleid.compareTo(nextid) == 0)
		    {
			diggrecord.setcumcount(ccount);
			data.add(diggrecord);
			tree.add(articleid);
			ccount++;
		    }
		    else
		    {
			markedData.addAll(data);
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
    
    public void votePlot(int sample)
    {
	ArrayList<String> ids = new ArrayList<String>(tree);
	ArrayList<String> selecteid = new ArrayList<String>();
	Collections.shuffle(ids);
	for(int i = 0; i < sample; i++)
	{
	    selecteid.add(ids.get(i));
	    System.out.println(ids.get(i));
	}
	
	TimeSeries series = new TimeSeries(" ");
	for(String id: selecteid)
	{	    
	    for(diggRecord dg: markedData)
	    {
		if(id.toString().compareTo(dg.getId()) == 0)
		{		   
			String time = dg.getTime();
			//System.out.println(time);			
			String[] temp = null;
			temp = time.split("\\s|/|\\:");
			int count = dg.getcumcount();
			//System.out.println(count+ ", " + time + ", id: " + dg.getId());
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
	    //dataset.addSeries(series);
	    //dataset.addSeries(series1);
	    JFreeChart chart = ChartFactory.createTimeSeriesChart(
			"Digg random-6", 
			"Hours (0-23 hour format) ", 
			"Votes", 
			dataset, 
			true, 
			true, 
			false);
	    chart.setBorderPaint(Color.white);
	    XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.white);
	    plot.setOutlinePaint(null);
	   
	    DateAxis axis = (DateAxis)plot.getDomainAxis();
	    ValueAxis yaxis = plot.getRangeAxis();
	    yaxis.setMinorTickCount(5);
	    
	    //NumberAxis nx = (NumberAxis)plot.getDomainAxis();
	    axis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 20));	   
	    yaxis.setRange(0, 5000);	    
	    
	    axis.setDateFormatOverride(new SimpleDateFormat("H"));
	    // from here .. 
	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(600, 600));
	    setContentPane(chartPanel);
    }
    

}
