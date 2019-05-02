package com.janhoracek.doitwithandroid.Database;


import android.util.Log;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entity of Tasks
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
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
    private boolean doable_all;
    private boolean doable_medium;
    private boolean doable_high;

    /**
     * Constructor
     * @param name name of task
     * @param description description of task
     * @param priority priority of task
     * @param time_consumption estimated time consumption of task
     * @param d_day deadline day of task
     * @param d_month deadline month of task
     * @param d_year deadline year of task
     * @param d_time deadline time of task
     * @param d_time_milisec deadline time of task in millisecs
     * @param to_be_done temporary time that will be completed when partly done task is completed
     * @param completed completed time (if task was completed partly)
     */
    public Taskers(String name, String description, int priority,  int time_consumption, int d_day, int d_month, int d_year, String d_time, Long d_time_milisec, int to_be_done, int completed) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.time_consumption = time_consumption;

        //set experience based on priority
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
        this.doable_all = true;
        this.doable_medium = true;
        this.doable_high = true;
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

    public boolean isDoable_all() {
        return doable_all;
    }

    public void setDoable_all(boolean doable_all) {
        this.doable_all = doable_all;
    }

    public boolean isDoable_medium() {
        return doable_medium;
    }

    public void setDoable_medium(boolean doable_medium) {
        this.doable_medium = doable_medium;
    }

    public boolean isDoable_high() {
        return doable_high;
    }

    public void setDoable_high(boolean doable_high) {
        this.doable_high = doable_high;
    }

    /**
     * Logs information about task to console
     */
    public void toText(){
        Log.d("TASK", "Name: " + this.name + "\nPriority: " + this.priority + "\nDoable all: " + this.doable_all + "\nDoable medium: " + this.doable_medium + "\nDoable high: " + this.doable_high);
    }
}
