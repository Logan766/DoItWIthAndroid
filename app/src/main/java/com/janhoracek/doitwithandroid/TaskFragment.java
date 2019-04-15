package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.takusemba.spotlight.target.SimpleTarget;
import com.tooltip.Tooltip;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String TASKS_FRAG_RUN = "com.janhoracek.doitwithandroid.TASKS_FRAG_RUN";

    private static final String TAG = "LOTKA";

    private FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterAll mAdapterAll;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private StatsViewModel mStatsViewModel;


    private LottieAnimationView mLottieAnimationViewAll;
    private LottieAnimationView mLottieAnimationViewMedium;
    private LottieAnimationView mLottieAnimationViewHigh;
    private LottieAnimationView mLottieAnimationViewDeadline;
    private Tooltip mTooltip;

    private boolean FirstRunCheck;

    List<Taskers> tempSave = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_task_both, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mLottieAnimationViewAll = v.findViewById(R.id.lottie_all);
        mLottieAnimationViewMedium = v.findViewById(R.id.lottie_medium);
        mLottieAnimationViewHigh = v.findViewById(R.id.lottie_high);
        mLottieAnimationViewDeadline = v.findViewById(R.id.lottie_deadline);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        mTooltip = new Tooltip.Builder(mLottieAnimationViewDeadline)
                .setText("Some tasks are after their deadline")
                .setBackgroundColor(getResources().getColor(R.color.backgroundNormal))
                .setTextColor(getResources().getColor(R.color.colorAccent))
                .setDismissOnClick(true)
                .setCornerRadius(20f)
                .setCancelable(true)
                .build();


        mLottieAnimationViewDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mTooltip.show();

            }
        });

        if(ChartDataHolder.getInstance().getAllTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("success.json");
            mLottieAnimationViewAll.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("not_success.json");
            mLottieAnimationViewAll.playAnimation();
        }

        if(ChartDataHolder.getInstance().getMediumTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("success.json");
            mLottieAnimationViewMedium.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("not_success.json");
            mLottieAnimationViewMedium.playAnimation();
        }

        if(ChartDataHolder.getInstance().getHighTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("success.json");
            mLottieAnimationViewHigh.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("not_success.json");
            mLottieAnimationViewHigh.playAnimation();
        }

        if(ChartDataHolder.getInstance().getDeadlinesDoable()) {
            mLottieAnimationViewDeadline.setVisibility(View.GONE);
        } else {
            mLottieAnimationViewDeadline.setVisibility(View.VISIBLE);
        }





        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        new DateHandler().checkLastDate(mStatsViewModel);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = v.findViewById(R.id.view_pager_tasks);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = v.findViewById(R.id.tab_layout_tasks);
        tabLayout.setupWithViewPager(mViewPager);

        FirstRunCheck = pref.getBoolean(TASKS_FRAG_RUN, true);
        //if(FirstRunCheck) {
        if(true) {
            saveTempTasks();
            taskViewModel.deleteAllTasks();
            taskViewModel.insert(new Taskers("Call mom", "Tell mom to have a nice day", 1, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() + 660000, 0, 0));
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
                                    taskViewModel.deleteAllTasks();
                                    //mViewPager.setCurrentItem(0);
                                    reloadTasks();
                                    //pref.edit().putBoolean(TASKS_FRAG_RUN, false).apply();
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
                .setTitle("Task screen")
                .setDescription("Welcome to task screen. There you can find all your tasks. From this screen you can add tasks, see status of every task and change current tasks")
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

        //targets.add(welcomeTarget);

        SimpleTarget currentTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle("Tasks")
                .setDescription("Here you can see all of your unfinished tasks. You can see their title, description, deadline and on some of them there is more information. You will learn about this in short time.")
                .setOverlayPoint(0, v.getHeight() / 2f)
                .setPoint(v.findViewById(R.id.view_pager_tasks))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(currentTasksTarget);

        return targets;
    }

    private void saveTempTasks() {
        tempSave = taskViewModel.getAllTasksList();
    }

    private void reloadTasks() {
        for(int i = 0; i <= tempSave.size()-1; i++) {
            taskViewModel.insert(tempSave.get(i));
        }
    }

    public void redrawLottie() {
        if(ChartDataHolder.getInstance().getAllTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("success.json");
            mLottieAnimationViewAll.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("not_success.json");
            mLottieAnimationViewAll.playAnimation();
        }

        if(ChartDataHolder.getInstance().getMediumTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("success.json");
            mLottieAnimationViewMedium.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("not_success.json");
            mLottieAnimationViewMedium.playAnimation();
        }

        if(ChartDataHolder.getInstance().getHighTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("success.json");
            mLottieAnimationViewHigh.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + ChartDataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("not_success.json");
            mLottieAnimationViewHigh.playAnimation();
        }


        Log.d("DDLINE", "Deadline doable: " + ChartDataHolder.getInstance().getDeadlinesDoable());

        if(ChartDataHolder.getInstance().getDeadlinesDoable()) {
            mLottieAnimationViewDeadline.setVisibility(View.GONE);
        } else {
            mLottieAnimationViewDeadline.setVisibility(View.VISIBLE);
            mLottieAnimationViewDeadline.playAnimation();
        }

        mLottieAnimationViewDeadline.invalidate();
        mLottieAnimationViewAll.invalidate();
        mLottieAnimationViewMedium.invalidate();
        mLottieAnimationViewHigh.invalidate();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FragmentCurrentTasks();
                    Log.d("FRAGS", "Ted se prepnul item ");
                    break;
                case 1:
                    fragment = new FragmentArchivedTasks();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Current Tasks";
                case 1:
                    return "Archived Tasks";
            }
            return null;
        }
    }
}
