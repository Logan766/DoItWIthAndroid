package com.janhoracek.doitwithandroid.Database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;


public class StatsRepository {
    private StatsDao statsDao;
    private LiveData allStats;

    public StatsRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        statsDao = database.statsDao();
        allStats = statsDao.getAllStats();
    }

    public void insert(Stats stats) {
        new InsertStatAsyncTask(statsDao).execute(stats);
    }

    public void update(Stats stats) {
        new UpdateStatAsyncTask(statsDao).execute(stats);
    }

    public void delete(Stats stats) {
        new DeleteStatAsyncTask(statsDao).execute(stats);
    }

    public void deleteAllStats() {
        new DeleteAllStatsAsyncTask(statsDao).execute();
    }

    public LiveData<List<Stats>> getAllStats() {
        return allStats;
    }

    private static class InsertStatAsyncTask extends AsyncTask<Stats, Void, Void> {
        private StatsDao statsDao;

        private InsertStatAsyncTask(StatsDao statsDao) {
            this.statsDao = statsDao;
        }

        @Override
        protected Void doInBackground(Stats... stats) {
            statsDao.insert(stats[0]);
            return null;
        }
    }

    private static class UpdateStatAsyncTask extends AsyncTask<Stats, Void, Void> {
        private StatsDao statsDao;

        private UpdateStatAsyncTask(StatsDao statsDao) {
            this.statsDao =statsDao;
        }

        @Override
        protected Void doInBackground(Stats... stats) {
            statsDao.update(stats[0]);
            return null;
        }
    }

    private static class DeleteStatAsyncTask extends AsyncTask<Stats, Void, Void> {
        private StatsDao statsDao;

        private DeleteStatAsyncTask(StatsDao statsDao) {
            this.statsDao = statsDao;
        }

        @Override
        protected Void doInBackground(Stats... stats) {
            statsDao.delete(stats[0]);
            return null;
        }
    }

    private static class DeleteAllStatsAsyncTask extends AsyncTask<Void, Void, Void> {
        private StatsDao statsDao;

        private DeleteAllStatsAsyncTask(StatsDao statsDao) {
            this.statsDao = statsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            statsDao.deleteAllTasks();
            return null;
        }
    }

}
