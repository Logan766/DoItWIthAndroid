package com.janhoracek.doitwithandroid;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;


public class ApplicationActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    public static Context contextOfApplication;

    private int mOldMenu;
    private Fragment mHome = new HomeFragment();
    ///private Fragment mTask = new TaskFragment(); ////
    private StatsViewModel mStatsViewModel;
    private TaskViewModel mTaskViewModel;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        Utils.init(getApplicationContext());

        contextOfApplication = getApplicationContext();

        setContentView(R.layout.activity_application);
        pref = getSharedPreferences(PREFS_NAME ,MODE_PRIVATE);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mTaskViewModel.checkAllDoables(mTaskViewModel.getAllTasksList(), pref);

        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        //new DateHandler().checkLastDate(mStatsViewModel);
        mStatsViewModel.getAllStats().observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(@Nullable List<Stats> stats) {
                Log.d("GGR", "Graf updated");
                Log.d("GGR", "Velikost stats: " + stats.size());
                ChartDataHolder.getInstance().setmLineChartData(stats);
                ChartDataHolder.getInstance().setmBarDataMonth(mStatsViewModel.getTasksDoneByMonths());
                ChartDataHolder.getInstance().setmBarDataDay(stats);
                ChartDataHolder.getInstance().setmPieOverallData(mStatsViewModel.getOverallPriority());
            }
        });

        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_navigation);
        final Toolbar mToolbar = findViewById(R.id.toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        mToolbar.setTitle(R.string.navigation_home);
        mOldMenu = mBottomNavigationView.getSelectedItemId();
        setSupportActionBar(mToolbar);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int enter = 0;
                int exit = 0;
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = mHome;
                        mToolbar.setTitle(R.string.navigation_home);
                        enter = R.anim.enter_from_left;
                        exit = R.anim.exit_to_right;
                        break;
                    case R.id.navigation_task:
                        selectedFragment = new TaskFragment(); ////////
                        mToolbar.setTitle(R.string.navigation_task);
                        if(mOldMenu == R.id.navigation_home) {
                            enter = R.anim.enter_from_right;
                            exit = R.anim.exit_to_left;
                        } else {
                            enter = R.anim.enter_from_left;
                            exit = R.anim.exit_to_right;
                        }
                        break;
                    case R.id.navigation_graphs:
                        selectedFragment = new OverviewFragment();
                        mToolbar.setTitle(R.string.navigation_graphs);
                        enter = R.anim.enter_from_right;
                        exit = R.anim.exit_to_left;
                        break;
                }
                if(menuItem.getItemId() == R.id.navigation_graphs) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
                if(mOldMenu == menuItem.getItemId()) return true;
                if((mOldMenu == R.id.navigation_home) && (menuItem.getItemId() == R.id.navigation_graphs)) {
                    Log.d("DIWD", "Z home na grafy");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right_short, R.anim.exit_to_left_short).replace(R.id.fragment_container,selectedFragment).commit();
                } else if((mOldMenu == R.id.navigation_graphs) && (menuItem.getItemId() == R.id.navigation_home)) {
                    Log.d("DIWD", "Z grafu na home");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left_short, R.anim.exit_to_right_short).replace(R.id.fragment_container,selectedFragment).commit();
                } else {getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit).replace(R.id.fragment_container,selectedFragment).commit();}
                mOldMenu = menuItem.getItemId();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }



}
