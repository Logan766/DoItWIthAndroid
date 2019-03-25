package com.janhoracek.doitwithandroid;

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


public class CurrentTasksFragment extends Fragment implements UpdateableFragment{

    ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
    ArcProgressStackView mGraph;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_tasks, container, false);
        Log.d("DIWD", "Ted jsme na taskach");

//        Button mButton = v.findViewById(R.id.button4);
//        mButton.setOnClickListener(this);
//
//        Button mButton1 = v.findViewById(R.id.button_test);
//        mButton1.setOnClickListener(this);

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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.button4:
//                Log.d("DIWD", "Button pressed");
//                DatabaseController.getInstance().saveToDatabase("Nadpis", "Obsah");
//                break;
//            case R.id.button_test:
//                String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//                Log.d("DIWD", "read passed");
//                String path = user+"/Prvni note";
//
//                break;
//        }
//    }

    @Override
    public void update() {
        Log.d("DIWD", "CurrTask");
        mGraph.requestLayout();
        mGraph.animateProgress();
    }
}
