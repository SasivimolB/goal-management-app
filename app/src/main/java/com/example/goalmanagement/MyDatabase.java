package com.example.goalmanagement;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class, Goal.class, Checkingg.class, Ach.class}, version = 1, exportSchema = false)

public abstract class MyDatabase extends RoomDatabase {

    public static final String DB_NAME = "app_db";
    public static final String TABLE_NAME_TODO = "todo";
    public static final String TABLE_NAME_GOAL = "goal";
    public static final String TABLE_NAME_CHECKINGG = "checkingg";
    public static final String TABLE_NAME_ACH = "ach";

    public abstract DaoAccess daoAccess();
    public abstract GoalDaoAccess goalDaoAccess();
    public abstract AchDaoAccess achDaoAccess();
    public abstract CheckinggDaoAccess checkinggDaoAccess();

}
