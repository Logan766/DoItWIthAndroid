package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.janhoracek.doitwithandroid.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;
/**
 * Contains Pie Chart to be used in recycler view in overall stats
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class PieItem extends ChartItem {

    private PieData mPieData;
    private String mTitle;
    private PieChart mPieChart;
    private int mPieGraphKind;
    private Context mContext;

    /**
     * Constructor
     *
     * @param chartData Bar Chart Data
     * @param title Title of chart
     * @param pieGraphKind Type of Pie Chart
     * @param context Context
     */
    public PieItem(ChartData<?> chartData, String title, int pieGraphKind, Context context) {
        super(chartData);
        this.mPieData = (PieData) chartData;
        this.mTitle = title;
        this.mPieGraphKind = pieGraphKind;
        this.mContext = context;
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
        entries.add(new LegendEntry(mContext.getString(R.string.graph_legend_priority_high), Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, rgb(239, 83, 80)));
        entries.add(new LegendEntry(mContext.getString(R.string.graph_legend_priority_medium), Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, rgb(255,202,40)));
        entries.add(new LegendEntry(mContext.getString(R.string.graph_legend_priority_low), Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, rgb(156,204,101)));
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
