package com.janhoracek.doitwithandroid;

import android.content.Context;

import androidx.fragment.app.Fragment;

public abstract class UpdateableFragment extends Fragment {

    public abstract void update();

    public abstract void updateProgress(int expGained, Context ctx);
}
