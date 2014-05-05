package DailyMePlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import distributionGenerator.articleRecord;

public class Plot extends ApplicationFrame
{
    private ArrayList<articleRecord> data = new ArrayList<articleRecord>();
    
    public Plot(ArrayList<articleRecord> data, String title)
    {
	super(title);
	
	this.data = data;
	
	readData();
    }
    
    public void readData()
    {
	TimeSeries series = new TimeSeries("per minute data");
	TreeSet<String> tree = new TreeSet<String>();
	for(articleRecord ar: data)
	{
	    tree.add(ar.getId());
	}
	ArrayList<String> idList = new ArrayList<String>(tree);
	for(int i = 0; i < idList.size(); i++)
	{
	    if( i == 3)
	    {
		for(articleRecord ar: data)
		    {
			if(idList.get(i).compareTo(ar.getId()) == 0)
			{
			    String time = ar.getTime();
			    String[] temp = null;
			    temp = time.split("\\s|/|\\:");
			    int count = ar.getCumCount();
			    series.addOrUpdate(new Minute(Integer.parseInt(temp[4]), Integer.parseInt(temp[3]), 
				    Integer.parseInt(temp[1]), Integer.parseInt(temp[0]), Integer.parseInt(temp[2])), count);			    		    
			}
		    }
	    }
	}
	    
	    TimeSeriesCollection dataset = new TimeSeriesCollection();
	    dataset.addSeries(series);
	    JFreeChart chart = ChartFactory.createTimeSeriesChart(
			"Time Series Demo 10", 
			"Time", 
			"Value", 
			dataset, 
			true, 
			true, 
			false);
	    XYPlot plot = chart.getXYPlot();
	    DateAxis axis = (DateAxis)plot.getDomainAxis();
	    axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));
	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	}      
}
