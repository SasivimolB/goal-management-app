package com.example.goalmanagement;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_GOAL)
public class Goal implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int goal_id;

    public String g_name;

    public String g_description;

    public String g_category;

    public int duration;
}
