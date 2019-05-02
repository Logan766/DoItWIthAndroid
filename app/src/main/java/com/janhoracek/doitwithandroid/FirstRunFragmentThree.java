package com.janhoracek.doitwithandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.janhoracek.doitwithandroid.Application.ApplicationActivity;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FirstRunFragmentThree extends Fragment {
    private static final String TAG = "FIRSTRUNN";
    private static final String PREFS_NAME = "com.janhoracek.doitwithandroid.SettingsSharedPrefs";
    private static final String START_HOUR = "com.janhoracek.doitwithandroid.START_HOUR";
    private static final String START_MINUTE = "com.janhoracek.doitwithandroid.START_MINUTE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";
    private static final String PRODUCTIVITY_TIME = "com.janhoracek.doitwithandroid.PRODUCTIVITY_TIME";
    private static final String TIME_REMAINING = "com.janhoracek.doitwithandroid.TIME_REMAINING";
    final static String PREF_VERSION_CODE_KEY = "1";

    private Button mButton;
    private SingleDateAndTimePicker mDateAndTimePickerStart;
    private SingleDateAndTimePicker mDateAndTimePickerEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_run_third_screen, container, false);
        final SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mButton = v.findViewById(R.id.button_first_run_complete);
        mDateAndTimePickerStart = v.findViewById(R.id.first_run_time_pick_start);
        mDateAndTimePickerEnd = v.findViewById(R.id.first_run_time_pick_end);

        final Calendar cal = Calendar.getInstance();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int startHour;
                final int startMinute;
                final int endHour;
                final int endMinute;
                final int prodHours;
                int prodMinutes;
                final long prodTime;
                cal.setTime(mDateAndTimePickerStart.getDate());
                startHour = cal.get(Calendar.HOUR_OF_DAY);
                startMinute = cal.get(Calendar.MINUTE);
                Log.d(TAG, "Start time hours " + startHour);
                Log.d(TAG, "Start time minutes " + startMinute);
                cal.setTime(mDateAndTimePickerEnd.getDate());
                endHour = cal.get(Calendar.HOUR_OF_DAY);
                endMinute = cal.get(Calendar.MINUTE);
                Log.d(TAG, "End time hours " + endHour);
                Log.d(TAG, "End time minutes " + endMinute);
                prodHours = (((endHour - startHour) * 60) + (endMinute - startMinute)) / 60;
                prodMinutes = (((endHour - startHour) * 60) + (endMinute - startMinute)) % 60;
                prodTime = prodHours * 60 + prodMinutes;
                if(prodHours <= 0) {
                    Toast.makeText(getActivity(), getString(R.string.activity_settings_lower_hours), Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(null)
                            .setTitle(getString(R.string.activity_settings_dialog_prod_time_title))
                            .setMessage(getString(R.string.activity_settings_dialog_prod_time_p1) + "\n" + prodHours + " " + getString(R.string.activity_settings_dialog_prod_time_p2) + " " + prodMinutes +" "+ getString(R.string.activity_settings_dialog_prod_time_p3))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int currentVersionCode = BuildConfig.VERSION_CODE;
                                    pref.edit().putInt(START_HOUR, startHour).apply();
                                    pref.edit().putInt(START_MINUTE, startMinute).apply();
                                    pref.edit().putInt(END_HOUR, endHour).apply();
                                    pref.edit().putInt(END_MINUTE, endMinute).apply();
                                    pref.edit().putLong(PRODUCTIVITY_TIME, prodTime).apply();
                                    pref.edit().putLong(TIME_REMAINING, prodTime);
                                    pref.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
                                    Log.d(TAG, "Productivity time: " + prodTime);
                                    Intent intent = new Intent(getContext(), ApplicationActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }

            }
        });



        return v;
    }
}
