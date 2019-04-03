package com.janhoracek.doitwithandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import devlight.io.library.ArcProgressStackView;


public class LevelFragment extends Fragment implements UpdateableFragment {
    //, View.OnClickListener


    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String USER_LEVEL = "com.janhoracek.doitwithandroid.USER_LEVEL";
    private static final String USER_EXPERIENCE = "com.janhoracek.doitwithandroid.USER_EXPERIENCE";
    private static final String NEXT_EXPERIENCE = "com.janhoracek.doitwithandroid.NEXT_EXPERIENCE";

    ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
    ArcProgressStackView mGraph;
    TextView mUserLevel;
    TextView mCurrentExperience;
    TextView mNextExperience;
    SharedPreferences pref;

    //private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_level, container, false);
        mUserLevel = v.findViewById(R.id.text_level_number);
        mCurrentExperience = v.findViewById(R.id.text_current_exp);
        mNextExperience = v.findViewById(R.id.text_exp_next_level);
        //Button mButton = v.findViewById(R.id.button2);
        //mButton.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

//
        //Button mButton2 = v.findViewById(R.id.button3);
        //mButton2.setOnClickListener(this);
        mGraph = v.findViewById(R.id.arcProgressStackViewLevel);
        models.add(new ArcProgressStackView.Model("Progress", 55, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
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

    public void updateProgress() {

    }

    /*
    @Override
    public void onClick(View v) {
          switch (v.getId()) {
            case R.id.button2:
                Log.d("DIWD", "Button pressed");
                ArrayList<ArcProgressStackView.Model> models1 = new ArrayList<>();

                models1.add(new ArcProgressStackView.Model("Progress", 75, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
                mGraph.setModels(models1);
                mGraph.getProgressAnimator().setCurrentPlayTime(716);
                mGraph.animateProgress();
                break;
            case R.id.button3:
                ArrayList<ArcProgressStackView.Model> models2 = new ArrayList<>();

                models2.add(new ArcProgressStackView.Model("Progress", 25, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
                mGraph.setModels(models2);

                mGraph.animateProgress();

                break;
          }
      }*/
}
