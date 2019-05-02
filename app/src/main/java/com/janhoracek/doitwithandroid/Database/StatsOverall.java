package com.janhoracek.doitwithandroid.Database;
/**
 * Stores Stats
 *
 * @author  Jan Horáček
 * @version 1.0
 * @since   2019-03-28
 */
public class StatsOverall {
    private int low_done;
    private int medium_done;
    private int high_done;

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
}
