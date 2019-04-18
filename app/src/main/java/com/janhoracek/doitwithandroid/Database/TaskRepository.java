package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


public class TaskRepository {
    private TaskDao taskDao;
    private LiveData allTasks;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllNotes();
    }

    /*public void insert(Taskers taskers) {
        new InsertTaskAsyncTask(taskDao).execute(taskers);
    }*/

    public void insert(Taskers taskers) {
        taskDao.insert(taskers);
    }

    public void update(Taskers taskers) {
        //new UpdateTaskAsyncTask(taskDao).execute(taskers);
        taskDao.update(taskers);
    }

    public void delete(Taskers taskers) {
        //new DeleteTaskAsyncTask(taskDao).execute(taskers);
        taskDao.delete(taskers);
    }

    public void deleteAllTasks() {
        //new DeleteAllTasksAsyncTask(taskDao).execute();
        taskDao.deleteAllTasks();
    }

    public LiveData<List<Taskers>> getAllTasks() {
        return allTasks;
    }

    public List<Taskers> getAllTasksListByPriority(){
        return taskDao.getAllTasksListByPriority();
    }

    public List<Taskers> getAllTasksList() {
        return taskDao.getAllTasksList();
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Taskers, Void, Void> {
        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Taskers... taskers) {
            taskDao.insert(taskers[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Taskers, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Taskers... taskers) {
            taskDao.update(taskers[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Taskers, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Taskers... taskers) {
            taskDao.delete(taskers[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private DeleteAllTasksAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAllTasks();
            return null;
        }
    }
}
