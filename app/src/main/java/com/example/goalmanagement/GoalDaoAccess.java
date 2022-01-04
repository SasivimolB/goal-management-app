package com.example.goalmanagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDaoAccess {

    @Insert
    long insertGoal(Goal goal);

    @Insert
    void insertGoalList(List<Goal> goalList);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_GOAL)
    List<Goal> fetchAllGoals();

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_GOAL + " WHERE g_category = :category")
    List<Goal> fetchGoalListByCategory(String category);

    @Query("SELECT * FROM " + MyDatabase.TABLE_NAME_GOAL + " WHERE goal_id = :goalId")
    Goal fetchGoalListById(int goalId);

    @Update
    int updateGoal(Goal goal);

    @Delete
    int deleteGoal(Goal goal);

}
