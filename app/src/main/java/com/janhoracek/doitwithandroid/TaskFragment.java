package com.janhoracek.doitwithandroid;

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

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;

import java.io.FileReader;

public class TaskFragment extends Fragment {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String HOME_FRAG_RUN = "com.janhoracek.doitwithandroid.HOME_FRAG_RUN";

    private static final String TAG = "LOTKA";

    private FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterAll mAdapterAll;
    private boolean FirstRunCheck;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private StatsViewModel mStatsViewModel;


    private LottieAnimationView mLottieAnimationViewAll;
    private LottieAnimationView mLottieAnimationViewMedium;
    private LottieAnimationView mLottieAnimationViewHigh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_task_both, container, false);
        mLottieAnimationViewAll = v.findViewById(R.id.lottie_all);
        mLottieAnimationViewMedium = v.findViewById(R.id.lottie_medium);
        mLottieAnimationViewHigh = v.findViewById(R.id.lottie_high);


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




        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        new DateHandler().checkLastDate(mStatsViewModel);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = v.findViewById(R.id.view_pager_tasks);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = v.findViewById(R.id.tab_layout_tasks);
        tabLayout.setupWithViewPager(mViewPager);

        return v;
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
