package com.janhoracek.doitwithandroid.Overview;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.janhoracek.doitwithandroid.ChartDataHolder;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsByMonth;

import java.util.ArrayList;
import java.util.List;

public class BarItem extends ChartItem {

    private BarData mBarData;
    private String mTitle;
    private BarChart mBarChart;
    private int mBarGraphKind;

    public BarItem(ChartData<?> chartData, String title, int barGraphKind) {
        super(chartData);
        this.mBarData = (BarData) chartData;
        this.mTitle = title;
        this.mBarGraphKind = barGraphKind;

    }

    @Override
    public int getOwnKindType() {
        return mBarGraphKind;
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

        mBarChart = (BarChart) chart;
        mBarChart.setNoDataText("No data");
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.animateY(2500);
        mBarChart.getLegend().setEnabled(false);


        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setGranularity(1f);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        switch (mBarGraphKind) {
            case 1:
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getDateByDay()));
                break;
            case 2:
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getDateByMonth()));
                break;
        }


    }

    private ArrayList<String> getDateByDay() {
        ArrayList<String> label = new ArrayList<>();
        List<Stats> stats = ChartDataHolder.getInstance().getStatsList();
        for (int i = 0; i <= stats.size()-1; i++) {
            String day = String.valueOf(stats.get(i).getDate());
            String month = String.valueOf(stats.get(i).getMonth());
            String year = String.valueOf(stats.get(i).getYear());
            label.add(day + "." + month + "\n" + year);
        }
        return label;
    }

    private ArrayList<String> getDateByMonth() {
        ArrayList<String> label = new ArrayList<>();
        List<StatsByMonth> stats = ChartDataHolder.getInstance().getStatsByMonths();
        for(int i = 0; i <= stats.size()-1; i++) {
            String month = String.valueOf(stats.get(i).getMonth());
            String year = String.valueOf(stats.get(i).getYear());
            label.add(month + "." + year);
        }

        return label;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setGraphData(ChartData<?> chartData) {
        mBarChart.setData((BarData) chartData);
    }

    @Override
    public void notifyGraph() {
        mBarChart.notifyDataSetChanged();
        mBarChart.invalidate();
    }

    @Override
    public Chart getGraph() {
        return mBarChart;
    }

}
