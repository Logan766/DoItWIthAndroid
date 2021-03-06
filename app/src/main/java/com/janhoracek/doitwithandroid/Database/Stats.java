package com.janhoracek.doitwithandroid.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entity of Stats
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
@Entity(tableName = "stats_table")
public class Stats {

    @PrimaryKey()
    private int id;
    private int year;
    private int month;
    private int date;
    private int exp;
    private int low_priority_done;
    private int medium_priority_done;
    private int high_priority_done;

    /**
     * Constructor
     * @param id ID of stat
     */
    public Stats(int id) {
        this.id = id;
        year = id / 10000;
        month = (id % 10000) / 100;
        date = id % 100;
        exp = 0;
        low_priority_done = 0;
        medium_priority_done = 0;
        high_priority_done = 0;
    }

    /**
     * Gets information about particular stat - for debugging
     * @return String of information about particular stat
     */
    public String toString() {
        String result;
        result = "\n ID: " + String.valueOf(getId()) +
                 "\n YEAR: " + String.valueOf(getYear()) +
                 "\n MONTH: " + String.valueOf(getMonth()) +
                 "\n DAY: " + String.valueOf(getDate()) +
                 "\n EXP: " + String.valueOf(getExp()) +
                 "\n LOW PRIORITY DONE: " + String.valueOf(getLow_priority_done()) +
                 "\n MEDIUM PRIORITY DONE: " + String.valueOf(getMedium_priority_done()) +
                 "\n HIGH PRIORITY DONE: " + String.valueOf(getHigh_priority_done());
        return result;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLow_priority_done() {
        return low_priority_done;
    }

    public void setLow_priority_done(int low_priority_done) {
        this.low_priority_done = low_priority_done;
    }

    public int getMedium_priority_done() {
        return medium_priority_done;
    }

    public void setMedium_priority_done(int medium_priority_done) {
        this.medium_priority_done = medium_priority_done;
    }

    public int getHigh_priority_done() {
        return high_priority_done;
    }

    public void setHigh_priority_done(int high_priority_done) {
        this.high_priority_done = high_priority_done;
    }
}
