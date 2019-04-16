package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.janhoracek.doitwithandroid.ChartDataHolder;
import com.janhoracek.doitwithandroid.DateHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ArchiveTaskViewModel extends AndroidViewModel {
    private ArchiveRepository repository;
    private LiveData<List<ArchivedTasks>> allTasks;

    public ArchiveTaskViewModel(@NonNull Application application) {
        super(application);
        repository = new ArchiveRepository(application);
        allTasks = repository.getAllTasks();
    }

    public void insert(ArchivedTasks tasks) {
        repository.insert(tasks);
    }

    public void update(ArchivedTasks tasks) {
        repository.update(tasks);
    }

    public void delete(ArchivedTasks tasks) {
        repository.delete(tasks);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }

    public LiveData<List<ArchivedTasks>> getAllTasks() {
        return allTasks;
    }

    public List<ArchivedTasks> getAllTasksList() {
        return repository.getAllArchivesList();
    }
}
