package com.janhoracek.doitwithandroid.Overview;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;

public class PieItem extends ChartItem {

    private PieData mPieData;
    private String mTitle;
    private int mPieGraphKind;

    public PieItem(ChartData<?> chartData, String title, int pieGraphKind) {
        super(chartData);
        this.mPieData = (PieData) chartData;
        this.mTitle = title;
        this.mPieGraphKind = pieGraphKind;
    }

    @Override
    public int getOwnKindType() {
        return 0;
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

    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setGraphData(ChartData<?> chartData) {

    }

    @Override
    public void notifyGraph() {

    }

    @Override
    public Chart getGraph() {
        return null;
    }

}
