package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.janhoracek.doitwithandroid.Database.TaskRepository;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.DateHandler;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;

public class TaskViewModel extends AndroidViewModel {
    private static final String START_HOUR = "com.janhoracek.doitwithandroid.START_HOUR";
    private static final String START_MINUTE = "com.janhoracek.doitwithandroid.START_MINUTE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";
    private static final String TIME_REMAINING = "com.janhoracek.doitwithandroid.TIME_REMAINING";
    private static final String PRODUCTIVITY_TIME = "com.janhoracek.doitwithandroid.PRODUCTIVITY_TIME";

    private static final String TAG = "LISTER";
    private static final String TAG1 = "DOABLE";

    private TaskRepository repository;
    private LiveData<List<Taskers>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public void insert(Taskers taskers) {
        repository.insert(taskers);
    }

    public void update(Taskers taskers) {
        repository.update(taskers);
    }

    public void delete(Taskers taskers) {
        repository.delete(taskers);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }

    public LiveData<List<Taskers>> getAllTasks() {
        return allTasks;
    }

    public List<Taskers> getTasksToday(List<Taskers> allTasks, SharedPreferences pref) {

        List<Taskers> todayTasks = new ArrayList<>();
        long timeRemaining = pref.getLong(TIME_REMAINING, -1);
        Log.d(TAG, "time remaining " + timeRemaining);
        Log.d(TAG, "alltask.size()-1: " + (allTasks.size()-1));
        for(int i= 0; i<= allTasks.size() - 1; i++) {
            if(allTasks.get(i).getTime_consumption() <= timeRemaining) {
                Log.d(TAG, "Inserting new task today: " + allTasks.get(i).getName());
                todayTasks.add(allTasks.get(i));
                timeRemaining -= allTasks.get(i).getTime_consumption();
                Log.d(TAG, "Time remaining po odectu: " + timeRemaining);
            } else {
                Log.d(TAG, "Last longer: " + allTasks.get(i).getName());
                todayTasks.add(allTasks.get(i));
                timeRemaining -= allTasks.get(i).getTime_consumption();
                Log.d(TAG, "Time remaining po odectu: " + timeRemaining);
                break;
            }
        }

        return todayTasks;
    }

    public boolean checkAllDoable(List<Taskers> tasks, SharedPreferences pref) {
        boolean result = true;
        long deadline;
        long lastEnd;
        long start;

        long timeRemaining = pref.getLong(TIME_REMAINING, -1);
        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, -1));
        calStart.set(Calendar.MINUTE, pref.getInt(START_MINUTE, -1));
        calStart.set(Calendar.SECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, -1));
        calEnd.set(Calendar.MINUTE, pref.getInt(END_MINUTE, -1));
        calEnd.set(Calendar.SECOND, 0);

        //lastEnd = calEnd.getTimeInMillis() - timeRemaining * 60000;

        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(calEnd.getTimeInMillis());
        temp.add(Calendar.MINUTE, (int) -timeRemaining);

        Log.d(TAG1, "Time remaining: " + timeRemaining);
        Log.d(TAG1, "Temp calendar date: " + temp.getTime());

        lastEnd = temp.getTimeInMillis();

        //lastEnd = 60000 * (lastEnd / 60000);


        for (int i=0; i<= tasks.size()-1; i++) {
            deadline = tasks.get(i).getD_time_milisec();

            Calendar lastEndCal = Calendar.getInstance();
            lastEndCal.setTimeInMillis(lastEnd);
            Log.d(TAG1, "LatEnd: " + lastEndCal.getTime());


            int duration = tasks.get(i).getTime_consumption();
            int numberDays = duration / (int) pref.getLong(PRODUCTIVITY_TIME, -1);
            Log.d(TAG1, "Number of days: " + numberDays);


            for (int e = 0; e < numberDays; e++) {
                lastEndCal.add(Calendar.DAY_OF_YEAR, 1);
                Log.d(TAG1, "adding day...");
            }

            calEnd.setTimeInMillis(lastEndCal.getTimeInMillis());
            calEnd.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, -1));
            calEnd.set(Calendar.MINUTE, pref.getInt(END_MINUTE, -1));

            Log.d(TAG1, "Current end date: " + calEnd.getTime());

            if(duration%pref.getLong(PRODUCTIVITY_TIME, -1) > (calEnd.getTimeInMillis() - lastEndCal.getTimeInMillis()) / 60000) {
                Log.d(TAG1, "One more day");
                long minutesOver = (duration%pref.getLong(PRODUCTIVITY_TIME, -1)) * 60000 - (calEnd.getTimeInMillis() - lastEndCal.getTimeInMillis());
                minutesOver = minutesOver / 60000;

                lastEndCal.add(Calendar.DAY_OF_YEAR, 1);
                lastEndCal.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, -1));
                lastEndCal.set(Calendar.MINUTE, pref.getInt(START_MINUTE, -1));
                lastEndCal.set(Calendar.SECOND, 0);

                lastEndCal.add(Calendar.MINUTE, (int) minutesOver);
                Log.d(TAG1, "Final end date is: " + lastEndCal.getTime());

            } else {
                int minutes = duration % (int) pref.getLong(PRODUCTIVITY_TIME, -1);
                calEnd.add(Calendar.MINUTE, minutes);
                Log.d(TAG1, "Short task final end date: " + calEnd.getTime());
            }


            if (calEnd.getTimeInMillis() > deadline) {
                Log.d(TAG1, "Over deadline");
                result = false;
                break;
            }

            lastEnd = calEnd.getTimeInMillis();
            Log.d(TAG, "Not over deadline, next task should begin at: " + calEnd.getTime());
        }


        return result;
    }

}
