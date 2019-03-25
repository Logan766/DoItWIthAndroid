package com.janhoracek.doitwithandroid.Overview;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;

public class PieItem extends ChartItem {

    private PieData mPieData;

    public PieItem(ChartData<?> chartData) {
        super(chartData);
        this.mPieData = (PieData) chartData;
    }

    public int getGraphType() {
        return 3;
    }

    @Override
    public ChartData<?> getChartData() {
        return mPieData;
    }

}
