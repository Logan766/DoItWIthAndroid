package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.protobuf.StringValue;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.Overview.BarItem;
import com.janhoracek.doitwithandroid.Overview.ChartItem;
import com.janhoracek.doitwithandroid.Overview.GraphAdaper;
import com.janhoracek.doitwithandroid.Overview.LineItem;
import com.janhoracek.doitwithandroid.Overview.PieItem;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverviewFragment extends Fragment {
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String OVERVIEW_FRAG_RUN = "com.janhoracek.doitwithandroid.OVERVIEW_FRAG_RUN";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChartItem> items = new ArrayList<>();
    private StatsViewModel mStatsViewModel;
    private SharedPreferences pref;
    private boolean FirstRunCheck;
    //private List<Stats> Mstats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_overview, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        DateChangeChecker.getInstance().CheckDate(pref);

        mRecyclerView = v.findViewById(R.id.recycler_view_graphs);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        items.add(new LineItem(ChartDataHolder.getInstance().getmLineChartData(), "Experience gained", 1));
        items.add(new BarItem(ChartDataHolder.getInstance().getmBarDataDay(), "Tasks completed per day", 1));
        items.add(new BarItem(ChartDataHolder.getInstance().getmBarDataMonth(), "Tasks completed per month", 2));
        items.add(new PieItem(ChartDataHolder.getInstance().getmPieOverallData(), "Tasks priority ratio", 1));

        final GraphAdaper adaper = new GraphAdaper();
        adaper.setGraphs(items);


        mRecyclerView.setAdapter(adaper);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        new DateHandler().checkLastDate(mStatsViewModel);
        mStatsViewModel.getAllStats().observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(@Nullable List<Stats> stats) {

            }
        });
        Log.d("DIWD", "ted cumis na statistiku");
        FirstRunCheck = pref.getBoolean(OVERVIEW_FRAG_RUN, true);
        if(FirstRunCheck) {
            v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    final Spotlight spot = Spotlight.with(getActivity())
                            .setOverlayColor(R.color.background)
                            .setDuration(1000L)
                            .setAnimation(new DecelerateInterpolator(2f))
                            .setTargets(buildTargets(v))
                            .setClosedOnTouchedOutside(true)
                            .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                                @Override
                                public void onStarted() {
                                    Toast.makeText(getContext(), "spotlight is started", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onEnded() {
                                    Toast.makeText(getActivity(), "spotlight is ended", Toast.LENGTH_SHORT).show();
                                    pref.edit().putBoolean(OVERVIEW_FRAG_RUN, false).apply();
                                }
                            });
                    spot.start();
                }

            });
        }
        return v;
    }

    private ArrayList<SimpleTarget> buildTargets(View v) {
        ArrayList<SimpleTarget> targets = new ArrayList<>();

        SimpleTarget welcomeTarget = new SimpleTarget.Builder(getActivity())
                .setShape(new Circle(0))
                .setTitle("Statistics")
                .setDescription("Statistics screen is exactly what it sounds to be. You can see statistics about your progress, completed tasks and more. Just scroll down and look at your statistics :)")
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

        targets.add(welcomeTarget);

        return targets;
    }

}
