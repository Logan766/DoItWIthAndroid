package com.janhoracek.doitwithandroid.Overview;

import com.github.mikephil.charting.data.ChartData;

public abstract class ChartItem {
    ChartData<?> mChartData;

    public ChartItem(ChartData<?> chartData) {
        this.mChartData = chartData;
    }

    public abstract int getGraphType();

    public abstract ChartData<?> getChartData();

}
