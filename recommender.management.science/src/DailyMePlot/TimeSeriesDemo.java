package DailyMePlot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import thousandPlus.tracker;

public class TimeSeriesDemo extends ApplicationFrame
{
    private static final String FILE_PATH = "C" + ":" + File.separator
	    + "academic" + File.separator + "DailyMe" + File.separator;
    private static String TRACKER_PATH = 
	    "C" + ":" + File.separator + "academic" + File.separator+"recommender system 2"+File.separator
	    +"Management Science"+File.separator + "conn-clicks" + File.separator + "conn_sorted.csv";

    public TimeSeriesDemo(String title) 
    {
	super(title);
	
	TimeSeries series = new TimeSeries("per minute data");
	TimeSeries series1 = new TimeSeries("per minute data-1");
	
	Hour hour = new Hour();
	series.add(new Minute(1, hour), 10.2);
	series.add(new Minute(3, hour), 17.3);
	series.add(new Minute(9, hour), 14.6);
	series.add(new Minute(11, hour), 11.9);
	series.add(new Minute(15, hour), 13.5);
	series.add(new Minute(19, hour), 10.9);
	
	series1.add(new Minute(1, hour), 15.3);
	series1.add(new Minute(5, hour), 5.0);
	series1.add(new Minute(18, hour), 12.1);
	
	TimeSeriesCollection dataset = new TimeSeriesCollection();
	dataset.addSeries(series);
	dataset.addSeries(series1);
	
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
	chartPanel.setPreferredSize(new java.awt.Dimension(1000, 1200));
	setContentPane(chartPanel);
    }
    
    public static void main(String[] args)
    {
	//Counts counts = new Counts(FILE_PATH  + "mng_mass" + File.separator + "mng_mass.csv");
	//Counts counts = new Counts(FILE_PATH  + "jrc_philly" + File.separator + "jrc_philly.csv");
	//CumulativeCounts cumcount = new CumulativeCounts(counts.getData());
	//massPlot massplot = new massPlot("Massachusetts");
	//phillyPlot philPlot = new phillyPlot("Philadelphia");
	//Plot plot = new Plot(cumcount.getData(), "this is test");
	/*
	Plotcsv plot = new Plotcsv("Article popularity");
	plot.pack();	
	RefineryUtilities.centerFrameOnScreen(plot);
	plot.setVisible(true);
	*/	
	//new timeSort();
	
	// for Digg
	/*
	readData read = new readData();
	//Digg2Kplot read = new Digg2Kplot("2000");
	read.pack();
	RefineryUtilities.centerFrameOnScreen(read);
	read.setVisible(true);	
	*/	
	
	TimeSeriesDemo demo = new TimeSeriesDemo("Time Series Demo");
	demo.pack();
	RefineryUtilities.centerFrameOnScreen(demo);
	demo.setVisible(true);
	
	// for Connecticut
	/*
	connPlot plot = new connPlot("Connecticut");
	plot.pack();
	RefineryUtilities.centerFrameOnScreen(plot);
	plot.setVisible(true);
	*/
	
	// for new York
	
	/*nyPlot nplot = new nyPlot("New York");
	nplot.pack();
	RefineryUtilities.centerFrameOnScreen(nplot);
	nplot.setVisible(true);*/
	
	
	//Philadelphia
	/*
	philPlot.pack();
	RefineryUtilities.centerFrameOnScreen(philPlot);
	philPlot.setVisible(true);
	*/
	
	/*	
	massplot.pack();
	RefineryUtilities.centerFrameOnScreen(massplot);
	massplot.setVisible(true);
	*/
	
	ArrayList<String> ids= new ArrayList<String>();
	ids.add("16221131");
	ids.add("14429793");
	ids.add("15369829");
	ids.add("16208856");
	ids.add("15135144");
	ids.add("16406685");
	/*
	tracker track = new tracker(TRACKER_PATH, ids);
	track.read();
	track.plot();
	track.pack();
	RefineryUtilities.centerFrameOnScreen(track);
	track.setVisible(true);*/
	
	/* tracking article*/
	
	
	// for more than 1000 counts
	/*
	Digg1000 gidm = new Digg1000();
	gidm.pack();
	RefineryUtilities.centerFrameOnScreen(gidm);
	gidm.setVisible(true);
	*/
    }
	
}
