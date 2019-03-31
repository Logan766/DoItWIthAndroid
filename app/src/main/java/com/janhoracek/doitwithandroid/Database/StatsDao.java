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

    @Query("UPDATE stats_table SET low_priority_done = :low_priority, medium_priority_done = :medium_priority, high_priority_done = :high_priority, exp = :exp WHERE id = :id_today")
    void update(int low_priority, int medium_priority, int high_priority, int exp, int id_today);

    @Query("SELECT * FROM stats_table WHERE id = :id_today")
    List<Stats> getPrioritiesExp(int id_today);

    @Insert
    void insert(Stats stats);

    @Update
    void update(Stats stats);

    @Delete
    void delete(Stats stats);

    @Query("DELETE FROM stats_table")
    void deleteAllTasks();

    @Query("SElECT * FROM stats_table ORDER BY id ASC")
    LiveData<List<Stats>> getAllStats();

    @Query("SELECT * FROM stats_table  ORDER BY id DESC LIMIT 1")
    List<Stats> getLastDate();

    @Query("SELECT * FROM stats_table ORDER BY id ASC")
    List<Stats> getAllStatsList();
}
