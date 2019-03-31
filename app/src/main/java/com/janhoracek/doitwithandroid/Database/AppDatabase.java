package com.janhoracek.doitwithandroid.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Taskers.class, Stats.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract TaskDao taskDao();
    public abstract StatsDao statsDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "task_databse")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static  class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;
        private StatsDao statsDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            taskDao = db.taskDao();
            statsDao = db.statsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            taskDao.insert(new Taskers("Nazev1", "popis1", 1, 80, 15, 6, 2019, "13:45"));
//            taskDao.insert(new Taskers("Nazev2", "popis2", 2, 150, 15,6,2019, "13:15"));
//            taskDao.insert(new Taskers("Nazev3", "popis3", 3, 200,16,7, 2019, "12:30"));
            /*Stats stats;
            stats = new Stats(20190325);
            stats.setHigh_priority_done(5);
            stats.setLow_priority_done(1);
            stats.setExp(2100);
            statsDao.insert(stats);
            stats = new Stats(20190326);
            stats.setHigh_priority_done(2);
            stats.setLow_priority_done(1);
            stats.setMedium_priority_done(4);
            stats.setExp(1700);
            statsDao.insert(stats);
            stats = new Stats(20190327);
            stats.setHigh_priority_done(0);
            stats.setLow_priority_done(3);
            stats.setMedium_priority_done(4);
            stats.setExp(1100);
            statsDao.insert(stats);
            stats = new Stats(20190328);
            stats.setHigh_priority_done(6);
            stats.setLow_priority_done(0);
            stats.setMedium_priority_done(0);
            stats.setExp(2400);
            statsDao.insert(stats);
            stats = new Stats(20190329);
            stats.setHigh_priority_done(2);
            stats.setLow_priority_done(1);
            stats.setMedium_priority_done(3);
            stats.setExp(1500);
            statsDao.insert(stats);*/
            return null;
        }
    }
}
