package com.janhoracek.doitwithandroid.Overview;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;
/**
 * Abstract class to be used as Chart item in recycler view
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public abstract class ChartItem {
    ChartData<?> mChartData;

    /**
     * Constructor
     *
     * @param chartData Data for desired chart
     */
    public ChartItem(ChartData<?> chartData) {
        this.mChartData = chartData;
    }

    /**
     * Gets exact ID of chart
     *
     * @return ID of chart
     */
    public abstract int getOwnKindType();

    /**
     * Gets number based on graph type
     *
     * @return graph type number
     */
    public abstract int getGraphType();

    /**
     * Gets chart data of graph
     *
     * @return chart data of graph
     */
    public abstract ChartData<?> getChartData();

    /**
     * Apply styling on graph
     *
     * @param chart type of chart that will be styled
     */
    public abstract void styleGraph(Chart chart);

    /**
     * Gets title of chart
     *
     * @return title of chart
     */
    public abstract String getTitle();

    /**
     * Sets chart data
     *
     * @param chartData chart data
     */
    public abstract void setGraphData(ChartData<?> chartData);

    /**
     * Notify graph about changes
     */
    public abstract void notifyGraph();

    /**
     * Gets graph
     * @return Chart
     */
    public abstract Chart getGraph();

}
