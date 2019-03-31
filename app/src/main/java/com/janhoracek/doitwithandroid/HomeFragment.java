package com.janhoracek.doitwithandroid;

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

import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//public class HomeFragment extends Fragment implements View.OnClickListener
public class HomeFragment extends Fragment{
    private static final String TAG = "DIWD1";


    private ViewPager mViewPager;
    private GraphPagerAdapter mAdapter;
    private SpringDotsIndicator mSpringDotsIndicator;
    private TaskAdapterToday adapter;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;

    private StatsViewModel mStatsViewModel;
    private List<Stats> mStats = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        /*Date currentTime = Calendar.getInstance().getTime();

        int date_id = Calendar.getInstance().get(Calendar.YEAR) * 10000 + (Calendar.getInstance().get(Calendar.MONTH)+1) * 100 + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        Log.d("DIWD", "Integer id date " + String.valueOf(date_id));

        Log.d("DIWD", "Integer year " + String.valueOf(date_id / 10000));
        Log.d("DIWD", "Integer month " + String.valueOf((date_id % 10000) / 100));
        Log.d("DIWD", "Integer day " + String.valueOf(date_id % 100));*/



        //observe data and change
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                //update ReyclerView
                adapter.setTasks(taskers);
            }
        });

        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        //new DateHandler(mStatsViewModel).getCurrentDateForStats();
        new DateHandler(mStatsViewModel).checkLastDate();
        /*Stats stats = new Stats(20190325);
        stats.setHigh_priority_done(5);
        stats.setLow_priority_done(1);
        stats.setExp(2100);
        mStatsViewModel.update(stats);*/

        mStatsViewModel.getAllStats().observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(@Nullable List<Stats> stats) {
                //update ReyclerView
                //adapter.setTasks(taskers);
                //Log.d("DIWD", "TRYYYY: " + stats.get(0).getExp());
            }
        });





        /*Button mButton = v.findViewById(R.id.button4);
        mButton.setOnClickListener(this);

        Button mButton1 = v.findViewById(R.id.button_test);
        mButton1.setOnClickListener(this);*/
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

                /*int newExp = adapter.getTaskAt(viewHolder.getAdapterPosition()).getExp();
                Log.d(TAG, "new EXP= " + String.valueOf(adapter.getTaskAt(viewHolder.getAdapterPosition()).getExp()));

                int newPriority = adapter.getTaskAt(viewHolder.getAdapterPosition()).getPriority();
                Log.d(TAG, "new priority: " + String.valueOf(adapter.getTaskAt(viewHolder.getAdapterPosition()).getPriority()));

                int exp = mStatsViewModel.getPrioritiesExp(20190329).get(0).getExp();
                Log.d(TAG, "EXP today " + String.valueOf(mStatsViewModel.getPrioritiesExp(20190329).get(0).getExp()));

                int high_priority = mStatsViewModel.getPrioritiesExp(20190329).get(0).getHigh_priority_done();
                Log.d(TAG, "high priority today: " + String.valueOf(mStatsViewModel.getPrioritiesExp(20190329).get(0).getHigh_priority_done()));

                int medium_priority = mStatsViewModel.getPrioritiesExp(20190329).get(0).getMedium_priority_done();
                Log.d(TAG, "medium priority today: " + String.valueOf(mStatsViewModel.getPrioritiesExp(20190329).get(0).getMedium_priority_done()));

                int low_priority = mStatsViewModel.getPrioritiesExp(20190329).get(0).getLow_priority_done();
                Log.d(TAG, "medium priority today: " + String.valueOf(mStatsViewModel.getPrioritiesExp(20190329).get(0).getLow_priority_done()));

                exp += newExp;
                Log.d(TAG, "EXP k zapsani " + String.valueOf(exp));
                switch (newPriority) {
                    case 1:
                        high_priority += 1;
                        break;
                    case 2:
                        medium_priority += 1;
                        break;
                    case 3:
                        low_priority += 1;
                        break;
                }
                mStatsViewModel.update(low_priority, medium_priority, high_priority, exp, 20190329);*/
                mStatsViewModel.completeTask(adapter.getTaskAt(viewHolder.getAdapterPosition()), mStatsViewModel);
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));

                //Log.d(TAG, "onSwiped: " + mStatsViewModel.getPrioritiesExp(20190329).get(0).toString());
            }
        }).attachToRecyclerView(mRecyclerView);


        return v;
    }




    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:
                Log.d("DIWD", "Button pressed");
                DatabaseController.getInstance().saveToDatabase("Nadpis", "Obsah");
                break;
            case R.id.button_test:
                String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Log.d("DIWD", "read passed");
                String path = user+"/Prvni note";

                break;
        }
    }*/
}
