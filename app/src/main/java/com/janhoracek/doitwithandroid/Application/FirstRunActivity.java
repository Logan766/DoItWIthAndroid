package com.janhoracek.doitwithandroid.Application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.ViewGroup;

import com.janhoracek.doitwithandroid.FirstRunFragmentOne;
import com.janhoracek.doitwithandroid.FirstRunFragmentThree;
import com.janhoracek.doitwithandroid.FirstRunFragmentTwo;
import com.janhoracek.doitwithandroid.R;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.HashMap;
import java.util.Map;

/**
 * First run activity - activity launched after the first run of the application.
 * User is informed about app and productivity time is set
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class FirstRunActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private FirstRunPagerAdapter mAdapter;
    private SpringDotsIndicator mSpringDotsIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
        mViewPager = findViewById(R.id.first_run_view_pager);
        mSpringDotsIndicator = findViewById(R.id.spring_dots_indicator_first_run);
        mAdapter = new FirstRunPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);
        mSpringDotsIndicator.setViewPager(mViewPager);

    }

    /**
     * Adapter of FragmentPagerAdapter - provides showing of fragments
     */
    private class FirstRunPagerAdapter extends FragmentPagerAdapter {
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        /**
         * Constructor of FragmentPagerAdapter for FirstRunPagerAdapter
         * @param fm FragmentManager
         */
        public FirstRunPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new FirstRunFragmentOne();
                case 1:
                    return new FirstRunFragmentTwo();
                case 2:
                    return new FirstRunFragmentThree();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        /**
         * Gets fragment based on position
         * @param position
         * @return Fragment by tag
         */
        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null) return null;
            return mFragmentManager.findFragmentByTag(tag);
        }
    }
}
