package com.janhoracek.doitwithandroid.Tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateHandler;
import com.janhoracek.doitwithandroid.Database.ArchiveTaskViewModel;
import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.R;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.takusemba.spotlight.target.SimpleTarget;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class FragmentTasks extends Fragment {
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
    private ArchiveTaskViewModel mArchiveTaskViewModel;


    private LottieAnimationView mLottieAnimationViewAll;
    private LottieAnimationView mLottieAnimationViewMedium;
    private LottieAnimationView mLottieAnimationViewHigh;
    private LottieAnimationView mLottieAnimationViewDeadline;
    private Tooltip mTooltip;

    private boolean FirstRunCheck;
    private boolean tutorialRunning = false;

    List<Taskers> tempSave = new ArrayList<>();
    List<ArchivedTasks> tempSaveArchive = new ArrayList<>();


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
        mArchiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);

        mTooltip = new Tooltip.Builder(mLottieAnimationViewDeadline)
                .setText(getString(R.string.fragment_tasks_deadline_after))
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

        if(DataHolder.getInstance().getAllTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("success.json");
            mLottieAnimationViewAll.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("not_success.json");
            mLottieAnimationViewAll.playAnimation();
        }

        if(DataHolder.getInstance().getMediumTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("success.json");
            mLottieAnimationViewMedium.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("not_success.json");
            mLottieAnimationViewMedium.playAnimation();
        }

        if(DataHolder.getInstance().getHighTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("success.json");
            mLottieAnimationViewHigh.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("not_success.json");
            mLottieAnimationViewHigh.playAnimation();
        }

        if(DataHolder.getInstance().getDeadlinesDoable()) {
            mLottieAnimationViewDeadline.setVisibility(View.GONE);
        } else {
            mLottieAnimationViewDeadline.setVisibility(View.VISIBLE);
        }


        new DateHandler().checkLastDate(mStatsViewModel);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = v.findViewById(R.id.view_pager_tasks);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = v.findViewById(R.id.tab_layout_tasks);
        tabLayout.setupWithViewPager(mViewPager);

        FirstRunCheck = pref.getBoolean(TASKS_FRAG_RUN, true);
        //if(FirstRunCheck) {
        if(FirstRunCheck) {
            tutorialRunning = true;
            saveTempTasks();
            taskViewModel.deleteAllTasks();
            mArchiveTaskViewModel.deleteAllTasks();
            taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_call_mom_title), getString(R.string.tasks_fragment_tutorial_call_mom_des), 1, 30, 26, 5, 2019, "14:00", (new DateHandler().getCurrentDateTimeInMilisec()+3600000), 0, 0));
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
                                    //Toast.makeText(getContext(), "spotlight is started", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onEnded() {
                                    //Toast.makeText(getActivity(), "spotlight is ended", Toast.LENGTH_SHORT).show();
                                    taskViewModel.deleteAllTasks();
                                    mArchiveTaskViewModel.deleteAllTasks();
                                    tutorialRunning = false;
                                    reloadTasks();
                                    pref.edit().putBoolean(TASKS_FRAG_RUN, false).apply();
                                }
                            });
                    spot.start();
                }

            });
        }

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(tutorialRunning) {
            taskViewModel.deleteAllTasks();
            reloadTasks();
        }
    }

    private ArrayList<SimpleTarget> buildTargets(View v) {
        ArrayList<SimpleTarget> targets = new ArrayList<>();

        SimpleTarget welcomeTarget = new SimpleTarget.Builder(getActivity())
                .setShape(new Circle(0))
                .setTitle(getString(R.string.tasks_fragment_tutorial_welcome_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_welcome_des))
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

        targets.add(welcomeTarget);

        SimpleTarget currentTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_tasks_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_tasks_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() *2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        taskViewModel.deleteAllTasks();
                        taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_call_mom_title), getString(R.string.tasks_fragment_tutorial_call_mom_des), 1, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() + 3600000, 0, 15));
                    }
                })
                .build();

        targets.add(currentTasksTarget);

        SimpleTarget partedTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_parted_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_parted_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() *2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(partedTasksTarget);

        SimpleTarget swipingTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_edit_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_edit_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() *2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        taskViewModel.deleteAllTasks();
                        taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_call_mom_title), getString(R.string.tasks_fragment_tutorial_call_mom_des), 1, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() - 3600, 0, 0));
                    }
                })
                .build();

        targets.add(swipingTasksTarget);

        SimpleTarget deadlineTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_deadline_after_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_deadline_after_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() *2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        taskViewModel.deleteAllTasks();
                        taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_pay_bills_title), getString(R.string.tasks_fragment_tutorial_pay_bills_des), 2, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() + 3600, 0, 0));
                    }
                })
                .build();

        targets.add(deadlineTasksTarget);

        SimpleTarget deadlineUndoableTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_udnoable_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_undoable_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() *2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        taskViewModel.deleteAllTasks();
                    }
                })
                .build();

        targets.add(deadlineUndoableTasksTarget);

        SimpleTarget focusTasksTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_focus_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_focus_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() *2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_call_mom_title), getString(R.string.tasks_fragment_tutorial_call_mom_des), 1, 15, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() + 1000000, 0, 0));
                        taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_buy_milk_title), getString(R.string.tasks_fragment_tutorial_buy_milk_des), 2, 150, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() + 1000000, 0, 0));
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                })
                .build();

        targets.add(focusTasksTarget);

        SimpleTarget archiveTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_archive_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_archive_des))
                .setOverlayPoint(0, v.findViewById(R.id.task_fragment_recyclerview).getY() + v.findViewById(R.id.task_fragment_recyclerview).getHeight() * 2)
                .setPoint(v.findViewById(R.id.task_fragment_recyclerview))
                .setShape(new RoundedRectangle(v.findViewById(R.id.task_fragment_recyclerview).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        mViewPager.setCurrentItem(1);
                        mArchiveTaskViewModel.insert(new ArchivedTasks(getString(R.string.tasks_fragment_tutorial_call_mom_title), getString(R.string.tasks_fragment_tutorial_call_mom_des), 1, 120, new DateHandler().getCurrentDateTimeInMilisec(), new DateHandler().getCurrentDateTimeInMilisec() + 7200000));
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        mViewPager.setCurrentItem(0);
                    }
                })
                .build();

        targets.add(archiveTarget);

        SimpleTarget topScreenTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_top_screen_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_top_screen_des))
                .setOverlayPoint(0, v.getWidth() /2f)
                .setPoint(v.findViewById(R.id.fragment_both_status))
                .setShape(new RoundedRectangle(v.findViewById(R.id.fragment_both_status).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(topScreenTarget);

        SimpleTarget topScreenDeadlineTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.tasks_fragment_tutorial_deadline_alert_title))
                .setDescription(getString(R.string.tasks_fragment_tutorial_deadline_alert_des))
                .setOverlayPoint(0, v.getWidth() /2f)
                .setPoint(v.findViewById(R.id.fragment_both_status))
                .setShape(new RoundedRectangle(v.findViewById(R.id.fragment_both_status).getHeight(), v.getWidth(), 5f))
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        taskViewModel.deleteAllTasks();
                        taskViewModel.insert(new Taskers(getString(R.string.tasks_fragment_tutorial_call_mom_title), getString(R.string.tasks_fragment_tutorial_call_mom_des), 1, 15, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() - 3600, 0, 0));
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                })
                .build();

        targets.add(topScreenDeadlineTarget);

        return targets;
    }

    private void saveTempTasks() {
        tempSave = taskViewModel.getAllTasksList();
        tempSaveArchive = mArchiveTaskViewModel.getAllTasksList();
    }

    private void reloadTasks() {
        for(int i = 0; i <= tempSave.size()-1; i++) {
            taskViewModel.insert(tempSave.get(i));
        }
        for(int i = 0; i <= tempSaveArchive.size()-1; i++) {
            mArchiveTaskViewModel.insert(tempSaveArchive.get(i));
        }
    }

    public void redrawLottie() {
        if(DataHolder.getInstance().getAllTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("success.json");
            mLottieAnimationViewAll.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("not_success.json");
            mLottieAnimationViewAll.playAnimation();
        }

        if(DataHolder.getInstance().getMediumTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("success.json");
            mLottieAnimationViewMedium.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("not_success.json");
            mLottieAnimationViewMedium.playAnimation();
        }

        if(DataHolder.getInstance().getHighTasksDoable()) {
            //Log.d(TAG, "Chart holder pro Lotku jest asi true: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("success.json");
            mLottieAnimationViewHigh.playAnimation();
        } else {
            //Log.d(TAG, "Chart holder pro Lotku jest asi false: " + DataHolder.getInstance().getAllTasksDoable());
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("not_success.json");
            mLottieAnimationViewHigh.playAnimation();
        }


        Log.d("DDLINE", "Deadline doable: " + DataHolder.getInstance().getDeadlinesDoable());

        if(DataHolder.getInstance().getDeadlinesDoable()) {
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
                    return getString(R.string.fragment_tasks_current_tasks);
                case 1:
                    return getString(R.string.fragment_tasks_archived_tasks);
            }
            return null;
        }
    }
}
