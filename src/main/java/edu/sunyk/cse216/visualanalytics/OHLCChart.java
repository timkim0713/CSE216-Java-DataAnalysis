package edu.sunyk.cse216.visualanalytics;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.RectangleInsets;

public class OHLCChart extends JFrame {
  
    
    
  private static final long serialVersionUID = 6294689542092367723L;
  private List<String[]> data;
  private static String userTicker;
  private static String userT;
  public OHLCChart(String title, List<String[]> data, String s ) {
    super(title);
    this.data=data;
    OHLCChart.userTicker= title;
    this.userT = s;
    // Create dataset
    OHLCDataset dataset = createDataset();

    // Create chart
    JFreeChart chart = ChartFactory.createHighLowChart(
        title, 
        "Date", "Price", dataset, true);
    
    XYPlot plot = (XYPlot) chart.getPlot();
    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setAutoRangeIncludesZero(false);

    
           // Set chart styles
        chart.setBackgroundPaint(Color.white);

        // Set plot styles
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
                
        
        // Set date axis style
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateTickUnit unit = new DateTickUnit(DateTickUnit.DAY, 5, formatter);
        axis.setTickUnit(unit);
        
        DateAxis xAxis = new DateAxis("Date");
        xAxis.setVerticalTickLabels(false);
        plot.setDomainAxis(xAxis);
    
    
    
    
    // Create Panel
    ChartPanel panel = new ChartPanel(chart);
    setContentPane(panel);
    
    
  }

  private OHLCDataset createDataset() {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        OHLCDataItem dataItem[] = new OHLCDataItem[data.size()];
    try {
           for(int x=0; x<data.size();x++){
                 Date current = format.parse(data.get(x)[0]);
                 double open = Double.parseDouble(data.get(x)[1]);
                 double high = Double.parseDouble(data.get(x)[2]);
                 double low = Double.parseDouble(data.get(x)[3]);
                 double close = Double.parseDouble(data.get(x)[4]);
                 double volume = Double.parseDouble(data.get(x)[5]);
                 dataItem[x] = new OHLCDataItem(current,open,high,low,close,volume);
                }
 
    } catch (ParseException e) {
      e.printStackTrace();
    }

    OHLCDataset dataset = new DefaultOHLCDataset(userT, dataItem);
    
    return dataset;
  }

}