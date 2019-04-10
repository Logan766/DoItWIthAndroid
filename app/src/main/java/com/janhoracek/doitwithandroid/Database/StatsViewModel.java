package com.janhoracek.doitwithandroid.Database;

import android.app.Application;

import com.janhoracek.doitwithandroid.DateHandler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class StatsViewModel extends AndroidViewModel {
    private StatsRepository mRepository;
    private LiveData<List<Stats>> allStats;

    public StatsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new StatsRepository(application);
        allStats = mRepository.getAllStats();
    }


    public void insert(Stats stats) {
        mRepository.insert(stats);
    }

    public void update(Stats stats) {
        mRepository.update(stats);
    }

    public void update(int low_priority, int medium_priority, int high_priority, int exp, int id_today) {
        mRepository.update(low_priority, medium_priority, high_priority, exp, id_today);}

    public void delete(Stats stats) {
        mRepository.delete(stats);
    }

    public void deleteAllStats() {
        mRepository.deleteAllStats();
    }

    public LiveData<List<Stats>> getAllStats() {
        return allStats;
    }

    public List<Stats> getPrioritiesExp(int id_today) {
        return mRepository.getPrioritiesExp(id_today);
    }

    public List<Stats> getAllStatsList() {
        return mRepository.getAllStatsList();
    }

    public List<StatsByMonth> getTasksDoneByMonths() {
        return mRepository.getTasksDoneByMonths();
    }

    public List<StatsOverall> getOverallPriority() {
        return mRepository.getOverallPriority();
    }

    public List<Stats> getLastDate() {
        return mRepository.getLastDate();
    }

    public int completeTask(Taskers task) {
        int idToday = new DateHandler().getCurrentDateForStats(this);
        Stats stat = getPrioritiesExp(idToday).get(0);
        int priorityDone = task.getPriority();
        int xpEarned = task.getExp();
        int currentXP = stat.getExp();
        int currentLowPriorityDone = stat.getLow_priority_done();
        int currentMediumPriorityDone = stat.getMedium_priority_done();
        int currentHighPriorityDone = stat.getHigh_priority_done();

        switch (priorityDone) {
            case 1:
                currentHighPriorityDone += 1;
                break;
            case 2:
                currentMediumPriorityDone +=1;
                break;
            case 3:
                currentLowPriorityDone +=1;
                break;
        }

        currentXP += xpEarned;
        update(currentLowPriorityDone, currentMediumPriorityDone, currentHighPriorityDone, currentXP, idToday);
        return xpEarned;
    }
}
