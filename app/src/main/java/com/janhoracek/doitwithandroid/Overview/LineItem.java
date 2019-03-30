package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

public class LineItem extends  ChartItem {
    private LineData mLineData;
    private String mTitle;
    private LineChart mLineChart;

    public LineItem(ChartData<?> chartData, String title) {
        super(chartData);
        mLineData = (LineData) chartData;
        this.mTitle = title;
    }

    @Override
    public int getGraphType() {
        return 1;
    }

    @Override
    public ChartData<?> getChartData() {
        return this.mLineData;
    }

    @Override
    public void styleGraph(Chart chart) {
        mLineChart = (LineChart) chart;

        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.animateX(1000);
        mLineChart.getLegend().setEnabled(false);



        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);




    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setGraphData(ChartData<?> chartData) {
        mLineChart.setData((LineData) chartData);
    }

    @Override
    public void notifyGraph() {
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }
}
