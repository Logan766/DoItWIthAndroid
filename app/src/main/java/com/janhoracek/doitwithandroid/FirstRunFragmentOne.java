package com.janhoracek.doitwithandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
/**
 * Fragment to be used first in first run Activity containing info
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class FirstRunFragmentOne extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_run_first_screen, container, false);

        return v;
    }
}
