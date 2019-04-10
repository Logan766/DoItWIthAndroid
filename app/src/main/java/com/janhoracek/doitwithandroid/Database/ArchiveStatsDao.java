package com.janhoracek.doitwithandroid.Database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ArchiveStatsDao {

    @Insert
    void insert(ArchivedTasks aTasks);

    @Update
    void update(ArchivedTasks aTasks);

    @Delete
    void delete(ArchivedTasks aTasks);

    @Query("DELETE FROM archive_stats_table")
    void deleteAllArchives();

    @Query("SElECT * FROM archive_stats_table ORDER BY id DESC")
    LiveData<List<ArchivedTasks>> getAllArchives();

}
