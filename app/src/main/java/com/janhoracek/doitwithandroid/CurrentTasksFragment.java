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

import java.util.ArrayList;

import devlight.io.library.ArcProgressStackView;


public class CurrentTasksFragment extends UpdateableFragment{

    ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
    ArcProgressStackView mGraph;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_tasks, container, false);
        Log.d("DIWD", "Ted jsme na taskach");

        mGraph = v.findViewById(R.id.arcProgressStackViewTasks);


        models.add(new ArcProgressStackView.Model("Progress", 50, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
        models.add(new ArcProgressStackView.Model("Progress", 25, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));
        models.add(new ArcProgressStackView.Model("Progress", 75, getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimaryDark)));

        mGraph.setModels(models);

        mGraph.requestLayout();


        Log.d("DIWD", "Zmena");
        Log.d("DIWD", String.valueOf(mGraph.getProgressModelSize()));

        return v;
    }


    @Override
    public void update() {
        Log.d("DIWD", "CurrTask");
        mGraph.requestLayout();
        mGraph.animateProgress();
    }

    @Override
    public void updateProgress(int expGained, Context ctx) {

    }
}
