package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsByMonth;
import com.janhoracek.doitwithandroid.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;
/**
 * Contains Bar Chart to be used in recycler view in overall stats
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class BarItem extends ChartItem {

    private BarData mBarData;
    private String mTitle;
    private BarChart mBarChart;
    private int mBarGraphKind;
    private Context mContext;

    /**
     * Constructor
     *
     * @param chartData Bar Chart Data
     * @param title Title of chart
     * @param barGraphKind Type of Bar Chart
     * @param context Context
     */
    public BarItem(ChartData<?> chartData, String title, int barGraphKind, Context context) {
        super(chartData);
        this.mBarData = (BarData) chartData;
        this.mTitle = title;
        this.mBarGraphKind = barGraphKind;
        this.mContext = context;
    }

    @Override
    public int getOwnKindType() {
        return mBarGraphKind;
    }

    /**
     * Gets type of graph
     *
     * @return type of graph
     */
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
        mBarChart.setNoDataText(mContext.getString(R.string.graph_no_data));
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.animateY(2500);
        mBarChart.setExtraBottomOffset(30f);

        Legend legend = mBarChart.getLegend();
        legend.setEnabled(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        List<LegendEntry> entries = new ArrayList<>();
        entries.add(new LegendEntry(mContext.getString(R.string.graph_legend_priority_high), Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, rgb(239, 83, 80)));
        entries.add(new LegendEntry(mContext.getString(R.string.graph_legend_priority_medium), Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, rgb(255,202,40)));
        entries.add(new LegendEntry(mContext.getString(R.string.graph_legend_priority_low), Legend.LegendForm.CIRCLE, 10f, Float.NaN, null, rgb(156,204,101)));
        legend.setCustom(entries);
        legend.setXEntrySpace(5f);



        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setGranularity(1f);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f);

        //load data based on exact chart
        switch (mBarGraphKind) {
            case 1:
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getDateByDay()));
                break;
            case 2:
                xAxis.setValueFormatter(new IndexAxisValueFormatter(getDateByMonth()));
                break;
        }


    }

    /**
     * Gets labels for data (daily statistics)
     *
     * @return label for data (day)
     */
    private ArrayList<String> getDateByDay() {
        ArrayList<String> label = new ArrayList<>();
        List<Stats> stats = DataHolder.getInstance().getStatsList();
        for (int i = 0; i <= stats.size()-1; i++) {
            String day = String.valueOf(stats.get(i).getDate());
            String month = String.valueOf(stats.get(i).getMonth());
            String year = String.valueOf(stats.get(i).getYear());
            label.add(day + "." + month + "\n" + year);
        }
        return label;
    }

    /**
     * Gets labels for data (monthly statistics)
     *
     * @return label for data (month)
     */
    private ArrayList<String> getDateByMonth() {
        ArrayList<String> label = new ArrayList<>();
        List<StatsByMonth> stats = DataHolder.getInstance().getStatsByMonths();
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
