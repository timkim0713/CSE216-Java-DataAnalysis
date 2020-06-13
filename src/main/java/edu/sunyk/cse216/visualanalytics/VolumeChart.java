package edu.sunyk.cse216.visualanalytics;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

public class VolumeChart extends ApplicationFrame {

    private static List<String[]> data ;
    private static String userTicker;
    public VolumeChart(String title, List<String[]> data) {
         super(title);
        VolumeChart.data=data;
        VolumeChart.userTicker =  title;
        setContentPane(createDemoPanel());
    
    }
    
    public static JPanel createDemoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
        TimeSeriesCollection dataset = createDataset(userTicker);
        JFreeChart chart2 = createChart2(userTicker, dataset);
        ChartPanel chartPanel2 = new ChartPanel(chart2);
        panel.add(chartPanel2);
        return panel;
    }

    private static TimeSeriesCollection createDataset(String title) {
        TimeSeries s1 = new TimeSeries(title, Day.class);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for(int x=0; x<data.size();x++){
             try {
                 Date current = format.parse(data.get(x)[0]);
                 double volume = Double.parseDouble(data.get(x)[5]);   
                 
                 int day = current.getDate();
                 int month = current.getMonth()+1;
                 int year = current.getYear()+1900;
                                         
                 s1.addOrUpdate(new Day(day, month, year), volume);
             } catch (ParseException ex) {
                 Logger.getLogger(VolumeChart.class.getName()).log(Level.SEVERE, null, ex);
             }
        } 

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.setDomainIsPointsInTime(false);
        return dataset;

    }

    private static JFreeChart createChart2(String title, IntervalXYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYBarChart(
                title,              // title
                "Month",             // x-axis label
                true,               // date axis?
                "Volume",           // y-axis label
                dataset,          //  // data
                PlotOrientation.VERTICAL,       // orientation
                true,               // create legend?
                true,               // generate tooltips?
                false               // generate URLs?
        );
        // Set chart styles
        chart.setBackgroundPaint(Color.white);
        // Set plot styles
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));      
        // Set date axis style
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateTickUnit unit = new DateTickUnit(DateTickUnit.DAY, 5,formatter);
        axis.setTickUnit(unit);
        axis.setVerticalTickLabels(true);
        return chart;
    }
   
}