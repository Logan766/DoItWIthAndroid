package com.janhoracek.doitwithandroid.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.R;
import com.janhoracek.doitwithandroid.UpdateableFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import devlight.io.library.ArcProgressStackView;

import static android.content.Context.MODE_PRIVATE;


public class LevelFragment extends UpdateableFragment {
    //, View.OnClickListener


    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String USER_LEVEL = "com.janhoracek.doitwithandroid.USER_LEVEL";
    private static final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";
    private static final String NEXT_EXPERIENCE = "com.janhoracek.doitwithandroid.NEXT_EXPERIENCE";
    private static final String TAG = "LVL";

    private  ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
    private ArcProgressStackView mGraph;
    private TextView mUserLevel;
    private TextView mCurrentExperience;
    private TextView mNextExperience;
    private SharedPreferences pref;

    private LottieAnimationView mLottieFireworks;

    //private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_level, container, false);
        mUserLevel = v.findViewById(R.id.text_level_number);
        mCurrentExperience = v.findViewById(R.id.text_current_exp);
        mNextExperience = v.findViewById(R.id.text_exp_next_level);
        mLottieFireworks = v.findViewById(R.id.lottie_fireworks);
        pref = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mGraph = v.findViewById(R.id.arcProgressStackViewLevel);

        float progress = (((float) pref.getInt(USER_EXPERIENCE, -1)) / pref.getInt(NEXT_EXPERIENCE, -1)) * 100;
        models.add(new ArcProgressStackView.Model(getString(R.string.level_fragment_progress), progress, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.LevelBar)));
        mGraph.setModels(models);
        update();
        return v;
    }

    @Override
    public void update() {
        Log.d("DIWD", "Level");
        mGraph.animateProgress();
        mUserLevel.setText(String.valueOf(pref.getInt(USER_LEVEL, -1)));
        mCurrentExperience.setText(String.valueOf(pref.getInt(USER_EXPERIENCE,-1)));
        mNextExperience.setText(String.valueOf(pref.getInt(NEXT_EXPERIENCE, -1)));

    }

    public void updateProgress(int expGained, Context ctx) {
        pref = ctx.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        int currExp = pref.getInt(USER_EXPERIENCE, -1);
        int expNextLevel = pref.getInt(NEXT_EXPERIENCE, -1);

        if(((currExp + expGained)/expNextLevel) > 0) {
            mLottieFireworks.playAnimation();
            Log.d(TAG, "level up");
            int oldLevel = pref.getInt(USER_LEVEL, -1);
            int oldNextExp = pref.getInt(NEXT_EXPERIENCE, -1);
            pref.edit().putInt(USER_LEVEL, oldLevel + 1).apply();
            pref.edit().putInt(NEXT_EXPERIENCE, oldNextExp * 2).apply();
            pref.edit().putInt(USER_EXPERIENCE, currExp + expGained - expNextLevel).apply();

            ArrayList<ArcProgressStackView.Model> progress = new ArrayList<>();
            float newProgress = (((float) pref.getInt(USER_EXPERIENCE, -1)) / (pref.getInt(NEXT_EXPERIENCE, -1))) * 100;
            progress.add(new ArcProgressStackView.Model(getString(R.string.level_fragment_progress), newProgress, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.LevelBar)));
            //progress.add(new ArcProgressStackView.Model("Progress", newProgress, Color.RED, Color.GRAY));
            mGraph.setModels(progress);
            mGraph.animateProgress();
        } else {
            Log.d(TAG, "Not level up");
            pref.edit().putInt(USER_EXPERIENCE, expGained + currExp).apply();
            float oldProgress = ((float) currExp / expNextLevel) * 100;
            float newProgress = (((float) currExp + expGained) / (float) expNextLevel) * 100;

            Log.d(TAG, "old Progress: " + oldProgress);
            Log.d(TAG, "new Progress: " + newProgress);

            ArrayList<ArcProgressStackView.Model> progress = new ArrayList<>();
            progress.add(new ArcProgressStackView.Model(getString(R.string.level_fragment_progress), newProgress, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.LevelBar)));
            mGraph.setModels(progress);
            long progressTime = (long) (mGraph.getAnimationDuration() * (oldProgress / newProgress));
            Log.d(TAG, "Progress time: " + progressTime);
            mGraph.getProgressAnimator().setCurrentPlayTime(progressTime);
            mGraph.animateProgress();

        }
        mUserLevel.setText(String.valueOf(pref.getInt(USER_LEVEL, -1)));
        mCurrentExperience.setText(String.valueOf(pref.getInt(USER_EXPERIENCE,-1)));
        mNextExperience.setText(String.valueOf(pref.getInt(NEXT_EXPERIENCE, -1)));

    }
}
