package com.janhoracek.doitwithandroid.Database;


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
    /*private long deadline_time;*/
    private int time_consumption;

    public Taskers(String name, String description, int priority, int time_consumption) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        //this.deadline_time = deadline_time;
        this.time_consumption = time_consumption;
        switch (priority) {
            case 1:
                exp = 100;
                break;
            case 2:
                exp = 200;
                break;
            case 3:
                exp = 400;
        }
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

    /*
    public long getDeadline_time() {
        return deadline_time;
    }
*/
    public int getTime_consumption() {
        return time_consumption;
    }
}
