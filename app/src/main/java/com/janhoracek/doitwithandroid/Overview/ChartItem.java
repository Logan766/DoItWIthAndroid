package com.janhoracek.doitwithandroid.Overview;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;

public abstract class ChartItem {
    ChartData<?> mChartData;

    public ChartItem(ChartData<?> chartData) {
        this.mChartData = chartData;
    }

    public abstract int getGraphType();

    public abstract ChartData<?> getChartData();

    public abstract void styleGraph(Chart chart);

    public abstract String getTitle();

    public abstract void setGraphData(ChartData<?> chartData);

    public abstract void notifyGraph();

}
