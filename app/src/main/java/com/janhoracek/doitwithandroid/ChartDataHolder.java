package com.janhoracek.doitwithandroid;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsByMonth;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.res.ResourcesCompat;

public class ChartDataHolder {

    private static ChartDataHolder instance;
    private static LineData mLineChartData;
    private static List<Stats> sStatsList;
    private static List<StatsByMonth> sStatsByMonths;
    private static BarData mBarDataMonth;
    private static BarData mBarDataDay;

    private ChartDataHolder() {

    }

    public static ChartDataHolder getInstance() {
        if(instance == null) {
            instance = new ChartDataHolder();
        }
        return instance;
    }

    public LineData getmLineChartData() {
        return mLineChartData;
    }

    public void setmLineChartData(List<Stats> stats) {
        sStatsList = stats;
        mLineChartData = new LineData();

        ArrayList<Entry> yValues = new ArrayList<>();

        for(int i = 0; i<= stats.size()-1; i++) {
            yValues.add(new Entry(i, stats.get(i).getExp()));
            Log.d("OWUP", "Inserting values to graph ID: " + (stats.get(i).getId()-20190000) + " EXP: "+ stats.get(i).getExp());
        }

        LineDataSet set = new LineDataSet(yValues, "EXP");
        //set.setLineWidth(3f);
        //Log.d("GGR", "ted jsem tlusta ");
        set.setColor(Color.GRAY);
        set.setCircleHoleColor(Color.GRAY);
        set.setCircleColor(Color.GRAY);
        set.setHighLightColor(Color.GRAY);
        set.setDrawValues(false);
        set.setCircleRadius(5f);
        set.setCircleColor(Color.GRAY);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        //set.setDrawFilled(true);
        //set.setFillColor(Color.BLACK);
        //set the transparency
        //set.setFillAlpha(80);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        mLineChartData = new LineData(dataSets);
        mLineChartData.notifyDataChanged();
    }

    public List<Stats> getStatsList() {
        return sStatsList;
    }


    public List<StatsByMonth> getStatsByMonths() {
        return sStatsByMonths;
    }


    public BarData getmBarDataMonth() {
        return mBarDataMonth;
    }

    public void setmBarDataMonth(List<StatsByMonth> stats) {
        sStatsByMonths = stats;
        mBarDataMonth = new BarData();
        mBarDataMonth.setBarWidth(0.5f);

        List<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i<= stats.size()-1; i++) {
            entries.add(new BarEntry(i, new float[] {stats.get(i).getLow_done(), stats.get(i).getMedium_done(), stats.get(i).getHigh_done()}));
        }

        BarDataSet set = new BarDataSet(entries, "Tasks done in month");
        set.setColors(Color.GREEN, Color.YELLOW, Color.RED);
        mBarDataMonth = new BarData(set);
    }

    public BarData getmBarDataDay() {
        return mBarDataDay;
    }

    public void setmBarDataDay(List<Stats> stats) {
        List<Stats> Sstats = stats;
        mBarDataDay = new BarData();
        mBarDataDay.setBarWidth(0.5f);
        List<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i<= stats.size()-1; i++) {
            entries.add(new BarEntry(i, new float[] {stats.get(i).getLow_priority_done(), stats.get(i).getMedium_priority_done(), stats.get(i).getHigh_priority_done()}));
        }

        BarDataSet set = new BarDataSet(entries, "Tasks done every day");
        set.setColors(Color.GREEN, Color.YELLOW, Color.RED);
        mBarDataDay = new BarData(set);
    }
}
