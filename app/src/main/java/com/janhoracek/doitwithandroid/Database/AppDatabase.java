package com.janhoracek.doitwithandroid.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
/**
 * Database class
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
@Database(entities = {Taskers.class, Stats.class, ArchivedTasks.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract TaskDao taskDao();
    public abstract StatsDao statsDao();
    public abstract ArchiveStatsDao archiveDao();

    /**
     * Get instance of database
     * @param context Context of application
     * @return instance of database
     */
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

    /**
     * Populates database
     */
    private static  class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;
        private StatsDao statsDao;
        private ArchiveStatsDao archiveDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            taskDao = db.taskDao();
            statsDao = db.statsDao();
            archiveDao = db.archiveDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
