package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;

public class BarItem extends ChartItem {

    private BarData mBarData;
    private String mTitle;
    private BarChart barGraph;

    public BarItem(ChartData<?> chartData, String title) {
        super(chartData);
        this.mBarData = (BarData) chartData;
        this.mTitle = title;

    }

    public int getGraphType() {
        return 2;
    }

    @Override
    public ChartData<?> getChartData() {
        return this.mBarData;
    }


    @Override
    public void styleGraph(Chart chart) {
        // apply styling

        barGraph = (BarChart) chart;
        barGraph.setNoDataText("No data");
        barGraph.getDescription().setEnabled(false);
        barGraph.setDrawGridBackground(false);
        barGraph.setDrawBarShadow(false);
        barGraph.animateY(2500);
        barGraph.getLegend().setEnabled(false);

        XAxis xAxis = barGraph.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = barGraph.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barGraph.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setGraphData(ChartData<?> chartData) {
        barGraph.setData((BarData) chartData);
    }

    @Override
    public void notifyGraph() {
        barGraph.notifyDataSetChanged();
        barGraph.animate();
    }


}
