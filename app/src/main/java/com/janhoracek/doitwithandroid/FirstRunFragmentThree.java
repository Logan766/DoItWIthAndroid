package com.janhoracek.doitwithandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FirstRunFragmentThree extends Fragment {
    private static final String TAG = "FIRSTRUNN";

    private Button mButton;
    private SingleDateAndTimePicker mDateAndTimePickerStart;
    private SingleDateAndTimePicker mDateAndTimePickerEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_run_third_screen, container, false);
        mButton = v.findViewById(R.id.button_first_run_complete);
        mDateAndTimePickerStart = v.findViewById(R.id.first_run_time_pick_start);
        mDateAndTimePickerEnd = v.findViewById(R.id.first_run_time_pick_end);

        final Calendar cal = Calendar.getInstance();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startHour;
                int startMinute;
                int endHour;
                int endMinute;
                int prodHours;
                int prodMinutes;
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
                if(prodHours <= 0) {
                    Toast.makeText(getActivity(), "Productivity time lower than hour...You can do better!", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(null)
                            .setTitle("Confirm your productivity time")
                            .setMessage("\nYour productivity time is: \n\n" + prodHours + " hours " + prodMinutes + " minutes\n\nIs that correct?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
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
