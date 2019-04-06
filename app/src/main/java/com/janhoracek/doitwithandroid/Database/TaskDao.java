package com.janhoracek.doitwithandroid.Database;

import androidx.lifecycle.LiveData;

import java.util.List;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    @Insert
    void insert(Taskers taskers);

    @Update
    void update(Taskers taskers);

    @Delete
    void delete(Taskers taskers);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SElECT * FROM task_table ORDER BY d_time_milisec ASC")
    LiveData<List<Taskers>> getAllNotes();
}
