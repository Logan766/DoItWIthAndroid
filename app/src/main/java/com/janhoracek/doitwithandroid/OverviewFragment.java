package com.janhoracek.doitwithandroid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.janhoracek.doitwithandroid.Overview.BarItem;
import com.janhoracek.doitwithandroid.Overview.ChartItem;
import com.janhoracek.doitwithandroid.Overview.GraphAdaper;
import com.janhoracek.doitwithandroid.Overview.LineItem;
import com.janhoracek.doitwithandroid.Overview.PieItem;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {
    BarChart mBarChartTasksByMonth;
    LineChart mLineChartExpGained;
    BarChart mBarChartTasksByYear;
    PieChart mPieChartTaskRatio;
    RecyclerView mRecyclerView;
    FloatingActionButton mFloatingActionButton;
    RecyclerView.LayoutManager mLayoutManager;
    List<ChartItem> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_view_graphs);
        mFloatingActionButton = v.findViewById(R.id.fab_change);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        items.add(new BarItem(new DataFetcher().tuMasBare()));
        items.add(new BarItem(new DataFetcher().tuMasBare2()));
        items.add(new BarItem(new DataFetcher().tuMasBare3()));
        items.add(new BarItem(new DataFetcher().tuMasBare()));
        items.add(new BarItem(new DataFetcher().tuMasBare()));
        items.add(new BarItem(new DataFetcher().tuMasBare()));
        items.add(new BarItem(new DataFetcher().tuMasBare()));
        items.add(new PieItem(new DataFetcher().tuMas()));

        GraphAdaper adaper = new GraphAdaper();
        adaper.setGraphs(items);

        mRecyclerView.setAdapter(adaper);

        Log.d("DIWD", "ted cumis na statistiku");


        return v;
    }


}
