package com.janhoracek.doitwithandroid.Database;


import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Taskers {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private int priority;
    private int exp;
    private int time_consumption;
    private int d_day;
    private int d_month;
    private int d_year;
    private String d_time;
    private Long d_time_milisec;
    private int to_be_done;
    private int completed;

    public Taskers(String name, String description, int priority,  int time_consumption, int d_day, int d_month, int d_year, String d_time, Long d_time_milisec, int to_be_done, int completed) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.time_consumption = time_consumption;
        switch (priority) {
            case 1:
                exp = 4 * this.time_consumption;
                break;
            case 2:
                exp = 2 * this.time_consumption;
                break;
            case 3:
                exp = this.time_consumption;
        }
        this.d_day = d_day;
        this.d_month = d_month;
        this.d_year = d_year;
        this.d_time = d_time;
        this.d_time_milisec = d_time_milisec;
        this.to_be_done = to_be_done;
        this.completed = completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExp(int exp) {
        this.exp = exp;
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

    public int getExp() {
        return exp;
    }

    public int getD_day() {
        return d_day;
    }

    public int getD_month() {
        return d_month;
    }

    public int getD_year() {
        return d_year;
    }

    public String getD_time() {
        return d_time;
    }

    public int getTime_consumption() {
        return time_consumption;
    }

    public Long getD_time_milisec() {
        return d_time_milisec;
    }

    public int getTo_be_done() {
        return to_be_done;
    }

    public void setTo_be_done(int to_be_done) {
        this.to_be_done = to_be_done;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
