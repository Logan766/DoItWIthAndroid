package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.takusemba.spotlight.target.CustomTarget;
import com.takusemba.spotlight.target.SimpleTarget;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//public class HomeFragment extends Fragment implements View.OnClickListener
public class HomeFragment extends Fragment{
    private static final String TAG = "DIWD1";
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String HOME_FRAG_RUN = "com.janhoracek.doitwithandroid.HOME_FRAG_RUN";
    private static final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";


    private ViewPager mViewPager;
    private GraphPagerAdapter mAdapter;
    private SpringDotsIndicator mSpringDotsIndicator;
    private TaskAdapterToday adapter;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;

    private StatsViewModel mStatsViewModel;
    private List<Stats> mStats = new ArrayList<>();
    private Spotlight spotik;

    private boolean FirstRunCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                adapter.setTasks(taskers);
            }
        });


        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        ChartDataHolder.getInstance().setmLineChartData(mStatsViewModel.getAllStatsList());

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        adapter = new TaskAdapterToday();
        mRecyclerView.setAdapter(adapter);

        mSpringDotsIndicator = v.findViewById(R.id.spring_dots_indicator);
        mViewPager = v.findViewById(R.id.view_pager);
        mAdapter = new GraphPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mSpringDotsIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                UpdateableFragment fragment = (UpdateableFragment) mAdapter.getFragment(i);
                if(fragment == null) return;
                fragment.update();
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int expGain = mStatsViewModel.completeTask(adapter.getTaskAt(viewHolder.getAdapterPosition()), mStatsViewModel);
                UpdateableFragment fragment = (UpdateableFragment) mAdapter.getFragment(0);
                if(fragment == null) return;
                fragment.updateProgress(expGain, getContext());
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(mRecyclerView);

        FirstRunCheck = pref.getBoolean(HOME_FRAG_RUN, true);
        //if(FirstRunCheck) {
        if(false) {
            pref.edit().putBoolean(HOME_FRAG_RUN, false).apply();
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
        int[] location = new int[2];

        mRecyclerView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        SimpleTarget simpleTarget = new SimpleTarget.Builder(getActivity())
                .setPoint(x + v.getWidth() / 2 , y)
                .setShape(new RoundedRectangle(100f, v.getWidth(), 5f)) // or RoundedRectangle()
                .setDuration(1000L)
                .setTitle("Tasks to do today")
                .setDescription("Here you can see tasks that you should do today, simply swipe in any direction to mark them as completed")
                .setOverlayPoint(mRecyclerView.getX(), mRecyclerView.getY())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        // do something
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        // do something
                    }
                })
                .build();
        targets.add(simpleTarget);

        return targets;
    }

}

