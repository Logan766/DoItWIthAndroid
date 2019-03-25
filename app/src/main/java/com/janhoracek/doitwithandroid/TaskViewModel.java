package com.janhoracek.doitwithandroid;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.annotation.NonNull;

public class TaskViewModel extends AndroidViewModel {
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
}
