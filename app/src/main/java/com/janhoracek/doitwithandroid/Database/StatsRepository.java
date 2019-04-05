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

    public void update(int low_priority, int medium_priority, int high_priority, int exp, int id_today) {
        new UpdateStatsPriorityAsyncTask(statsDao).execute(low_priority, medium_priority, high_priority, exp, id_today);
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

    public List<StatsByMonth> getTasksDoneByMonths() {
        return statsDao.getTasksDoneByMonths();
    }

    public List<StatsOverall> getOverallPriority() {
        return statsDao.getOverallPriority();
    }

    public List<Stats> getPrioritiesExp(int id_today) {
        return statsDao.getPrioritiesExp(id_today);
    }

    public List<Stats> getLastDate() {
        return statsDao.getLastDate();
    }

    public List<Stats> getAllStatsList() {
        return statsDao.getAllStatsList();
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

    private static class UpdateStatsPriorityAsyncTask extends AsyncTask<Integer, Void, Void> {
        private StatsDao statsDao;

        private UpdateStatsPriorityAsyncTask(StatsDao statsDao) {
            this.statsDao =statsDao;
        }


        @Override
        protected Void doInBackground(Integer... integers) {
            statsDao.update(integers[0], integers[1], integers[2], integers[3], integers[4]);
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
