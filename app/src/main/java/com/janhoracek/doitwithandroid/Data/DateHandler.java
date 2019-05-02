package com.janhoracek.doitwithandroid.Data;

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
/**
 * Class for operations that are connected with dates
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class DateHandler {
    private static final String TAG = "JODA";

    SimpleDateFormat mDatabaseFormat = new SimpleDateFormat("yyyyMMdd");
    StatsViewModel mStatsViewModel;
    int mLastStatsDate;
    int mCurrentDate;
    Calendar mCalendar;

    /**
     * Constructor - sets current date
     */
    public DateHandler() {
        mCalendar = Calendar.getInstance();
        mCurrentDate = Calendar.getInstance().get(Calendar.YEAR) * 10000 + (Calendar.getInstance().get(Calendar.MONTH)+1) * 100 + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Gets current date
     * @param statsViewModel StatsViewModel
     * @return calendar with current date
     */
    public int getCurrentDateForStats(StatsViewModel statsViewModel) {
        mStatsViewModel = statsViewModel;
        return mCurrentDate;
    }

    /**
     * Gets current date and time in millisecs
     * @return current date and time in millisecs
     */
    public long getCurrentDateTimeInMilisec() {
        long currentDate = 60000 * ((Calendar.getInstance().getTimeInMillis() + 60000) / 60000);

        return currentDate;
    }

    /**
     * Check if last date is in database
     * @param statsViewModel StatsViewModel
     */
    public void checkLastDate(StatsViewModel statsViewModel) {
        mStatsViewModel = statsViewModel;
        if(mStatsViewModel.getLastDate().size() == 0) {
            //stats are empty
            mStatsViewModel.insert(new Stats(mCurrentDate));
            return;
        } else if (mCurrentDate != mStatsViewModel.getLastDate().get(0).getId()) {
            //current date is different from current date in stats
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            LocalDate currdate = LocalDate.now();
            LocalDate lastDate = new LocalDate(mStatsViewModel.getLastDate().get(0).getYear(), mStatsViewModel.getLastDate().get(0).getMonth(), mStatsViewModel.getLastDate().get(0).getDate());
            int diff = Days.daysBetween(lastDate, currdate).getDays();
            for(int i = 1; i<=diff; i++) {
                String sId = lastDate.plusDays(i).toString(fmt);
                int id = Integer.parseInt(sId);
                mStatsViewModel.insert(new Stats(id));
            }
        }
    }

    /**
     * Covnerts date from millisecs to string date
     * @param milliSeconds
     * @return String of current date
     */
    public String getDateFromMilisecs(long milliSeconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliSeconds);
        return String.valueOf(cal.getTime());
    }


}
