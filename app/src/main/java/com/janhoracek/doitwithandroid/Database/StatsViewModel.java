package com.janhoracek.doitwithandroid.Database;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class StatsViewModel extends AndroidViewModel {
    private StatsRepository repository;
    private LiveData<List<Stats>> allStats;

    public StatsViewModel(@NonNull Application application) {
        super(application);
        repository = new StatsRepository(application);
        allStats = repository.getAllStats();
    }

    public void insert(Stats stats) {
        repository.insert(stats);
    }

    public void update(Stats stats) {
        repository.update(stats);
    }

    public void delete(Stats stats) {
        repository.delete(stats);
    }

    public void deleteAllStats() {
        repository.deleteAllStats();
    }

    public LiveData<List<Stats>> getAllStats() {
        return allStats;
    }
}
