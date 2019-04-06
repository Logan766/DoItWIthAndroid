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

    private static final String TAG = "LISTER";

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
        /*long startTime = 0;
        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, 0));
        calStart.set(Calendar.MINUTE, pref.getInt(START_MINUTE, 0));

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.HOUR_OF_DAY, pref.getInt(END_HOUR, 0));
        calEnd.set(Calendar.MINUTE, pref.getInt(END_MINUTE, 0));



        if(new DateHandler().getCurrentDateTimeInMilisec() < calStart.getTimeInMillis()) {
            startTime = calStart.getTimeInMillis();
        } else {
            startTime = new DateHandler().getCurrentDateTimeInMilisec();
        }

        long timeRemaining = calEnd.getTimeInMillis() - startTime;
        */

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


}
