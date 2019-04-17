package com.janhoracek.doitwithandroid;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class GraphPagerAdapter extends FragmentPagerAdapter {
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public GraphPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public UpdateableFragment getItem(int position) {
        switch(position) {
            case 0:
                return new LevelFragment();
            case 1:
                return new FragmentHomeOverview();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
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
