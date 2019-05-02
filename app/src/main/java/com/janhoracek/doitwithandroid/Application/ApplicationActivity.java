package com.janhoracek.doitwithandroid.Application;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;
import com.janhoracek.doitwithandroid.Database.TaskViewModel;
import com.janhoracek.doitwithandroid.Tasks.FragmentTasks;
import com.janhoracek.doitwithandroid.Home.HomeFragment;
import com.janhoracek.doitwithandroid.Overview.OverviewFragment;
import com.janhoracek.doitwithandroid.R;
import com.janhoracek.doitwithandroid.Settings.AboutActivity;
import com.janhoracek.doitwithandroid.Settings.SettingsActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.view.Menu;
import android.view.MenuItem;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

/**
 * Application activity - activity that holds navigation of the app and fragments of main windows
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */

public class ApplicationActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    public static Context contextOfApplication;
    private int mOldMenu;
    private Fragment mHome = new HomeFragment();
    private StatsViewModel mStatsViewModel;
    private TaskViewModel mTaskViewModel;
    private SharedPreferences pref;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        Utils.init(getApplicationContext());
        contextOfApplication = getApplicationContext();
        setContentView(R.layout.activity_application);

        //load shared preferences
        pref = getSharedPreferences(PREFS_NAME ,MODE_PRIVATE);

        //load models
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mTaskViewModel.checkAllDoables(mTaskViewModel.getAllTasksList(), pref);
        mStatsViewModel = ViewModelProviders.of(this).get(StatsViewModel.class);
        mStatsViewModel.getAllStats().observe(this, new Observer<List<Stats>>() {
            @Override
            public void onChanged(@Nullable List<Stats> stats) {
                DataHolder.getInstance().setmLineChartData(stats);
                DataHolder.getInstance().setmBarDataMonth(mStatsViewModel.getTasksDoneByMonths());
                DataHolder.getInstance().setmBarDataDay(stats);
                DataHolder.getInstance().setmPieOverallData(mStatsViewModel.getOverallPriority());
            }
        });

        //load UI
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mToolbar = findViewById(R.id.toolbar);
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
                //navigation
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = mHome;
                        mToolbar.setTitle(R.string.navigation_home);
                        enter = R.anim.enter_from_left;
                        exit = R.anim.exit_to_right;
                        break;
                    case R.id.navigation_task:
                        selectedFragment = new FragmentTasks();
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
                if(mOldMenu == menuItem.getItemId()) return true; //same item
                //animation check
                if((mOldMenu == R.id.navigation_home) && (menuItem.getItemId() == R.id.navigation_graphs)) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right_short, R.anim.exit_to_left_short).replace(R.id.fragment_container,selectedFragment).commit();
                } else if((mOldMenu == R.id.navigation_graphs) && (menuItem.getItemId() == R.id.navigation_home)) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left_short, R.anim.exit_to_right_short).replace(R.id.fragment_container,selectedFragment).commit();
                } else {getSupportFragmentManager().beginTransaction().setCustomAnimations(enter, exit).replace(R.id.fragment_container,selectedFragment).commit();}
                mOldMenu = menuItem.getItemId();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(ApplicationActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                intent = new Intent(ApplicationActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

}
