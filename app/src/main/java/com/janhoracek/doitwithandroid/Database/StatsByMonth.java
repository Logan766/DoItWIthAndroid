package com.janhoracek.doitwithandroid.Database;
/**
 * Stores Stats sorted by month
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class StatsByMonth {
    public int low_done;
    public int medium_done;
    public int high_done;
    public int month;
    public int year;

    public int getLow_done() {
        return low_done;
    }

    public void setLow_done(int low_done) {
        this.low_done = low_done;
    }

    public int getMedium_done() {
        return medium_done;
    }

    public void setMedium_done(int medium_done) {
        this.medium_done = medium_done;
    }

    public int getHigh_done() {
        return high_done;
    }

    public void setHigh_done(int high_done) {
        this.high_done = high_done;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
