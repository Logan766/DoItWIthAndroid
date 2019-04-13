package com.janhoracek.doitwithandroid;

import android.content.SharedPreferences;
import android.util.Log;

import com.janhoracek.doitwithandroid.Database.Taskers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateChangeChecker {
    private static final String CURRENT_DATE = "com.janhoracek.doitwithandroid.CURRENT_DATE";
    private static final String PRODUCTIVITY_TIME = "com.janhoracek.doitwithandroid.PRODUCTIVITY_TIME";
    private static final String TIME_REMAINING = "com.janhoracek.doitwithandroid.TIME_REMAINING";
    private static final String START_HOUR = "com.janhoracek.doitwithandroid.START_HOUR";
    private static final String START_MINUTE = "com.janhoracek.doitwithandroid.START_MINUTE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";
    private static final int NOT_EXIST = -1;
    private static DateChangeChecker instance;

    private static String TAG = "DCHCK";


    private DateChangeChecker() {

    }

    public static DateChangeChecker getInstance() {
        if(instance == null) {
            instance = new DateChangeChecker();
        }
        return instance;
    }

    public void CheckDate(SharedPreferences pref) {
        long CurrentTime = 60000 * (pref.getLong(CURRENT_DATE, NOT_EXIST) / 60000);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if(CurrentTime == NOT_EXIST ) {
            pref.edit().putLong(CURRENT_DATE, cal.getTimeInMillis()).apply();
            Log.d(TAG, "Date not exist puttin in date: " + cal.getTime());
            pref.edit().putLong(TIME_REMAINING, pref.getLong(PRODUCTIVITY_TIME, -1)).apply();
            return;
        } else if ( CurrentTime != cal.getTimeInMillis()) {
            pref.edit().putLong(CURRENT_DATE, cal.getTimeInMillis()).apply();
            pref.edit().putLong(TIME_REMAINING, pref.getLong(PRODUCTIVITY_TIME, -1)).apply();
            Log.d(TAG, "Time remaining: " + (pref.getLong(PRODUCTIVITY_TIME, -1)));
            Log.d(TAG, "Date different. Expected: " + CurrentTime + " got: " + cal.getTimeInMillis());
            return;
        }
        Log.d(TAG, "Dates are ok");
    }

    public Date getTodayStart(SharedPreferences pref) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new DateHandler().getCurrentDateTimeInMilisec());
        cal.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, -1));
        cal.set(Calendar.MINUTE, pref.getInt(START_MINUTE, -1));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return  cal.getTime();
    }

    public Date getTodayEnd(SharedPreferences pref) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new DateHandler().getCurrentDateTimeInMilisec());
        cal.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, -1));
        cal.set(Calendar.MINUTE, pref.getInt(END_MINUTE, -1));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return  cal.getTime();
    }

    public void checkTimeRemaining(List<Taskers> tasks, SharedPreferences pref) {
        long timeRemaining = pref.getLong(TIME_REMAINING, -1);
        long currTime = new DateHandler().getCurrentDateTimeInMilisec();
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();

        calStart.setTime(getTodayStart(pref));
        calEnd.setTime(getTodayEnd(pref));

        long minToEnd = (calEnd.getTimeInMillis() - currTime) / 60000;

        if((tasks.size() == 0) && (calStart.getTimeInMillis() < currTime) && (currTime < calEnd.getTimeInMillis())) {
            if(minToEnd < timeRemaining) {
                Log.d("PRDEL", "Time remaining by mel bejt: " + minToEnd);
                pref.edit().putLong(TIME_REMAINING, minToEnd).apply();
            }
        }
    }
}
