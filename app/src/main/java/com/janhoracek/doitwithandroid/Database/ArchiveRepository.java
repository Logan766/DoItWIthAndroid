package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;


public class ArchiveRepository {
    private ArchiveStatsDao archiveDao;
    private LiveData allTasks;

    public ArchiveRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        archiveDao = database.archiveDao();
        allTasks = archiveDao.getAllArchives();
    }

    public void insert(ArchivedTasks tasks) {
        new InsertTaskAsyncTask(archiveDao).execute(tasks);
    }

    public void update(ArchivedTasks tasks) {
        new UpdateTaskAsyncTask(archiveDao).execute(tasks);
    }

    public void delete(ArchivedTasks tasks) {
        new DeleteTaskAsyncTask(archiveDao).execute(tasks);
    }

    public void deleteAllTasks() {
        new DeleteAllTasksAsyncTask(archiveDao).execute();
    }

    public LiveData<List<ArchivedTasks>> getAllTasks() {
        return allTasks;
    }


    private static class InsertTaskAsyncTask extends AsyncTask<ArchivedTasks, Void, Void> {
        private ArchiveStatsDao aTaskDao;

        private InsertTaskAsyncTask(ArchiveStatsDao aTaskDao) {
            this.aTaskDao = aTaskDao;
        }

        @Override
        protected Void doInBackground(ArchivedTasks... tasks) {
            aTaskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<ArchivedTasks, Void, Void> {
        private ArchiveStatsDao aTaskDao;

        private UpdateTaskAsyncTask(ArchiveStatsDao aTaskDao) {
            this.aTaskDao = aTaskDao;
        }

        @Override
        protected Void doInBackground(ArchivedTasks... tasks) {
            aTaskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<ArchivedTasks, Void, Void> {
        private ArchiveStatsDao aTaskDao;

        private DeleteTaskAsyncTask(ArchiveStatsDao aTaskDao) {
            this.aTaskDao = aTaskDao;
        }

        @Override
        protected Void doInBackground(ArchivedTasks... tasks) {
            aTaskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArchiveStatsDao aTaskDao;

        private DeleteAllTasksAsyncTask(ArchiveStatsDao aTaskDao) {
            this.aTaskDao = aTaskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            aTaskDao.deleteAllArchives();
            return null;
        }
    }
}
