package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.Chart;

import java.util.ArrayList;

import devlight.io.library.ArcProgressStackView;


public class CurrentTasksFragment extends UpdateableFragment{
    LottieAnimationView mLottieAnimationViewAll;
    LottieAnimationView mLottieAnimationViewMedium;
    LottieAnimationView mLottieAnimationViewHigh;
    TextView mTextViewAll;
    TextView mTextViewMedium;
    TextView mTextViewHigh;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_tasks, container, false);
        mLottieAnimationViewAll = v.findViewById(R.id.lottie_big_all);
        mLottieAnimationViewMedium = v.findViewById(R.id.lottie_big_medium);
        mLottieAnimationViewHigh = v.findViewById(R.id.lottie_big_high);

        mTextViewAll = v.findViewById(R.id.text_lottie_all);
        mTextViewMedium = v.findViewById(R.id.text_lottie_medium);
        mTextViewHigh = v.findViewById(R.id.text_lottie_high);

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

    private void checkerAll() {
        ChartDataHolder holder = ChartDataHolder.getInstance();
        if(holder.getAllTasksDoable()) {
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("success.json");
            mTextViewAll.setText("Great! You can complete all tasks!");
        } else {
            mLottieAnimationViewAll.clearAnimation();
            mLottieAnimationViewAll.setAnimation("not_success.json");
            mTextViewAll.setText("Try skipping low priorities.\nThen you can make it!");
        }
        if(holder.getMediumTasksDoable()) {
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("success.json");
            mTextViewMedium.setText("Medium and higher tasks completable");
        } else {
            mLottieAnimationViewMedium.clearAnimation();
            mLottieAnimationViewMedium.setAnimation("not_success.json");
            mTextViewMedium.setText("Skip medium task, focus on high!");
        }
        if(holder.getHighTasksDoable()) {
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("success.json");
            mTextViewHigh.setText("You can complete all high priority tasks");
        } else {
            mLottieAnimationViewHigh.clearAnimation();
            mLottieAnimationViewHigh.setAnimation("not_success.json");
            mTextViewHigh.setText("Oh, you cannot complete all high tasks.\nConsider changing deadlines");
        }
    }
}
