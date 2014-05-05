package thousandPlus;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

public class tracker extends ApplicationFrame
{
    // fielpath, ids, 
    private String filepath;
    private ArrayList<String> ids;
    private ArrayList<diggRecord> data = new ArrayList<diggRecord>();
    private ArrayList<diggRecord> markedData = new ArrayList<diggRecord>();
    
    public tracker(String filepath, ArrayList<String> ids)
    {
	super("test");
	this.filepath = filepath;
	this.ids = ids;
    }
    
    public void read()
    {
	try
	{
	    BufferedReader in = new BufferedReader(new FileReader(filepath));
	    
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
			if(count > 99)
			{
			    markedData.addAll(data);			    
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
    
    public void plot()
    {
	TimeSeries series = new TimeSeries(" ");
	for(diggRecord ms: markedData)
	    {
		if(ids.get(5).compareToIgnoreCase(ms.getId()) == 0)
		{
		    String time = ms.getTime();
		    String[] temp = null;
		    temp = time.split("\\s|/|\\:");
		    int count = ms.getcumcount();
		    series.addOrUpdate(new Minute(Integer.parseInt(temp[4]), Integer.parseInt(temp[3]), 
			    Integer.parseInt(temp[1]), Integer.parseInt(temp[0]), Integer.parseInt(temp[2])), count);
		}
	    }
	    TimeSeriesCollection dataset = new TimeSeriesCollection();
	    dataset.addSeries(series);
	    JFreeChart chart = ChartFactory.createTimeSeriesChart(
			"Individual", 
			"Hours (0-23 hour format)", 
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
	    axis.setTickUnit(new DateTickUnit(DateTickUnitType.HOUR, 5));
	    ValueAxis yaxis = plot.getRangeAxis();	
	    yaxis.setRange(0, 5000);

	    axis.setDateFormatOverride(new SimpleDateFormat("h"));
	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
	    setContentPane(chartPanel);
    }

}
