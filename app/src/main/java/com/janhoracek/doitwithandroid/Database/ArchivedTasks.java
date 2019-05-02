package com.janhoracek.doitwithandroid.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entity of ArchivedTasks
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
@Entity(tableName = "archive_stats_table")
public class ArchivedTasks {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int priority;
    private int time_consumption;
    private Long d_time_milisec;
    private Long completed;

    /**
     * Constuctor of ArchivedTasks
     * @param name name of task
     * @param description description of task
     * @param priority priority of task
     * @param time_consumption time consumed when tasks was completed
     * @param d_time_milisec deadline time in milliseconds
     * @param completed completed time in milliseconds
     */
    public ArchivedTasks(String name, String description, int priority, int time_consumption, Long d_time_milisec, Long completed) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.time_consumption = time_consumption;
        this.d_time_milisec = d_time_milisec;
        this.completed = completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public int getTime_consumption() {
        return time_consumption;
    }

    public Long getD_time_milisec() {
        return d_time_milisec;
    }

    public Long getCompleted() {
        return completed;
    }
}
