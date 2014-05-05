package DailyMePlot;

import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class combinedPlot extends ApplicationFrame
{
    ArrayList<TimeSeries> data= new ArrayList<TimeSeries>();
    
    public combinedPlot(ArrayList<TimeSeries> data)
    {
	super("combined");
	this.data = data;
	ChartPanel chartpanel = new ChartPanel(makingPlot());
	chartpanel.setPreferredSize(new java.awt.Dimension(1000, 500));
	setContentPane(chartpanel);
    }
    
    // make two subplots
    public JFreeChart makingPlot()
    {
	XYPlot plot1 = new XYPlot((XYDataset) data.get(0), new DateAxis("Date"), null, null);
	XYPlot plot2 = new XYPlot((XYDataset) data.get(1), new DateAxis("Date"), null, null);
	
	CombinedRangeXYPlot plot = new CombinedRangeXYPlot(new NumberAxis("Value"));
	plot.add(plot1, 1);
	plot.add(plot2, 2);
	
	JFreeChart result = new JFreeChart("combined plot", plot);
	return result;
	
	
    }

}
