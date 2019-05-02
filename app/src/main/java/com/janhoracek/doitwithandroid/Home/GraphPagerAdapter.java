package com.janhoracek.doitwithandroid.Home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Graph Pager Adapter which handles level and summary on home screen
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class GraphPagerAdapter extends FragmentPagerAdapter {
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    /**
     * Constructor - sets fragment manager, sets TAGs
     *
     * @param fm Fragment Manager
     */
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

    /**
     * Gets fragment with TAG based on position
     *
     * @param position position of fragment
     * @return Fragment
     */
    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null) return null;
        return mFragmentManager.findFragmentByTag(tag);
    }



}
