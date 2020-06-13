package edu.sunyk.cse216.visualanalytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class HighLowChart extends ApplicationFrame {

    private static List<String[]> data;
    private static String userTicker;
    public HighLowChart(String title, List<String[]> data) {
        super(title);
        HighLowChart.userTicker = title;
        HighLowChart.data = data;
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));
        setContentPane(chartPanel);
  
    }

    public static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int x=0; x<data.size();x++){
            
            
            final String OLD_FORMAT = "yyyy-MM-dd";
            final String NEW_FORMAT = "EEE MMM dd HH:mm:ss z";

            String oldDateString = data.get(x)[0];
            String newDateString;

            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date d=null;
            try {
                d = sdf.parse(oldDateString);
            } catch (ParseException ex) {
                Logger.getLogger(HighLowChart.class.getName()).log(Level.SEVERE, null, ex);
            }
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
            data.get(x)[0]=newDateString;
            
            double high = Double.parseDouble(data.get(x)[2]);
            double low = Double.parseDouble(data.get(x)[3]);
     
            dataset.addValue(high, "High", data.get(x)[0]);
            dataset.addValue(low, "Low", data.get(x)[0]);
            
        } 
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart chart = ChartFactory.createBarChart(
            userTicker,               // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, 
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
            
        );
        
        CategoryAxis axis = chart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRangePannable(true);
        MinMaxCategoryRenderer renderer = new MinMaxCategoryRenderer();
        renderer.setDrawLines(false);
        plot.setRenderer(renderer);
        ChartUtilities.applyCurrentTheme(chart);
        return chart;
    }

    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
}