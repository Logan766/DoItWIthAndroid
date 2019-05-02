package com.janhoracek.doitwithandroid.Home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateChangeChecker;
import com.janhoracek.doitwithandroid.Data.DateHandler;
import com.janhoracek.doitwithandroid.Database.ArchiveTaskViewModel;
import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.R;
import com.janhoracek.doitwithandroid.Tasks.TaskAdapterToday;
import com.janhoracek.doitwithandroid.UpdateableFragment;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.shape.RoundedRectangle;
import com.takusemba.spotlight.target.SimpleTarget;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.graphics.Color.rgb;

/**
 * Main Home Fragment, which contains user level, summary, time remaining and tasks to do today
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class HomeFragment extends Fragment{
    private static final String TAG = "IMDATE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String HOME_FRAG_RUN = "com.janhoracek.doitwithandroid.HOME_FRAG_RUN";
    private static final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";
    private static final String TIME_REMAINING = "com.janhoracek.doitwithandroid.TIME_REMAINING";


    private ViewPager mViewPager;
    private GraphPagerAdapter mAdapter;
    private SpringDotsIndicator mSpringDotsIndicator;
    private TaskAdapterToday adapter;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;
    private ArchiveTaskViewModel mArchiveTaskViewModel;
    private StatsViewModel mStatsViewModel;
    private TextView mTextViewTimeRemaining;
    private int minutesRemaining;
    private int hoursRemaining;

    List<Taskers> tempSave = new ArrayList<>();

    private List<Stats> mStats = new ArrayList<>();
    private Spotlight spotik;

    private boolean FirstRunCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        //check date
        DateChangeChecker.getInstance().CheckDate(pref);

        //load models
        mArchiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        new DateHandler().checkLastDate(mStatsViewModel);

        //check time remaining
        DateChangeChecker.getInstance().checkTimeRemaining(taskViewModel.getAllTasksList(), pref);

        mTextViewTimeRemaining = v.findViewById(R.id.text_view_time_remaining);
        mScrollView = v.findViewById(R.id.scroll_view_home);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        //set recycler view for today tasks
        adapter = new TaskAdapterToday();
        mRecyclerView.setAdapter(adapter);

        //set viewpager
        mSpringDotsIndicator = v.findViewById(R.id.spring_dots_indicator);
        mViewPager = v.findViewById(R.id.view_pager);
        mAdapter = new GraphPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mSpringDotsIndicator.setViewPager(mViewPager);

        //set time remaining
        mTextViewTimeRemaining.setText(getTimeRemaining(pref));

        //listen on changes in today tasks
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.SECOND, 0);
                calEnd.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, 0));
                calEnd.set(Calendar.MINUTE, pref.getInt(END_MINUTE, 0));
                DataHolder holder = DataHolder.getInstance();

                //load tasks based on doability
                if(!holder.getHighTasksDoable()) {
                    adapter.setTasks(taskViewModel.getTasksToday(taskViewModel.getAllTasksListByPriority(), pref));
                } else if (!holder.getMediumTasksDoable()) {
                    adapter.setTasks(taskViewModel.getTasksToday(taskViewModel.getMedHighPriorityForToday(taskers), pref));
                } else if (!holder.getAllTasksDoable()) {
                    adapter.setTasks(taskViewModel.getTasksToday(taskViewModel.getMediumLowPriorityForToday(taskers), pref));
                } else {
                    adapter.setTasks(taskViewModel.getTasksToday(taskers, pref));
                }
                //set time remaining
                mTextViewTimeRemaining.setText(getTimeRemaining(pref));
            }
        });

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

        ItemTouchHelper.SimpleCallback callback =  new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            //complete task
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long timeRemaining = pref.getLong(TIME_REMAINING, -1);
                long timeConsumed = 0;

                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(DateChangeChecker.getInstance().getTodayEnd(pref));
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(DateChangeChecker.getInstance().getTodayStart(pref));

                if(((calStart.getTimeInMillis() < new DateHandler().getCurrentDateTimeInMilisec()) && (new DateHandler().getCurrentDateTimeInMilisec() < calEnd.getTimeInMillis()))) {
                    Calendar calRelativeStart = Calendar.getInstance();
                    calRelativeStart.setTimeInMillis(calEnd.getTimeInMillis() - timeRemaining*60000);

                    timeConsumed = new DateHandler().getCurrentDateTimeInMilisec() - calRelativeStart.getTimeInMillis();
                    timeConsumed = timeConsumed / 60000;
                    timeRemaining = timeRemaining-timeConsumed;
                }

                pref.edit().putLong(TIME_REMAINING, timeRemaining).apply();
                int expGain;
                if(adapter.getTaskAt(viewHolder.getAdapterPosition()).getTo_be_done() > 0) {
                    Log.d(TAG, "Task je pulenej");
                    Taskers uTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                    Log.d(TAG, "Task cela doba: " + uTask.getTime_consumption());
                    int completed = uTask.getCompleted() + uTask.getTo_be_done();
                    Log.d(TAG, "Splnena doba: " + completed);
                    if(completed == uTask.getTime_consumption()) {
                        expGain = mStatsViewModel.completeTask(adapter.getTaskAt(viewHolder.getAdapterPosition()), true);
                        Log.d(TAG, "Task je dokoncenej celej");
                        taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                        Taskers helpTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                        ArchivedTasks archivTask = new ArchivedTasks(helpTask.getName(), helpTask.getDescription(), helpTask.getPriority(), helpTask.getTime_consumption(), helpTask.getD_time_milisec(), new DateHandler().getCurrentDateTimeInMilisec());
                        mArchiveTaskViewModel.insert(archivTask);
                    } else {
                        expGain = mStatsViewModel.completeTask(adapter.getTaskAt(viewHolder.getAdapterPosition()), false);
                        Log.d(TAG, "Tasku jsi splnil cast, nova completed je: " + completed);
                        uTask.setCompleted(completed);
                        taskViewModel.update(uTask);
                    }

                } else {
                    Log.d(TAG, "Task je celej");
                    expGain = mStatsViewModel.completeTask(adapter.getTaskAt(viewHolder.getAdapterPosition()), true);
                    taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                    Taskers helpTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                    if(timeConsumed == 0) {timeConsumed = helpTask.getTime_consumption();}
                    ArchivedTasks archivTask = new ArchivedTasks(helpTask.getName(), helpTask.getDescription(), helpTask.getPriority(), (int) timeConsumed, helpTask.getD_time_milisec(), new DateHandler().getCurrentDateTimeInMilisec());
                    mArchiveTaskViewModel.insert(archivTask);
                }

                UpdateableFragment fragment = (UpdateableFragment) mAdapter.getFragment(0);
                if(fragment == null) return;
                fragment.updateProgress(expGain, getContext());


                Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.coord_layout),
                        getString(R.string.home_fragment_task_done), Snackbar.LENGTH_LONG);
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                        snack.getView().getLayoutParams();
                params.setMargins(0, 0, 0, getActivity().findViewById(R.id.bottom_navigation).getHeight());
                snack.getView().setLayoutParams(params);
                snack.show();
                //Snackbar.make(getActivity().findViewById(android.R.id.content), "Task done! Good work!", Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                        .addSwipeLeftActionIcon(R.drawable.ic_check_black_24dp)
                        .addSwipeLeftLabel(getString(R.string.complete_task))
                        .setSwipeLeftLabelColor(Color.BLACK)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        FirstRunCheck = pref.getBoolean(HOME_FRAG_RUN, true);
        //if(FirstRunCheck) {
        if(FirstRunCheck) {
            saveTempTasks();
            taskViewModel.deleteAllTasks();
            taskViewModel.insert(new Taskers(getString(R.string.tutorial_title_call_mom), getString(R.string.tutorial_title_call_mom_des), 1, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
            taskViewModel.insert(new Taskers(getString(R.string.tutorial_buy_milk_title), getString(R.string.tutorial_buy_milk_des), 2, 15, 28, 5, 2019, "18:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
            taskViewModel.insert(new Taskers(getString(R.string.tutorial_go_to_work_title), getString(R.string.tutorial_go_to_work_des), 3, 1440, 5, 8, 2019, "8:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
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
                                    mViewPager.setCurrentItem(0);
                                    reloadTasks();
                                    pref.edit().putBoolean(HOME_FRAG_RUN, false).apply();
                                }
                            });
                    spot.start();
                }

            });
        }

        return v;
    }

    private String getTimeRemaining(SharedPreferences pref) {
        long minutes = pref.getLong(TIME_REMAINING, -1) % 60;
        long hours = pref.getLong(TIME_REMAINING, -1) / 60;
        return hours + getString(R.string.hours_short) + " " +  minutes + getString(R.string.minutes_short);
    }

    private void saveTempTasks() {
        tempSave = taskViewModel.getAllTasksList();
    }

    private void reloadTasks() {
        for(int i = 0; i <= tempSave.size()-1; i++) {
            taskViewModel.insert(tempSave.get(i));
        }
    }

    private ArrayList<SimpleTarget> buildTargets(View v) {
        ArrayList<SimpleTarget> targets = new ArrayList<>();

        SimpleTarget welcomeTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_welcome_title))
                .setDescription(getString(R.string.home_fragment_tutorial_welcome_des))
                .setShape(new Circle(0f))
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

       targets.add(welcomeTarget);

        SimpleTarget menuTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_menu_title))
                .setDescription(getString(R.string.home_fragment_tutorial_menu_des))
                .setPoint(v.getRootView().findViewById(R.id.bottom_navigation))
                .setShape(new RoundedRectangle(v.getRootView().findViewById(R.id.bottom_navigation).getHeight(), v.getWidth(), 5f))
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

        targets.add(menuTarget);

        SimpleTarget levelTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_overall_lvl_title))
                .setDescription(getString(R.string.home_fragment_tutorial_overall_lvl_des))
                .setOverlayPoint(0, v.findViewById(R.id.recycler_view).getY())
                .setPoint(v.findViewById(R.id.arcProgressStackViewLevel))
                .setShape(new Circle(((v.findViewById(R.id.arcProgressStackViewLevel).getWidth() / 2f)) + 10))
                .build();

        targets.add(levelTarget);

        SimpleTarget experienceTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_exp_title))
                .setDescription(getString(R.string.home_fragment_tutorial_exp_des))
                .setOverlayPoint(0, v.findViewById(R.id.recycler_view).getY())
                .setPoint(v.findViewById(R.id.experience_layout))
                .setShape(new RoundedRectangle(v.findViewById(R.id.experience_layout).getHeight(), v.findViewById(R.id.experience_layout).getWidth(), 5f))
                .build();

        targets.add(experienceTarget);

        SimpleTarget timeRemainingTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_time_remaining_title))
                .setDescription(getString(R.string.home_fragment_tutorial_time_remaining_des))
                .setOverlayPoint(0, 250f)
                .setPoint(v.findViewById(R.id.text_view_time_remaining))
                .setShape(new RoundedRectangle(v.findViewById(R.id.text_view_time_remaining).getHeight(), v.findViewById(R.id.text_view_time_remaining).getWidth() + 50f, 5f))
                .build();

        targets.add(timeRemainingTarget);

        SimpleTarget normalTaskTarget = new SimpleTarget.Builder(getActivity())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        for(int i = 0; i<=taskViewModel.getAllTasksList().size() -1; i++) {
                            mRecyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);
                        }
                        //mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);
                        //mRecyclerView.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);
                        //mRecyclerView.findViewHolderForAdapterPosition(2).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);

                        mScrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator.ofInt(mScrollView, "scrollY",  mScrollView.getBottom()).setDuration(6000).start();
                                //mScrollView.smoothScrollTo(0, mScrollView.getBottom());
                            }
                        },100);
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                })
                .setTitle(getString(R.string.home_fragment_tutorial_todo_tasks_title))
                .setDescription(getString(R.string.home_fragment_tutorial_todo_tasks_des))
                .setOverlayPoint(0f, 250f)
                .setPoint(v.findViewById(R.id.recycler_view))
                .setShape(new RoundedRectangle(v.findViewById(R.id.recycler_view).getHeight() * 2, v.getWidth(), 5f))
                .build();

        targets.add(normalTaskTarget);

        SimpleTarget grayTaskTarget = new SimpleTarget.Builder(getActivity())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        for(int i = 0; i<=taskViewModel.getAllTasksList().size() -1; i++) {
                            mRecyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));
                        }
                        /*
                        mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));
                        mRecyclerView.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));
                        mRecyclerView.findViewHolderForAdapterPosition(2).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));
                        */


                        mScrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ObjectAnimator.ofInt(mScrollView, "scrollY",  mScrollView.getTop()).setDuration(3000).start();
                                //mScrollView.smoothScrollTo(0, mScrollView.getBottom());
                            }
                        },300);
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                })
                .setTitle(getString(R.string.home_fragment_tutorial_gray_tasks_title))
                .setDescription(getString(R.string.home_fragment_tutorial_gray_tasks_des))
                .setOverlayPoint(0f, 250f)
                .setPoint(v.findViewById(R.id.recycler_view))
                .setShape(new RoundedRectangle(v.findViewById(R.id.recycler_view).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(grayTaskTarget);

        SimpleTarget partedTaskTarget = new SimpleTarget.Builder(getActivity())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        Taskers tempTask = taskViewModel.getAllTasksListByPriority().get(0);
                        Taskers updated = new Taskers(tempTask.getName(), tempTask.getDescription(), tempTask.getPriority(), 1440, tempTask.getD_day(), tempTask.getD_month(), tempTask.getD_year(), tempTask.getD_time(), tempTask.getD_time_milisec(), tempTask.getTo_be_done(), tempTask.getCompleted());
                        updated.setId(tempTask.getId());
                        taskViewModel.update(updated);

                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        //taskViewModel.deleteAllTasks();
                    }
                })
                .setTitle(getString(R.string.home_fragment_tutorial_more_parts_title))
                .setDescription(getString(R.string.home_fragment_tutorial_more_parts_des))
                .setOverlayPoint(0f, 250f)
                .setPoint(v.findViewById(R.id.recycler_view))
                .setShape(new RoundedRectangle(v.findViewById(R.id.recycler_view).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(partedTaskTarget);

        SimpleTarget overviewTarget = new SimpleTarget.Builder(getActivity())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        mViewPager.setCurrentItem(1);

                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                        //taskViewModel.deleteAllTasks();
                    }
                })
                .setTitle(getString(R.string.home_fragment_tutorial_overview_title))
                .setDescription(getString(R.string.home_fragment_tutorial_overview_des))
                .setOverlayPoint(0f ,v.findViewById(R.id.recycler_view).getY())
                .setPoint(v.findViewById(R.id.view_pager))
                .setShape(new RoundedRectangle(v.findViewById(R.id.view_pager).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(overviewTarget);


        SimpleTarget allTaskTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_overview_all_title))
                .setDescription(getString(R.string.home_fragment_tutorial_overview_all_des))
                .setOverlayPoint(0f ,v.findViewById(R.id.recycler_view).getY())
                .setPoint(v.getWidth() / 2f, v.findViewById(R.id.view_pager).getHeight() * 0.45f)
                .setShape(new RoundedRectangle(240f, v.getWidth(), 5f))
                .build();

        targets.add(allTaskTarget);

        SimpleTarget mediumTaskTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_overview_med_title))
                .setDescription(getString(R.string.home_fragment_tutorial_overview_med_des))
                .setOverlayPoint(0f ,v.findViewById(R.id.recycler_view).getY() - 200f)
                .setPoint(v.getWidth() / 2f, v.findViewById(R.id.view_pager).getHeight() * 0.7f)
                .setShape(new RoundedRectangle(240f, v.getWidth(), 5f))
                .build();

        targets.add(mediumTaskTarget);

        SimpleTarget highTaskTarget = new SimpleTarget.Builder(getActivity())
                .setTitle(getString(R.string.home_fragment_tutorial_overview_high_title))
                .setDescription(getString(R.string.home_fragment_tutorial_overview_high_des))
                .setOverlayPoint(0f ,v.findViewById(R.id.recycler_view).getY() - 200f)
                .setPoint(v.getWidth() /2f, v.findViewById(R.id.view_pager).getHeight() * 0.95f)
                .setShape(new RoundedRectangle(240f, v.getWidth(), 5f))
                .build();

        targets.add(highTaskTarget);



        return targets;
    }

}

