package com.janhoracek.doitwithandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.List;

//public class HomeFragment extends Fragment implements View.OnClickListener
public class HomeFragment extends Fragment{

    private ViewPager mViewPager;
    private GraphPagerAdapter mAdapter;
    private SpringDotsIndicator mSpringDotsIndicator;
    private TaskAdapter adapter;
    private TaskViewModel taskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Taskers>>() {
            @Override
            public void onChanged(@Nullable List<Taskers> taskers) {
                //update ReyclerView
                adapter.setTasks(taskers);
            }
        });
        /*Button mButton = v.findViewById(R.id.button4);
        mButton.setOnClickListener(this);

        Button mButton1 = v.findViewById(R.id.button_test);
        mButton1.setOnClickListener(this);*/
        RecyclerView mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        mRecyclerView.setAdapter(adapter);

        mSpringDotsIndicator = v.findViewById(R.id.spring_dots_indicator);
        mViewPager = v.findViewById(R.id.view_pager);
        mAdapter = new GraphPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mSpringDotsIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                UpdateableFragment fragment = (UpdateableFragment) mAdapter.getFragment(i);
                if(fragment == null) return;
                fragment.update();
            }


            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(mRecyclerView);


        return v;
    }




    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button4:
                Log.d("DIWD", "Button pressed");
                DatabaseController.getInstance().saveToDatabase("Nadpis", "Obsah");
                break;
            case R.id.button_test:
                String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Log.d("DIWD", "read passed");
                String path = user+"/Prvni note";

                break;
        }
    }*/
}