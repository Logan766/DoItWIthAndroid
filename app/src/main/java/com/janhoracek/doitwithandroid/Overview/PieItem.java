package com.janhoracek.doitwithandroid.Overview;

import android.graphics.Color;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;

import java.util.ArrayList;
import java.util.List;

public class PieItem extends ChartItem {

    private PieData mPieData;
    private String mTitle;
    private PieChart mPieChart;
    private int mPieGraphKind;

    public PieItem(ChartData<?> chartData, String title, int pieGraphKind) {
        super(chartData);
        this.mPieData = (PieData) chartData;
        this.mTitle = title;
        this.mPieGraphKind = pieGraphKind;
    }

    @Override
    public int getOwnKindType() {
        return mPieGraphKind;
    }

    public int getGraphType() {
        return 3;
    }

    @Override
    public ChartData<?> getChartData() {
        return mPieData;
    }

    @Override
    public void styleGraph(Chart chart) {
        mPieChart = (PieChart) chart;
        mPieChart.getDescription().setEnabled(false);
        mPieChart.animateY(2500);
        //mPieChart.setExtraBottomOffset(30f);

        mPieChart.setUsePercentValues(true);

        Legend legend = mPieChart.getLegend();
        legend.setEnabled(true);
        //legend.setFormSize(10f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        List<LegendEntry> entries = new ArrayList<>();
        entries.add(new LegendEntry("High priority", Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, Color.RED));
        entries.add(new LegendEntry("Medium priority", Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, Color.YELLOW));
        entries.add(new LegendEntry("Low priority", Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, Color.GREEN));
        legend.setCustom(entries);
        legend.setXEntrySpace(5f);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setGraphData(ChartData<?> chartData) {
        mPieChart.setData((PieData) chartData);
    }

    @Override
    public void notifyGraph() {
        mPieChart.notifyDataSetChanged();
        mPieChart.invalidate();
    }

    @Override
    public Chart getGraph() {
        return mPieChart;
    }

}
