package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.janhoracek.doitwithandroid.ChartDataHolder;
import com.janhoracek.doitwithandroid.Database.TaskRepository;
import com.janhoracek.doitwithandroid.Database.Taskers;
import com.janhoracek.doitwithandroid.DateHandler;

import org.joda.time.LocalDate;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.temporal.ChronoUnit;
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

    public List<Taskers> getAllTasksListByPriority() {return repository.getAllTasksListByPriority();}

    public List<Taskers> getAllTasksList() {
        return repository.getAllTasksList();
    }

    public List<Taskers> getTasksToday(List<Taskers> allTasks, SharedPreferences pref) {

        List<Taskers> todayTasks = new ArrayList<>();
        long timeRemaining = pref.getLong(TIME_REMAINING, -1);
        Log.d(TAG, "time remaining " + timeRemaining);
        Log.d(TAG, "alltask.size()-1: " + (allTasks.size()-1));
        for(int i= 0; i<= allTasks.size() - 1; i++) {
            if((allTasks.get(i).getTime_consumption() - allTasks.get(i).getCompleted()) <= timeRemaining) {
                Log.d(TAG, "Inserting new task today: " + allTasks.get(i).getName());
                Taskers tTask = allTasks.get(i);
                tTask.setTo_be_done(0);
                todayTasks.add(tTask);
                timeRemaining -= (allTasks.get(i).getTime_consumption() - allTasks.get(i).getCompleted());
                Log.d(TAG, "Time remaining po odectu: " + timeRemaining);
            } else {
                Log.d(TAG, "Nevejde se: " + allTasks.get(i).getName());
                timeRemaining -= (allTasks.get(i).getTime_consumption() - allTasks.get(i).getCompleted());
                Log.d(TAG, "Nevejde se tam dneska minut: " + timeRemaining);
                Taskers tTask = allTasks.get(i);
                int toBeDone = (tTask.getTime_consumption() - tTask.getCompleted()) + (int) timeRemaining;
                tTask.setTo_be_done(toBeDone);
                //this.update(tTask);
                todayTasks.add(tTask);
                Log.d(TAG, "Takze to be done jest: " + toBeDone);

                break;
            }
        }
        Log.d(TAG, "/////////////////////////////////////////////");
        return todayTasks;
    }

    public List<Taskers> getHighPriority(List<Taskers> tasks) {
        List<Taskers> highPriority = new ArrayList<>();
        for(int i=0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).getPriority() == 1) {
                highPriority.add(tasks.get(i));
            }
        }
        return highPriority;
    }

    public List<Taskers> getMediumHighPriority(List<Taskers> tasks) {
        List<Taskers> mediumHighPriority = new ArrayList<>();
        for(int i=0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).getPriority() < 3) {
                mediumHighPriority.add(tasks.get(i));
            }
        }
        return mediumHighPriority;
    }

    public void checkAllDoables(List<Taskers> tasks, SharedPreferences pref) {
        if(tasks.size() == 0) {return;}
        boolean result;
        result = checkDoable(tasks, pref);
        ChartDataHolder.getInstance().setAllTasksDoable(result);
        result = checkDoable(this.getMediumHighPriority(tasks), pref);
        ChartDataHolder.getInstance().setMediumTasksDoable(result);
        result = checkDoable(this.getHighPriority(tasks), pref);
        ChartDataHolder.getInstance().setHighTasksDoable(result);
    }

    public boolean checkDoable(List<Taskers> tasks, SharedPreferences pref) {
        boolean result = true;
        long deadline;
        long lastEnd;
        long productivityTime = pref.getLong(PRODUCTIVITY_TIME, -1);
        int startHour = pref.getInt(START_HOUR, -1);
        int startMinute = pref.getInt(START_MINUTE, -1);
        int endHour = pref.getInt(END_HOUR, -1);
        int endMinute = pref.getInt(END_MINUTE, -1);
        int timeRemaining = (int) pref.getLong(TIME_REMAINING, -1);


        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, startHour);
        calStart.set(Calendar.MINUTE, startMinute);
        calStart.set(Calendar.SECOND, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, endHour);
        calEnd.set(Calendar.MINUTE, endMinute);
        calEnd.set(Calendar.SECOND, 0);

        Log.d(TAG1, "StartCal: " + calStart.getTime());
        Log.d(TAG1, "EndCal: " + calEnd.getTime());

        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(calEnd.getTimeInMillis());
        temp.add(Calendar.MINUTE, -timeRemaining);

        Log.d(TAG1, "Time remaining: " + timeRemaining);
        Log.d(TAG1, "Temp calendar date: " + temp.getTime());

        if(calEnd.getTimeInMillis() < new DateHandler().getCurrentDateTimeInMilisec()) {
            calStart.add(Calendar.DAY_OF_YEAR, 1);
            lastEnd = calStart.getTimeInMillis();
            Log.d(TAG1, "Now is after productivity");
        } else {
            lastEnd = temp.getTimeInMillis();
            Log.d(TAG1, "Ok time to add");
        }

        //lastEnd = 60000 * (lastEnd / 60000);

        Log.d(TAG1, "-------------------------------------");

        for (int i=0; i<= tasks.size()-1; i++) {
            deadline = tasks.get(i).getD_time_milisec();
            if(deadline < new DateHandler().getCurrentDateTimeInMilisec()) {
                Log.d(TAG1, "Skipping task");
                continue;
            }
            int duration = tasks.get(i).getTime_consumption();

            Log.d(TAG1, "This task duration: " + duration);
            Calendar deadlines = Calendar.getInstance();
            deadlines.setTimeInMillis(deadline);
            Log.d(TAG1, "This task deadline" + deadlines.getTime());

            Calendar lastEndCal = Calendar.getInstance();
            lastEndCal.setTimeInMillis(lastEnd);
            Log.d(TAG1, "LatEnd: " + lastEndCal.getTime());



            int numberDays = duration / (int) productivityTime;
            Log.d(TAG1, "Number of days: " + numberDays);


            for (int e = 0; e < numberDays; e++) {
                lastEndCal.add(Calendar.DAY_OF_YEAR, 1);
                Log.d(TAG1, "adding day...");
            }

            calEnd.setTime(lastEndCal.getTime());
            calEnd.set(Calendar.HOUR_OF_DAY, endHour);
            calEnd.set(Calendar.MINUTE, endMinute);
            calEnd.set(Calendar.SECOND, 0);

            Log.d(TAG1, "Future productivity end: " + calEnd.getTime());

            long endsDifference = (calEnd.getTimeInMillis() - lastEndCal.getTimeInMillis()) / 60000;

            Log.d(TAG1, "Difference between ends: " + endsDifference);

            if(endsDifference <= 0) {
                Log.d(TAG1, "One more day");
                long minutesOver = (duration % productivityTime) + endsDifference;
                Log.d(TAG1, "Minutes over: " + minutesOver);

                lastEndCal.add(Calendar.DAY_OF_YEAR, 1);

                lastEndCal.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, -1));
                lastEndCal.set(Calendar.MINUTE, pref.getInt(START_MINUTE, -1));
                lastEndCal.set(Calendar.SECOND, 0);

                lastEndCal.add(Calendar.MINUTE, (int) minutesOver);
                Log.d(TAG1, "Final end date is: " + lastEndCal.getTime());

            } else {
                int minutes = duration % (int) productivityTime;
                lastEndCal.add(Calendar.MINUTE, minutes);
                Log.d(TAG1, "Short task final end date: " + lastEndCal.getTime());
            }


            if (lastEndCal.getTimeInMillis() > deadline) {
                Log.d(TAG1, "Over deadline");
                result = false;
                return result;
            } else {
                Log.d(TAG1, "Not over deadline, next task should begin at: " + lastEndCal.getTime());
            }

            lastEnd = lastEndCal.getTimeInMillis();
            Log.d(TAG1, "////////////////////////////////////////////////\n\n\n\n");

        }
        Log.d(TAG1, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        return result;
    }

}
