package com.janhoracek.doitwithandroid;

import com.github.mikephil.charting.components.YAxis;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class DataFetcher extends AppCompatActivity {

    public PieData tuMas() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(18.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));

        PieDataSet set = new PieDataSet(entries, "Election Results");
        PieData data = new PieData(set);
        return data;
    }

    public BarData tuMasBare() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        BarData data = new BarData(set);
        return data;
    }

    public BarData tuMasBare2() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 50f));
        entries.add(new BarEntry(1f, 70f));
        entries.add(new BarEntry(2f, 30f));
        entries.add(new BarEntry(3f, 20f));
        // gap of 2f
        entries.add(new BarEntry(5f, 90f));
        entries.add(new BarEntry(6f, 10f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(new int[] {  R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark });
        BarData data = new BarData(set);
        return data;
    }

    public BarData tuMasBare3() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 90f));
        entries.add(new BarEntry(1f, 70f));
        entries.add(new BarEntry(2f, 10f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(5f, 30f));
        entries.add(new BarEntry(6f, 50f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(new int[] {  R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark });
        BarData data = new BarData(set);
        return data;
    }

    public LineData tuMasLino() {
        /*List<Entry> valsComp1 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
        valsComp1.add(c1e2);

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);

        LineData data = new LineData(dataSets);*/

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 70f));
        yValues.add(new Entry(2, 40f));
        yValues.add(new Entry(3, 20f));
        yValues.add(new Entry(4, 90f));

        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        return data;
    }
}
