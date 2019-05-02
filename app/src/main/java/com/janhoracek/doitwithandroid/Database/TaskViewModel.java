package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.janhoracek.doitwithandroid.Data.DataHolder;
import com.janhoracek.doitwithandroid.Data.DateHandler;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * View Model for Tasks
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class TaskViewModel extends AndroidViewModel {
    private static final String START_HOUR = "com.janhoracek.doitwithandroid.START_HOUR";
    private static final String START_MINUTE = "com.janhoracek.doitwithandroid.START_MINUTE";
    private static final String END_HOUR = "com.janhoracek.doitwithandroid.END_HOUR";
    private static final String END_MINUTE = "com.janhoracek.doitwithandroid.END_MINUTE";
    private static final String TIME_REMAINING = "com.janhoracek.doitwithandroid.TIME_REMAINING";
    private static final String PRODUCTIVITY_TIME = "com.janhoracek.doitwithandroid.PRODUCTIVITY_TIME";

    private static final int PRIORITY_TAG_ALL = 3;
    private static final int PRIORITY_TAG_MEDIUM = 2;
    private static final int PRIORITY_TAG_HIGH = 1;

    private static final String TAG = "LISTER";
    private static final String TAG1 = "DOABLE";

    private TaskRepository repository;
    private LiveData<List<Taskers>> allTasks;

    /**
     * Constructor - gets repository, gets all Tasks
     *
     * @param application
     */
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

    /**
     * Gets all tasks
     *
     * @return All tasks
     */
    public LiveData<List<Taskers>> getAllTasks() {
        return allTasks;
    }

    /**
     * Gets all tasks ordered by priority
     *
     * @return All tasks ordered by priority
     */
    public List<Taskers> getAllTasksListByPriority() {return repository.getAllTasksListByPriority();}

    /**
     * Get all tasks as List
     *
     * @return All tasks (List)
     */
    public List<Taskers> getAllTasksList() {
        return repository.getAllTasksList();
    }

    /**
     * Gets tasks to show on today
     *
     * @param allTasks List of all Tasks
     * @param pref SharedPreferences
     * @return Tasks for today
     */
    public List<Taskers> getTasksToday(List<Taskers> allTasks, SharedPreferences pref) {

        List<Taskers> todayTasks = new ArrayList<>();
        //get time remaining
        long timeRemaining = pref.getLong(TIME_REMAINING, -1);
        //check if tasks should be on today
        for(int i= 0; i<= allTasks.size() - 1; i++) {
            if((allTasks.get(i).getTime_consumption() - allTasks.get(i).getCompleted()) <= timeRemaining) {
                //add whole task on today
                Taskers tTask = allTasks.get(i);
                tTask.setTo_be_done(0);
                todayTasks.add(tTask);
                //lower time remaining by time remaining of task
                timeRemaining -= (allTasks.get(i).getTime_consumption() - allTasks.get(i).getCompleted());
            } else {
                //add parted task
                timeRemaining -= (allTasks.get(i).getTime_consumption() - allTasks.get(i).getCompleted());
                Taskers tTask = allTasks.get(i);
                //set temporary time to be completed
                int toBeDone = (tTask.getTime_consumption() - tTask.getCompleted()) + (int) timeRemaining;
                tTask.setTo_be_done(toBeDone);
                todayTasks.add(tTask);
                //break cycle
                break;
            }
        }
        return todayTasks;
    }

    /**
     * Gets all Tasks to that can be done or all tasks if none can be done
     *
     * @param tasks All Tasks
     * @return all Tasks that can be done or all if none can be done
     */
    public List<Taskers> getMediumLowPriorityForToday(List<Taskers> tasks) {
        List<Taskers> mediumPriorityToday = new ArrayList<>();
        for(int i=0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).isDoable_all()) {
                mediumPriorityToday.add(tasks.get(i));
            }
        }
        if(mediumPriorityToday.size() == 0) {
            // none can be done, get all
            mediumPriorityToday = tasks;
        }
        return mediumPriorityToday;
    }

    /**
     * Gets medium to high priority Tasks that can be done or all tasks if none can be done
     *
     * @param tasks All Tasks
     * @return medium to high priority Tasks that can be done or all Tasks if none can be done
     */
    public List<Taskers> getMedHighPriorityForToday(List<Taskers> tasks) {
        List<Taskers> highPriorityToday = new ArrayList<>();
        for(int i=0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).getPriority() < 3 && tasks.get(i).isDoable_medium()) {
                highPriorityToday.add(tasks.get(i));
            }
        }
        if(highPriorityToday.size() == 0) {
            //none can be done, get all
            for(int i=0; i<=tasks.size()-1; i++) {
                if(tasks.get(i).getPriority() < 3) {
                    highPriorityToday.add(tasks.get(i));
                }
            }
        }
        if(highPriorityToday.size() == 0) {
            highPriorityToday = tasks;
        }
        return highPriorityToday;
    }

    /**
     * Gets high priority Tasks
     *
     * @param tasks All Tasks
     * @return high priority Tasks
     */
    public List<Taskers> getHighPriority(List<Taskers> tasks) {
        List<Taskers> highPriority = new ArrayList<>();
        for(int i=0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).getPriority() == 1) {
                highPriority.add(tasks.get(i));
            }
        }
        return highPriority;
    }

    /**
     * Gets medium to high priority Tasks
     *
     * @param tasks All Tasks
     * @return medium and high priority Tasks
     */
    public List<Taskers> getMediumHighPriority(List<Taskers> tasks) {
        List<Taskers> mediumHighPriority = new ArrayList<>();
        for(int i=0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).getPriority() < 3) {
                mediumHighPriority.add(tasks.get(i));
            }
        }
        return mediumHighPriority;
    }

    /**
     * Check if Tasks can be done
     *
     * @param tasks All Tasks
     * @param pref SharedPreferences
     */
    public void checkAllDoables(List<Taskers> tasks, SharedPreferences pref) {
        if(tasks.size() == 0) {return;} //no tasks in database
        boolean result;
        //check all priority
        result = checkDoable(tasks, pref, PRIORITY_TAG_ALL);
        DataHolder.getInstance().setAllTasksDoable(result);
        //if all priority is false check medium to high
        if(!result) {
            result = checkDoable(this.getMediumHighPriority(tasks), pref, PRIORITY_TAG_MEDIUM);
            DataHolder.getInstance().setMediumTasksDoable(result);
        } else {
            DataHolder.getInstance().setMediumTasksDoable(true);
        }
        //if medium to high priority is false check high only
        if(!result) {
            result = checkDoable(this.getHighPriority(tasks), pref, PRIORITY_TAG_HIGH);
            DataHolder.getInstance().setHighTasksDoable(result);
        } else {
            DataHolder.getInstance().setHighTasksDoable(true);
        }

        //check deadlines
        result = checkDeadline(tasks);
        DataHolder.getInstance().setDeadlinesDoable(result);
    }

    /**
     * Check if tasks are doable based on priority
     *
     * @param tasks Tasks
     * @param pref SharedPreferences
     * @param PRIORITY_TAG Priority TAG
     * @return Boolean if tasks are doable based on priority
     */
    public boolean checkDoable(List<Taskers> tasks, SharedPreferences pref, int PRIORITY_TAG) {
        boolean result = true;
        long deadline;
        long lastEnd;
        long productivityTime = pref.getLong(PRODUCTIVITY_TIME, -1);
        int startHour = pref.getInt(START_HOUR, -1);
        int startMinute = pref.getInt(START_MINUTE, -1);
        int endHour = pref.getInt(END_HOUR, -1);
        int endMinute = pref.getInt(END_MINUTE, -1);
        int timeRemaining = (int) pref.getLong(TIME_REMAINING, -1);

        //get start time
        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, startHour);
        calStart.set(Calendar.MINUTE, startMinute);
        calStart.set(Calendar.SECOND, 0);

        //get end time
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, endHour);
        calEnd.set(Calendar.MINUTE, endMinute);
        calEnd.set(Calendar.SECOND, 0);

        //get temporary Calendar
        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(calEnd.getTimeInMillis());
        temp.add(Calendar.MINUTE, -timeRemaining);

        //check if now is productivity time
        if(calEnd.getTimeInMillis() < new DateHandler().getCurrentDateTimeInMilisec()) {
            //add day and put start on productivity start time
            calStart.add(Calendar.DAY_OF_YEAR, 1);
            lastEnd = calStart.getTimeInMillis();
        } else {
            //now is in productivity time
            lastEnd = temp.getTimeInMillis();
            Log.d(TAG1, "Ok time to add");
        }

        //check if its first task
        if(tasks.size() == 1) {
            //set last end date to current time
            lastEnd = new DateHandler().getCurrentDateTimeInMilisec();
        }

        //check deadlines
        for (int i=0; i<= tasks.size()-1; i++) {
            deadline = tasks.get(i).getD_time_milisec();
            //check if task is after deadline
            if(deadline < new DateHandler().getCurrentDateTimeInMilisec()) {
                //skip task
                continue;
            }
            int duration = tasks.get(i).getTime_consumption();

            //set task deadline Calendar
            Calendar deadlines = Calendar.getInstance();
            deadlines.setTimeInMillis(deadline);

            //set last end Calendar
            Calendar lastEndCal = Calendar.getInstance();
            lastEndCal.setTimeInMillis(lastEnd);

            //get number of productive days
            int numberDays = duration / (int) productivityTime;

            //add productive days
            for (int e = 0; e < numberDays; e++) {
                lastEndCal.add(Calendar.DAY_OF_YEAR, 1);
                //lower remaining duration
                duration -= productivityTime;
            }

            //set end Calendar
            calEnd.setTime(lastEndCal.getTime());
            calEnd.set(Calendar.HOUR_OF_DAY, endHour);
            calEnd.set(Calendar.MINUTE, endMinute);
            calEnd.set(Calendar.SECOND, 0);

            //check if task end is after productivity time
            long endsDifference = (calEnd.getTimeInMillis() - lastEndCal.getTimeInMillis() /*+ duration * 60000*/) / 60000;
            if(endsDifference <= duration) {
                //task ends next day -> add one day
                long minutesOver = (duration % productivityTime) - endsDifference;
                lastEndCal.add(Calendar.DAY_OF_YEAR, 1);

                lastEndCal.set(Calendar.HOUR_OF_DAY, pref.getInt(START_HOUR, -1));
                lastEndCal.set(Calendar.MINUTE, pref.getInt(START_MINUTE, -1));
                lastEndCal.set(Calendar.SECOND, 0);

                //set task end date
                lastEndCal.add(Calendar.MINUTE, (int) minutesOver);
            } else {
                //task end is in current day
                int minutes = duration % (int) productivityTime;
                lastEndCal.add(Calendar.MINUTE, minutes);
            }

            //check if task can be done
            if (lastEndCal.getTimeInMillis() > deadline) {
                //task cannot meet its deadline
                Taskers doableTask = tasks.get(i);
                //set doable based on priority TAG
                switch (PRIORITY_TAG) {
                    case PRIORITY_TAG_ALL:
                        if(doableTask.isDoable_all()) {
                            doableTask.setDoable_all(false);
                            this.update(doableTask);
                        }
                        break;
                    case PRIORITY_TAG_MEDIUM:
                        if(doableTask.isDoable_medium()) {
                            doableTask.setDoable_medium(false);
                            this.update(doableTask);
                        }
                        break;
                    case PRIORITY_TAG_HIGH:
                        if(doableTask.isDoable_high()) {
                            doableTask.setDoable_high(false);
                            this.update(doableTask);
                        }
                        break;
                }
                //one or more tasks are not doable
                result = false;
            } else {
                //task can meet its deadline
                Taskers doableTask = tasks.get(i);
                //set doable based on priority TAG
                switch (PRIORITY_TAG) {
                    case PRIORITY_TAG_ALL:
                        if(!doableTask.isDoable_all()) {
                            doableTask.setDoable_all(true);
                            this.update(doableTask);
                        }
                        break;
                    case PRIORITY_TAG_MEDIUM:
                        if(!doableTask.isDoable_medium()) {
                            doableTask.setDoable_medium(true);
                            this.update(doableTask);
                        }
                        break;
                    case PRIORITY_TAG_HIGH:
                        if(!doableTask.isDoable_high()) {
                            doableTask.setDoable_high(true);
                            this.update(doableTask);
                        }
                        break;
                }
                //task is not over its deadline
            }
            //set last end
            lastEnd = lastEndCal.getTimeInMillis();
        }
        return result;
    }

    /**
     * Check if any task is after its deadline
     *
     * @param tasks Tasks
     * @return false if any tasks is after deadline otherwise true
     */
    public Boolean checkDeadline(List<Taskers> tasks) {
        Boolean result = true;
        long currentDateMili = new DateHandler().getCurrentDateTimeInMilisec();
        for(int i = 0; i<=tasks.size()-1; i++) {
            if(tasks.get(i).getD_time_milisec() < currentDateMili) {
                result = false;
                break;
            }
        }
        return result;
    }
}
