package com.janhoracek.doitwithandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.ViewGroup;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.HashMap;
import java.util.Map;

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

    private class FirstRunPagerAdapter extends FragmentPagerAdapter {
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

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

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null) return null;
            return mFragmentManager.findFragmentByTag(tag);
        }



    }
}
