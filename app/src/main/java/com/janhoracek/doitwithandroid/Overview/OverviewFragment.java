package com.janhoracek.doitwithandroid.Overview;

import android.content.Context;
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

import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateChangeChecker;
import com.janhoracek.doitwithandroid.Data.DateHandler;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.R;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Overview Fragment, which contains recycler view with graphs
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class OverviewFragment extends Fragment {
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String OVERVIEW_FRAG_RUN = "com.janhoracek.doitwithandroid.OVERVIEW_FRAG_RUN";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChartItem> items = new ArrayList<>();
    private StatsViewModel mStatsViewModel;
    private SharedPreferences pref;
    private boolean FirstRunCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_overview, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        //check date
        DateChangeChecker.getInstance().CheckDate(pref);

        //setup RecyclerView
        mRecyclerView = v.findViewById(R.id.recycler_view_graphs);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        items.add(new LineItem(DataHolder.getInstance().getmLineChartData(), getString(R.string.fragment_overview_line_experience), 1, getActivity()));
        items.add(new BarItem(DataHolder.getInstance().getmBarDataDay(), getString(R.string.fragment_overview_bar_tasks_day), 1, getActivity()));
        items.add(new BarItem(DataHolder.getInstance().getmBarDataMonth(), getString(R.string.fragment_overview_bar_tasks_month), 2, getActivity()));
        items.add(new PieItem(DataHolder.getInstance().getmPieOverallData(), getString(R.string.fragment_overview_pie_priority_ratio), 1, getActivity()));
        final GraphAdaper adaper = new GraphAdaper();
        adaper.setGraphs(items);
        mRecyclerView.setAdapter(adaper);

        //set model
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        //check last date
        new DateHandler().checkLastDate(mStatsViewModel);
        mStatsViewModel.getAllStats().observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(@Nullable List<Stats> stats) {

            }
        });
        //check if tutorial should run
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

                                }

                                @Override
                                public void onEnded() {
                                    pref.edit().putBoolean(OVERVIEW_FRAG_RUN, false).apply();
                                }
                            });
                    spot.start();
                }

            });
        }
        return v;
    }

    /**
     * Builds targets for tutorial
     *
     * @param v View
     * @return List of targets
     */
    private ArrayList<SimpleTarget> buildTargets(View v) {
        ArrayList<SimpleTarget> targets = new ArrayList<>();

        SimpleTarget welcomeTarget = new SimpleTarget.Builder(getActivity())
                .setShape(new Circle(0))
                .setTitle(getString(R.string.overview_fragment_tutorial_statistics_title))
                .setDescription(getString(R.string.fragment_overview_statistics_des))
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

        targets.add(welcomeTarget);

        return targets;
    }

}
