package com.example.goalmanagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AchDaoAccess {

    @Insert
    long insertAch(Ach ach);

    @Insert
    void insertAchList(List<Ach> achList);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_ACH)
    List<Ach> fetchAllAchs();

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_ACH + " WHERE a_category = :category")
    List<Ach> fetchAchListByCategory(String category);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_ACH + " WHERE ach_id = :achId")
    Ach fetchAchListById(int achId);

}
