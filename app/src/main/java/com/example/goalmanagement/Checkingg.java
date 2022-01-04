package com.example.goalmanagement;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = MyDatabase.TABLE_NAME_CHECKINGG)
public class Checkingg implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int check_id;

    public String c_name;

    public String c_description;

    public String c_category;

    public int c_status;

    public int c_end;

}
