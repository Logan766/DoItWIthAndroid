package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

public class LineItem extends  ChartItem {
    private LineData mLineData;

    public LineItem(ChartData<?> chartData) {
        super(chartData);
        mLineData = (LineData) chartData;

    }

    @Override
    public int getGraphType() {
        return 1;
    }

    @Override
    public ChartData<?> getChartData() {
        return null;
    }
}
