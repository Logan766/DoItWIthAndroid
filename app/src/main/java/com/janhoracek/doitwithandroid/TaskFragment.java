package com.janhoracek.doitwithandroid;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;

public class TaskFragment extends Fragment {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String HOME_FRAG_RUN = "com.janhoracek.doitwithandroid.HOME_FRAG_RUN";

    private FloatingActionButton mFloatingActionButton;
    private TaskViewModel taskViewModel;
    private RecyclerView mRecyclerView;
    private TaskAdapterAll mAdapterAll;
    private boolean FirstRunCheck;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private StatsViewModel mStatsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_task_both, container, false);

        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        new DateHandler().checkLastDate(mStatsViewModel);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = v.findViewById(R.id.view_pager_tasks);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = v.findViewById(R.id.tab_layout_tasks);
        tabLayout.setupWithViewPager(mViewPager);

        return v;
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
                    fragment = new FragmentCurrentTasks();
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
                    return "Fragment 1";
                case 1:
                    return "Fragment 2";
            }
            return null;
        }
    }
}
