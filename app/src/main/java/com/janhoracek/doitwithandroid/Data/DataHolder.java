package com.janhoracek.doitwithandroid.Data;

import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsByMonth;
import com.janhoracek.doitwithandroid.Database.StatsOverall;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.rgb;

/**
 * Provides data for graphs and summaries.
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class DataHolder {

    private static DataHolder instance;
    private static LineData mLineChartData;
    private static List<Stats> sStatsList;
    private static List<StatsByMonth> sStatsByMonths;
    private static BarData mBarDataMonth;
    private static BarData mBarDataDay;
    private static PieData mPieOverallData;
    private static Boolean allTasksDoable = true;
    private static Boolean mediumTasksDoable = true;
    private static Boolean highTasksDoable = true;
    private static Boolean deadlinesDoable = true;

    /**
     * Constructor of DataHolder
     */
    private DataHolder() {

    }

    /**
     * Get instance
     * @return instance of DataHolder
     */
    public static DataHolder getInstance() {
        if(instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    /**
     * Gets data for Line Chart
     * @return LineData
     */
    public LineData getmLineChartData() {
        return mLineChartData;
    }

    /**
     * Sets data for Line Chart
     * @param stats list of statistics from database
     */
    public void setmLineChartData(List<Stats> stats) {
        sStatsList = stats;
        mLineChartData = new LineData();
        ArrayList<Entry> yValues = new ArrayList<>();

        //adds exp to array
        for(int i = 0; i<= stats.size()-1; i++) {
            yValues.add(new Entry(i, stats.get(i).getExp()));
        }

        //formatting data
        LineDataSet set = new LineDataSet(yValues, "EXP");
        set.setLineWidth(3f);
        set.setColor(rgb(37, 168, 0));
        set.setCircleHoleColor(rgb(178, 255, 89));
        set.setCircleColor(rgb(37, 168, 0));
        set.setHighLightColor(rgb(37, 168, 0));
        set.setDrawValues(false);
        set.setCircleRadius(5f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);

        //create data set and insert data
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        //load data set to LineData
        mLineChartData = new LineData(dataSets);
        mLineChartData.notifyDataChanged();
    }

    /**
     * Gets list of stats
     * @return List of Stats
     */
    public List<Stats> getStatsList() {
        return sStatsList;
    }

    /**
     * Gets stats by month
     * @return List of Stats by month
     */
    public List<StatsByMonth> getStatsByMonths() {
        return sStatsByMonths;
    }

    /**
     * Gets data for Bar Chart (month statistics)
     * @return BarData
     */
    public BarData getmBarDataMonth() {
        return mBarDataMonth;
    }

    /**
     * Sets data for Bar Chart (month statistics)
     * @param stats list of Stats by month
     */
    public void setmBarDataMonth(List<StatsByMonth> stats) {
        sStatsByMonths = stats;
        mBarDataMonth = new BarData();
        mBarDataMonth.setBarWidth(0.5f);

        List<BarEntry> entries = new ArrayList<>();

        //load priority done
        for(int i = 0; i<= stats.size()-1; i++) {
            entries.add(new BarEntry(i, new float[] {stats.get(i).getLow_done(), stats.get(i).getMedium_done(), stats.get(i).getHigh_done()}));
        }

        //format data
        BarDataSet set = new BarDataSet(entries, "Tasks done in month");
        set.setColors(rgb(156,204,101), rgb(255,202,40), rgb(239, 83, 80));
        //set data
        mBarDataMonth = new BarData(set);
    }

    /**
     * Gets BarData for day statistics
     * @return BarData (day statistics)
     */
    public BarData getmBarDataDay() {
        return mBarDataDay;
    }

    /**
     * Sets BarData (day statistics)
     * @param stats List of Stats by day
     */
    public void setmBarDataDay(List<Stats> stats) {
        List<Stats> Sstats = stats;
        mBarDataDay = new BarData();
        mBarDataDay.setBarWidth(0.5f);
        List<BarEntry> entries = new ArrayList<>();

        //load priorities
        for(int i = 0; i<= stats.size()-1; i++) {
            entries.add(new BarEntry(i, new float[] {stats.get(i).getLow_priority_done(), stats.get(i).getMedium_priority_done(), stats.get(i).getHigh_priority_done()}));
        }
        //format data
        BarDataSet set = new BarDataSet(entries, "Tasks done every day");
        set.setColors(rgb(156,204,101), rgb(255,202,40), rgb(239, 83, 80));
        //load data set to data
        mBarDataDay = new BarData(set);
    }

    /**
     * Sets data for PieChart (overall tasks done)
     * @param stats List of StatsOverall
     */
    public void setmPieOverallData(List<StatsOverall> stats) {
        List<StatsOverall> Sstats = stats;
        mPieOverallData = new PieData();
        List<PieEntry> entries = new ArrayList<>();

        //load records
        entries.add(new PieEntry(stats.get(0).getLow_done(), ""));
        entries.add(new PieEntry(stats.get(0).getMedium_done(), ""));
        entries.add(new PieEntry(stats.get(0).getHigh_done(), ""));

        //add entries to data set and format
        PieDataSet set = new PieDataSet(entries, "Tasks done by priority");
        set.setColors(rgb(156,204,101), rgb(255,202,40), rgb(239, 83, 80));
        //load data set to PieData
        mPieOverallData = new PieData(set);
    }

    /**
     * Gets data for Pie Chart (overall tasks done)
     * @return
     */
    public PieData getmPieOverallData() {
        return mPieOverallData;
    }

    /**
     * Gets status of all tasks doable
     * @return Boolean status of all tasks doable
     */
    public Boolean getAllTasksDoable() {
        return allTasksDoable;
    }

    /**
     * Sets all tasks doable status
     * @param allTasksDoable Boolean all tasks doable
     */
    public void setAllTasksDoable(Boolean allTasksDoable) {
        DataHolder.allTasksDoable = allTasksDoable;
    }

    /**
     * Gets status of medium to high tasks doable
     * @return Boolean status of medium to high tasks doable
     */
    public Boolean getMediumTasksDoable() {
        return mediumTasksDoable;
    }

    /**
     * Sets medium to high tasks doable status
     * @param mediumTasksDoable Boolean status of medium to high tasks doable
     */
    public void setMediumTasksDoable(Boolean mediumTasksDoable) {
        DataHolder.mediumTasksDoable = mediumTasksDoable;
    }

    /**
     * Gets status of high tasks doable
     * @return Boolean status of high tasks doable
     */
    public Boolean getHighTasksDoable() {
        return highTasksDoable;
    }

    /**
     * Sets high tasks doable status
     * @param highTasksDoable Boolean status of high tasks doable
     */
    public void setHighTasksDoable(Boolean highTasksDoable) {
        DataHolder.highTasksDoable = highTasksDoable;
    }

    /**
     * Get status of deadlines of all tasks
     * @return Boolean status of all tasks deadlines doable
     */
    public Boolean getDeadlinesDoable() {
        return deadlinesDoable;
    }

    /**
     * Sets status of deadlines of all tasks
     * @param deadlinesDoable Boolean status of all deadline doable
     */
    public void setDeadlinesDoable(Boolean deadlinesDoable) {
        DataHolder.deadlinesDoable = deadlinesDoable;
    }
}
