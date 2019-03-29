package com.janhoracek.doitwithandroid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Database.Taskers;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import devlight.io.library.ArcProgressStackView;


public class LevelFragment extends Fragment implements UpdateableFragment, View.OnClickListener {

    ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
    ArcProgressStackView mGraph;
    private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_level, container, false);
        Button mButton = v.findViewById(R.id.button2);
        mButton.setOnClickListener(this);
//
        Button mButton2 = v.findViewById(R.id.button3);
        mButton2.setOnClickListener(this);
        mGraph = v.findViewById(R.id.arcProgressStackViewLevel);
        models.add(new ArcProgressStackView.Model("Progress", 55, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
        mGraph.setModels(models);
        update();

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                //update ReyclerView
                /*models.set(0, new ArcProgressStackView.Model("Progress", 75, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
                mGraph.setModels(models);*/


            }
        });

        return v;
    }

    @Override
    public void update() {
        Log.d("DIWD", "Level");
        mGraph.animateProgress();
    }

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
      }
}
