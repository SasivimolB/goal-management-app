package com.example.goalmanagement;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_ACH)
public class Ach implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int ach_id;

    public String a_name;

    public String a_description;

    public String a_category;

    public int duration;
}
