package com.janhoracek.doitwithandroid;

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
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.janhoracek.doitwithandroid.Database.ArchiveTaskViewModel;
import com.janhoracek.doitwithandroid.Database.ArchivedTasks;
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

import static android.graphics.Color.rgb;

//public class HomeFragment extends Fragment implements View.OnClickListener
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

    List<Taskers> tempSave = new ArrayList<>();

    private List<Stats> mStats = new ArrayList<>();
    private Spotlight spotik;

    private boolean FirstRunCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        DateChangeChecker.getInstance().CheckDate(pref);


        mArchiveTaskViewModel = ViewModelProviders.of(this).get(ArchiveTaskViewModel.class);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        new DateHandler().checkLastDate(mStatsViewModel);

        Log.d("PRDEL", "time remain " + pref.getLong(TIME_REMAINING, -1));
        DateChangeChecker.getInstance().checkTimeRemaining(taskViewModel.getAllTasksList(), pref);

        mScrollView = v.findViewById(R.id.scroll_view_home);
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


        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                //adapter.setTasks(taskers);
                Calendar calEnd = Calendar.getInstance();
                calEnd.set(Calendar.SECOND, 0);
                calEnd.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, 0));
                calEnd.set(Calendar.MINUTE, pref.getInt(END_MINUTE, 0));
                //if (new DateHandler().getCurrentDateTimeInMilisec() < calEnd.getTimeInMillis() && new DateHandler().getCurrentDateTimeInMilisec() > ) {adapter.setTasks(taskViewModel.getTasksToday(taskers, pref));}
                ChartDataHolder holder = ChartDataHolder.getInstance();
                if(!holder.getHighTasksDoable()) {
                    adapter.setTasks(taskViewModel.getTasksToday(taskViewModel.getAllTasksListByPriority(), pref));
                } else if (!holder.getMediumTasksDoable()) {
                    adapter.setTasks(taskViewModel.getTasksToday(taskViewModel.getHighPriority(taskers), pref));
                } else if (!holder.getAllTasksDoable()) {
                    adapter.setTasks(taskViewModel.getTasksToday(taskViewModel.getMediumHighPriority(taskers), pref));
                } else {
                    adapter.setTasks(taskViewModel.getTasksToday(taskers, pref));
                }

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

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int expGain = mStatsViewModel.completeTask(adapter.getTaskAt(viewHolder.getAdapterPosition()));

                UpdateableFragment fragment = (UpdateableFragment) mAdapter.getFragment(0);
                if(fragment == null) return;

                fragment.updateProgress(expGain, getContext());


                long timeRemaining = pref.getLong(TIME_REMAINING, -1);
                long timeConsumed = 0;

                /////
                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(DateChangeChecker.getInstance().getTodayEnd(pref));
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(DateChangeChecker.getInstance().getTodayStart(pref));

                if(((calStart.getTimeInMillis() < new DateHandler().getCurrentDateTimeInMilisec()) && (new DateHandler().getCurrentDateTimeInMilisec() < calEnd.getTimeInMillis()))) {
                    Log.d(TAG, "Task delas v pracovni dobe");
                    Calendar calRelativeStart = Calendar.getInstance();
                    calRelativeStart.setTimeInMillis(calEnd.getTimeInMillis() - timeRemaining*60000);

                    timeConsumed = new DateHandler().getCurrentDateTimeInMilisec() - calRelativeStart.getTimeInMillis();
                    timeConsumed = timeConsumed / 60000;

                    Log.d(TAG, "Zbyva dneska casu: " + timeRemaining);

                    Log.d(TAG, "Po tom cos dokoncil ukol zbyva dnes casu: " + (timeRemaining - timeConsumed));
                    timeRemaining = timeRemaining-timeConsumed;
                }



                ///////

                //timeRemaining -= adapter.getTaskAt(viewHolder.getAdapterPosition()).getTime_consumption();
                pref.edit().putLong(TIME_REMAINING, timeRemaining).apply();

                if(adapter.getTaskAt(viewHolder.getAdapterPosition()).getTo_be_done() > 0) {
                    Log.d(TAG, "Task je pulenej");
                    Taskers uTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                    Log.d(TAG, "Task cela doba: " + uTask.getTime_consumption());
                    int completed = uTask.getCompleted() + uTask.getTo_be_done();
                    Log.d(TAG, "Splnena doba: " + completed);
                    if(completed == uTask.getTime_consumption()) {
                        Log.d(TAG, "Task je dokoncenej celej");
                        taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                        Taskers helpTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                        ArchivedTasks archivTask = new ArchivedTasks(helpTask.getName(), helpTask.getDescription(), helpTask.getPriority(), helpTask.getTime_consumption(), helpTask.getD_time_milisec(), new DateHandler().getCurrentDateTimeInMilisec());
                        mArchiveTaskViewModel.insert(archivTask);
                    } else {
                        Log.d(TAG, "Tasku jsi splnil cast, nova completed je: " + completed);
                        uTask.setCompleted(completed);
                        taskViewModel.update(uTask);
                    }

                } else {
                    Log.d(TAG, "Task je celej");
                    taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                    Taskers helpTask = adapter.getTaskAt(viewHolder.getAdapterPosition());
                    if(timeConsumed == 0) {timeConsumed = helpTask.getTime_consumption();}
                    ArchivedTasks archivTask = new ArchivedTasks(helpTask.getName(), helpTask.getDescription(), helpTask.getPriority(), (int) timeConsumed, helpTask.getD_time_milisec(), new DateHandler().getCurrentDateTimeInMilisec());
                    mArchiveTaskViewModel.insert(archivTask);
                }

                Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.coord_layout),
                        "Task done! Good work!", Snackbar.LENGTH_LONG);
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
                        .addSwipeLeftLabel("Complete task")
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
        if(false) {
            pref.edit().putBoolean(HOME_FRAG_RUN, false).apply();
            v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    List<Taskers> tempSave = new ArrayList<>();
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
                                    saveTempTasks();
                                    taskViewModel.deleteAllTasks();
                                    taskViewModel.insert(new Taskers("Call mom", "Tell mom to have a nice day", 1, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
                                    taskViewModel.insert(new Taskers("Buy milk", "Buy the freshest milk I can get", 2, 15, 28, 5, 2019, "18:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
                                    taskViewModel.insert(new Taskers("Go to work", "Make some money", 3, 1440, 5, 8, 2019, "8:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
                                }

                                @Override
                                public void onEnded() {
                                    Toast.makeText(getActivity(), "spotlight is ended", Toast.LENGTH_SHORT).show();
                                    taskViewModel.deleteAllTasks();
                                    reloadTasks();
                                }
                            });
                    spot.start();
                }

            });
        }

        return v;
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
                .setTitle("Welcome")
                .setDescription("Welcome to DoItWithAndroid, since this is your first run, lets take a look on how to use this awesome app")
                .setShape(new Circle(0f))
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

       targets.add(welcomeTarget);

        SimpleTarget menuTarget = new SimpleTarget.Builder(getActivity())
                .setTitle("Menu")
                .setDescription("This is the main menu, use it to go through application")
                .setPoint(v.getRootView().findViewById(R.id.bottom_navigation))
                .setShape(new RoundedRectangle(v.getRootView().findViewById(R.id.bottom_navigation).getHeight(), v.getWidth(), 5f))
                .setOverlayPoint(0, v.getHeight() / 2f)
                .build();

        targets.add(menuTarget);

        SimpleTarget levelTarget = new SimpleTarget.Builder(getActivity())
                .setTitle("Overall level")
                .setDescription("This is your level. You will earn experience by completing your tasks. Try to complete as many tasks as you can a become a high level effective person!")
                .setOverlayPoint(0, v.findViewById(R.id.recycler_view).getY())
                .setPoint(v.findViewById(R.id.arcProgressStackViewLevel))
                .setShape(new Circle(((v.findViewById(R.id.arcProgressStackViewLevel).getWidth() / 2f)) + 10))
                .build();

        targets.add(levelTarget);

        SimpleTarget experienceTarget = new SimpleTarget.Builder(getActivity())
                .setTitle("Experience")
                .setDescription("This is your current experience and experience you need to reach new level. Every new level will require more experience than the last, it will get harder over time, but you can do it!")
                .setOverlayPoint(0, v.findViewById(R.id.recycler_view).getY())
                .setPoint(v.findViewById(R.id.experience_layout))
                .setShape(new RoundedRectangle(v.findViewById(R.id.experience_layout).getHeight(), v.findViewById(R.id.experience_layout).getWidth(), 5f))
                .build();

        targets.add(experienceTarget);

        SimpleTarget normalTaskTarget = new SimpleTarget.Builder(getActivity())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);
                        mRecyclerView.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);
                        mRecyclerView.findViewHolderForAdapterPosition(2).itemView.findViewById(R.id.today_background).setBackgroundColor(Color.WHITE);

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
                .setTitle("Tasks to do today")
                .setDescription("There you can find tasks you should do today. You do not have to complete them in given order, order is just recommended. Every task have its own color based on its priority. RED means high, YELLOW is medium and GREEN is low. Experience gained per task is based on priority and its duration. When you complete task, simply swipe left to mark it as done.")
                .setOverlayPoint(0f, 100f)
                .setPoint(v.findViewById(R.id.recycler_view))
                .setShape(new RoundedRectangle(v.findViewById(R.id.recycler_view).getHeight() * 2, v.getWidth(), 5f))
                .build();

        targets.add(normalTaskTarget);

        SimpleTarget grayTaskTarget = new SimpleTarget.Builder(getActivity())
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                        //taskViewModel.insert(new Taskers("Call mom", "Tell mom to have a nice day", 1, 30, 26, 5, 2019, "14:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
                        //taskViewModel.insert(new Taskers("Buy milk", "Buy the freshest milk I can get", 2, 15, 28, 5, 2019, "18:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
                        //taskViewModel.insert(new Taskers("Go to work", "Make some money", 3, 15, 5, 8, 2019, "8:00", new DateHandler().getCurrentDateTimeInMilisec() - 60000, 0, 0));
                        mRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));
                        mRecyclerView.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));
                        mRecyclerView.findViewHolderForAdapterPosition(2).itemView.findViewById(R.id.today_background).setBackgroundColor(rgb(200, 200, 200));

                        /*
                        mScrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        },100);*/
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                })
                .setTitle("Gray tasks")
                .setDescription("Gray tasks are same as normal tasks, only difference is that when your tasks are grayed out, the current time is not within you working time. You can still complete task as usual, but planner will not count their duration to your working time left today, also their completion time will be same as their estimated time. Feel free to complete task out of your working time, planner will recalculate your working plan :) ")
                .setOverlayPoint(0f, 100f)
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
                .setTitle("Tasks on more parts")
                .setDescription("If you do not have enough working time left today to fit in the whole task or you have task that is longer than you daily hours, you will still see this task on this list. Difference is, that you will this task with number of percent, which represents the part that will be completed when you mark this task as done")
                .setOverlayPoint(0f, 100f)
                .setPoint(v.findViewById(R.id.recycler_view))
                .setShape(new RoundedRectangle(v.findViewById(R.id.recycler_view).getHeight(), v.getWidth(), 5f))
                .build();

        targets.add(partedTaskTarget);


        /*
        int[] location = new int[2];

        mRecyclerView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        SimpleTarget mRecyclerViewTarget = new SimpleTarget.Builder(getActivity())
                .setPoint(mRecyclerView)
                //.setPoint(x + v.getWidth() / 2 , y)
                .setShape(new RoundedRectangle(100f, v.getWidth(), 5f)) // or RoundedRectangle()
                .setDuration(1000L)
                .setTitle("Tasks to do today")
                .setDescription("Here you can see tasks that you should do today, simply swipe in any direction to mark them as completed")
                //.setOverlayPoint(mRecyclerView.getX(), mRecyclerView.getY())
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


        targets.add(mRecyclerViewTarget); */

        return targets;
    }

}

