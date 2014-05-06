package arrivalUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import com.lowagie.text.Image;

public class TrajectoryPlots extends ApplicationFrame {
	
	private ArrayList<ArrayList<Double>> datas = new ArrayList<ArrayList<Double>>();

	public TrajectoryPlots(String title, ArrayList<ArrayList<Double>> datapoints,
			ArrayList<ArrayList<Double>> pdatapoints) {
		super(title);
		JPanel jpanel = createDemoPanel(datapoints, pdatapoints);
		jpanel.setPreferredSize(new Dimension(1000, 800));
		setContentPane(jpanel);
	}

	private JPanel createDemoPanel(ArrayList<ArrayList<Double>> datapoints,
			ArrayList<ArrayList<Double>> pdatapoints) {
		// TODO Auto-generated method stub
		return null;
	}

	public TrajectoryPlots(String string,
			ArrayList<ArrayList<ArrayList<Double>>> repdatapoints) {
		
		super(string); 
		JPanel jpanel = createDemoPanel(repdatapoints);
		jpanel.setPreferredSize(new Dimension(1000, 800));
		setContentPane(jpanel);
	}

	private JPanel createDemoPanel(ArrayList<ArrayList<ArrayList<Double>>> repdatapoints) {
		
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		for(int i = 0; i < repdatapoints.size(); i++) {			
			xySeriesCollection.addSeries(samplexydataset3(repdatapoints.get(i)));
			System.out.println(i); 
		}		
		
		JFreeChart jfreechart = ChartFactory.createXYLineChart("Plot for M1", "time", "M1", xySeriesCollection, 
				PlotOrientation.VERTICAL, false, false, false); 
		// extra		
		Shape cross = ShapeUtilities.createDiagonalCross(2, 1);		
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();		
		NumberAxis domain = (NumberAxis) xyplot.getDomainAxis();
		NumberAxis range = (NumberAxis) xyplot.getRangeAxis();
		domain.setTickUnit(new NumberTickUnit(20));
		range.setTickUnit(new NumberTickUnit(0.05)); 
		
		xyplot.setOutlineVisible(false);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		xyplot.setBackgroundPaint(Color.white);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		XYItemRenderer renderer = xyplot.getRenderer();
		//renderer.setSeriesShape(0, cross);
		//renderer.setSeriesPaint(0, Color.red);
		
		BufferedImage image = jfreechart.createBufferedImage(500, 500);
		save(image);
		
		return new ChartPanel(jfreechart);		
	}

	private void save(BufferedImage image) {
		File file = new File("M1.gif");
		try {
			ImageIO.write(image, "gif", file);
			System.out.println("image written");  
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}

	private XYSeries samplexydataset3(ArrayList<ArrayList<Double>> datapoints) {
		String datasize = Integer.toString(datapoints.size());
		//System.out.println(datapoints.toString()); 
		XYSeries series = new XYSeries("data size: " + datasize);
		
		for(int i = 0; i < datapoints.size(); i++) {
			ArrayList<Double> da = new ArrayList<Double>();			
			double x = datapoints.get(i).get(0);
			da.add(x);
			double y = datapoints.get(i).get(1);
			da.add(y);
			series.add(x, y);
			datas.add(da);
		}
		
		return series;
	}

}
