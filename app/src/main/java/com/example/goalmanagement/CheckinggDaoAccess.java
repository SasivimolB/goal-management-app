package com.example.goalmanagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CheckinggDaoAccess {

    @Insert
    long insertCheck(Checkingg check);

    @Insert
    void insertCheckList(List<Checkingg> checkList);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_CHECKINGG)
    List<Checkingg> fetchAllChecks();

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_CHECKINGG + " WHERE c_category = :category")
    List<Checkingg> fetchCheckListByCategory(String category);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_CHECKINGG + " WHERE check_id = :checkId")
    Checkingg fetchCheckListById(int checkId);

    @Update
    int updateCheck(Checkingg check);

    @Delete
    int deleteCheck(Checkingg check);

}
