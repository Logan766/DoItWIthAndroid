package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Database.Stats;

import java.util.ArrayList;
import java.util.List;
/**
 * Contains Line Chart to be used in recycler view in overall stats
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class LineItem extends  ChartItem {
    private static LineData mLineData;
    private String mTitle;
    private LineChart mLineChart;
    private int mLineGraphKind;
    private Context mContext;

    /**
     * Constructor
     *
     * @param chartData Bar Chart Data
     * @param title Title of chart
     * @param lineGraphKind Type of Line Chart
     * @param context Context
     */
    public LineItem(ChartData<?> chartData, String title, int lineGraphKind, Context context) {
        super(chartData);
        mLineData = (LineData) chartData;
        this.mTitle = title;
        this.mLineGraphKind = lineGraphKind;
        this.mContext = context;
    }

    @Override
    public int getOwnKindType() {
        return mLineGraphKind;
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
        mLineChart.animateY(1000);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);



        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getDate()));

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

    @Override
    public Chart getGraph() {
        return mLineChart;
    }

    public ArrayList<String> getDate() {

        List<Stats> stats = DataHolder.getInstance().getStatsList();
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i <= stats.size()-1; i++) {
            String day = String.valueOf(stats.get(i).getDate());
            String month = String.valueOf(stats.get(i).getMonth());
            String year = String.valueOf(stats.get(i).getYear());
            label.add(day + "." + month + "\n" + year);
        }
        return label;
    }
}


