package com.janhoracek.doitwithandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String START_HOUR = "com.janhoracek.doitwithandroid.START_HOUR";
    private static final String START_MINUTE = "com.janhoracek.doitwithandroid.START_MINUTE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";
    private static final String PRODUCTIVITY_TIME = "com.janhoracek.doitwithandroid.PRODUCTIVITY_TIME";
    private static final String TIME_REMAINING = "com.janhoracek.doitwithandroid.TIME_REMAINING";
    private static final String HOME_FRAG_RUN = "com.janhoracek.doitwithandroid.HOME_FRAG_RUN";
    private static final String TASKS_FRAG_RUN = "com.janhoracek.doitwithandroid.TASKS_FRAG_RUN";
    private static final String OVERVIEW_FRAG_RUN = "com.janhoracek.doitwithandroid.OVERVIEW_FRAG_RUN";

    private Toolbar mToolbar;
    private SingleDateAndTimePicker mPickerStart;
    private SingleDateAndTimePicker mPickerEnd;
    private Button mButtonRunTutorial;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        mToolbar = findViewById(R.id.settings_toolbar);
        mPickerStart = findViewById(R.id.time_pick_start_settings);
        mPickerEnd = findViewById(R.id.time_pick_end_settings);
        mButtonRunTutorial = findViewById(R.id.button_settings_tutoral);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        setTitle(R.string.activity_settings_title);

        mButtonRunTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setIcon(null)
                        .setTitle(getString(R.string.activity_settings_butt_tutorial_title))
                        .setMessage(getString(R.string.activity_settings_dialog_tutorial))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                pref.edit().putBoolean(HOME_FRAG_RUN, true).apply();
                                pref.edit().putBoolean(TASKS_FRAG_RUN, true).apply();
                                pref.edit().putBoolean(OVERVIEW_FRAG_RUN, true).apply();
                                Intent intent = new Intent(SettingsActivity.this, ApplicationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });


    }

    private void saveSettings() {
        final int startHour;
        final int startMinute;
        final int endHour;
        final int endMinute;
        final int prodHours;
        int prodMinutes;
        final long prodTime;
        final Calendar cal = Calendar.getInstance();

        cal.setTime(mPickerStart.getDate());
        startHour = cal.get(Calendar.HOUR_OF_DAY);
        startMinute = cal.get(Calendar.MINUTE);
        cal.setTime(mPickerEnd.getDate());
        endHour = cal.get(Calendar.HOUR_OF_DAY);
        endMinute = cal.get(Calendar.MINUTE);
        prodHours = (((endHour - startHour) * 60) + (endMinute - startMinute)) / 60;
        prodMinutes = (((endHour - startHour) * 60) + (endMinute - startMinute)) % 60;
        prodTime = prodHours * 60 + prodMinutes;
        if(prodHours <= 0) {
            //Toast.makeText(getActivity(), "Productivity time lower than hour...You can do better!", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(SettingsActivity.this)
                    .setIcon(null)
                    .setTitle(getString(R.string.activity_settings_dialog_prod_time_title))
                    .setMessage(getString(R.string.activity_settings_dialog_prod_time_p1) + prodHours + " " + getString(R.string.activity_settings_dialog_prod_time_p2) +  " " + prodMinutes + " " + getString(R.string.activity_settings_dialog_prod_time_p3))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pref.edit().putInt(START_HOUR, startHour).apply();
                            pref.edit().putInt(START_MINUTE, startMinute).apply();
                            pref.edit().putInt(END_HOUR, endHour).apply();
                            pref.edit().putInt(END_MINUTE, endMinute).apply();
                            pref.edit().putLong(PRODUCTIVITY_TIME, prodTime).apply();
                            pref.edit().putLong(TIME_REMAINING, prodTime).apply();
                            Intent intent = new Intent(SettingsActivity.this, ApplicationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
