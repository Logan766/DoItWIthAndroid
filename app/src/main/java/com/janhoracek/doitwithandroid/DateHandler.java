package com.janhoracek.doitwithandroid;

import android.util.Log;

import com.janhoracek.doitwithandroid.Database.Stats;
import com.janhoracek.doitwithandroid.Database.StatsViewModel;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateHandler {
    private static final String TAG = "JODA";

    SimpleDateFormat mDatabaseFormat = new SimpleDateFormat("yyyyMMdd");
    StatsViewModel mStatsViewModel;
    int mLastStatsDate;
    int mCurrentDate;
    Calendar mCalendar;


    public DateHandler() {
        mCalendar = Calendar.getInstance();
        mCurrentDate = Calendar.getInstance().get(Calendar.YEAR) * 10000 + (Calendar.getInstance().get(Calendar.MONTH)+1) * 100 + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public int getCurrentDateForStats(StatsViewModel statsViewModel) {
        mStatsViewModel = statsViewModel;
        Log.d(TAG, "Velikost lasDate= " + String.valueOf(mStatsViewModel.getLastDate().size()));
        Log.d(TAG, "Current date: " + String.valueOf(mCurrentDate));
        //checkLastDate();
        return mCurrentDate;
    }

    public long getCurrentDateTimeInMilisec() {
        long currentDate = 60000 * ((Calendar.getInstance().getTimeInMillis() + 60000) / 60000);

        return currentDate;
    }

    public void checkLastDate(StatsViewModel statsViewModel) {
        mStatsViewModel = statsViewModel;
        if(mStatsViewModel.getLastDate().size() == 0) {
            Log.d(TAG, "Prazdny staty");
            mStatsViewModel.insert(new Stats(mCurrentDate));
            return;
        } else if (mCurrentDate != mStatsViewModel.getLastDate().get(0).getId()) {
            Log.d(TAG, "Data jsou rozdilna");
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            LocalDate currdate = LocalDate.now();
            LocalDate lastDate = new LocalDate(mStatsViewModel.getLastDate().get(0).getYear(), mStatsViewModel.getLastDate().get(0).getMonth(), mStatsViewModel.getLastDate().get(0).getDate());
            int diff = Days.daysBetween(lastDate, currdate).getDays();
            for(int i = 1; i<=diff; i++) {
                String sId = lastDate.plusDays(i).toString(fmt);
                int id = Integer.parseInt(sId);
                mStatsViewModel.insert(new Stats(id));
                Log.d("JODA", "Inserting ID: " + String.valueOf(id));
            }
        } else {
            Log.d(TAG, "Stats are present");
        }
    }

    public String getDateFromMilisecs(long milliSeconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliSeconds);


        return String.valueOf(cal.getTime());
    }
}
