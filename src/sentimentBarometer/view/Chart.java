/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentBarometer.view;

import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;

import org.jfree.data.xy.XYSeriesCollection;
import sentimentBarometer.model.observer.Observer;
import sentimentBarometer.model.observer.Subject;

/**
 *Handles the creation and manipulation of the Chart 
 *
 * @author Tim Skehel
 */
public class Chart implements Observer {
    
    private XYSeriesCollection _dataset;
    private XYSeries _series;
    private Subject _ratingNwords;
    
    //constructor
    public Chart() {
        _dataset  = new XYSeriesCollection();
        _series = new XYSeries("Rating over number Emotional Words");
        _dataset.addSeries(_series);
    }
    
    
    /**
     * Creates the chart based on the @see#_dataset
     * @return ChartPanel that extends JPanel so it can be added directly into a JPanel in a Swing Interface
     */
    public ChartPanel createChart() {
        //create chart
        JFreeChart chart = ChartFactory.createXYLineChart("Rating",
                "Emotional Words", "Rating", _dataset, PlotOrientation.VERTICAL, true, true,
                false);
        //add chart to panel
        ChartPanel cp = new ChartPanel(chart);
        //return the panel
        return cp;
    }

    //Implementaion of Observer
    
    /**
     * Listens to the Model and updates @see#_dataset as the Rating updates
     * due to JFreeChart's own implementations of the observer pattern updating the dataset automaically updates
     * the graph
     */
    @Override
    public void update() {
        //check not null
        if (_ratingNwords.getUpdate(this)!= null) {
            //get the update
            ArrayList<Double> xy = (ArrayList<Double>) _ratingNwords.getUpdate(this);
            //seperate the values
            double x = xy.get(0);
            double y = xy.get(1);
            //update the graph
            _series.add(x, y);
        } else {
            System.out.println("NO UPDATE");
        }
    }
    
    @Override
    public void setSubject(Subject sub) {
        _ratingNwords=sub;
    }
}

