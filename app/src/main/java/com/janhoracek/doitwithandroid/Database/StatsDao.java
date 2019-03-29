package com.janhoracek.doitwithandroid.Database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StatsDao {

    @Insert
    void insert(Stats stats);

    @Update
    void update(Stats stats);

    @Delete
    void delete(Stats stats);

    @Query("DELETE FROM stats_table")
    void deleteAllTasks();

    @Query("SElECT * FROM stats_table ORDER BY id DESC")
    LiveData<List<Stats>> getAllStats();
}
