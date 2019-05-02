package com.janhoracek.doitwithandroid.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateChangeChecker;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.R;

import androidx.lifecycle.ViewModelProviders;

/**
 * Fragment which contains status of doability of tasks
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class FragmentHomeOverview extends UpdateableFragment {

    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";

    private LottieAnimationView mLottieAnimationViewAll;
    private LottieAnimationView mLottieAnimationViewMedium;
    private LottieAnimationView mLottieAnimationViewHigh;
    private TextView mTextViewAll;
    private TextView mTextViewMedium;
    private TextView mTextViewHigh;
    private TaskViewModel mTaskViewModel;
    private SharedPreferences pref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_tasks, container, false);
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mLottieAnimationViewAll = v.findViewById(R.id.lottie_big_all);
        mLottieAnimationViewMedium = v.findViewById(R.id.lottie_big_medium);
        mLottieAnimationViewHigh = v.findViewById(R.id.lottie_big_high);

        mTextViewAll = v.findViewById(R.id.text_lottie_all);
        mTextViewMedium = v.findViewById(R.id.text_lottie_medium);
        mTextViewHigh = v.findViewById(R.id.text_lottie_high);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        DateChangeChecker.getInstance().checkTimeRemaining(mTaskViewModel.getAllTasksList(), pref);

        checkerAll();
        update();

        return v;
    }


    @Override
    public void update() {
        checkerAll();
        mLottieAnimationViewAll.invalidate();
        mLottieAnimationViewMedium.invalidate();
        mLottieAnimationViewHigh.invalidate();

        mLottieAnimationViewAll.playAnimation();
        mLottieAnimationViewHigh.playAnimation();
        mLottieAnimationViewMedium.playAnimation();

    }

    @Override
    public void updateProgress(int expGained, Context ctx) {

    }

    /**
     * Change icons of summary based on doablility results
     */
    private void checkerAll() {
        DataHolder holder = DataHolder.getInstance();
        if(holder.getAllTasksDoable()) {
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("success.json");
            mTextViewAll.setText(getString(R.string.fragment_home_overview_all_task_succ));
        } else {
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("not_success.json");
            mTextViewAll.setText(getString(R.string.fragment_home_overview_all_task_fail));
        }
        if(holder.getMediumTasksDoable()) {
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("success.json");
            mTextViewMedium.setText(getString(R.string.fragment_home_overview_med_task_succ));
        } else {
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("not_success.json");
            mTextViewMedium.setText(getString(R.string.fragment_home_overview_med_task_fail));
        }
        if(holder.getHighTasksDoable()) {
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("success.json");
            mTextViewHigh.setText(getString(R.string.fragment_home_overview_high_task_succ));
        } else {
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("not_success.json");
            mTextViewHigh.setText(getString(R.string.fragment_home_overview_high_task_fail));
        }
    }
}
