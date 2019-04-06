package com.janhoracek.doitwithandroid.Database;

import android.app.Application;

import com.janhoracek.doitwithandroid.Database.TaskRepository;
import com.janhoracek.doitwithandroid.Database.Taskers;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
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

    public List<Taskers> getTasksToday(List<Taskers> allTasks) {
        List<Taskers> todayTasks = new ArrayList<>();
        for(int i= 0; i<= allTasks.size() - 1; i++) {
            if(allTasks.get(i).getPriority() == 1) {
                todayTasks.add(allTasks.get(i));
            }
        }

        return todayTasks;
    }


}
