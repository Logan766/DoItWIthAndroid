package com.janhoracek.doitwithandroid.Home;

import android.content.Context;

import androidx.fragment.app.Fragment;
/**
 * Abstract class which makes Fragment updateable
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public abstract class UpdateableFragment extends Fragment {
    /**
     * Updates content of Fragment
     */
    public abstract void update();

    /**
     * Updates progress in Fragment
     *
     * @param expGained number of experience gained
     * @param ctx Context
     */
    public abstract void updateProgress(int expGained, Context ctx);
}
