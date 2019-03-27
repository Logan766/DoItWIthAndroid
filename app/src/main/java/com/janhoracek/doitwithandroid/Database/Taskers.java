package com.janhoracek.doitwithandroid.Database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Taskers {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;

    public Taskers(String name, String description) {
        this.name = name;
        this.description = description;
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
}
