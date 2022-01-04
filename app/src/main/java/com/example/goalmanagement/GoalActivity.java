package com.example.goalmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoalRecyclerViewAdapter.ClickListener {

    MyDatabase myDatabase;
    RecyclerView recyclerView;
    Spinner spinner;
    GoalRecyclerViewAdapter recyclerViewAdapter;
    FloatingActionButton floatingActionButton;
    private String[] categories = {
            "All",
            "Exercise",
            "Health",
            "Medicine",
            "Study"
    };

    ArrayList<Goal> goalArrayList = new ArrayList<>();
    ArrayList<Checkingg> checkArrayList = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));

    public static final int NEW_GOAL_REQUEST_CODE = 200;
    public static final int UPDATE_GOAL_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_goal);

        initViews();
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).fallbackToDestructiveMigration().build();
        //checkIfAppLaunchedFirstTime();

        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(GoalActivity.this, GoalNoteActivity.class), NEW_GOAL_REQUEST_CODE);
            }
        });

    }

    private void initViews() {
        floatingActionButton = findViewById(R.id.fab);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new GoalRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(GoalActivity.this, GoalNoteActivity.class).putExtra("id", id), UPDATE_GOAL_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0) {
            loadAllGoals();
        } else {
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredGoals(string);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @SuppressLint("StaticFieldLeak")
    private void loadFilteredGoals(String category) {
        new AsyncTask<String, Void, List<Goal>>() {
            @Override
            protected List<Goal> doInBackground(String... params) {
                return myDatabase.goalDaoAccess().fetchGoalListByCategory(params[0]);

            }

            @Override
            protected void onPostExecute(List<Goal> goalList) {
                recyclerViewAdapter.updateGoalList(goalList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void fetchGoalByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Goal>() {
            @Override
            protected Goal doInBackground(Integer... params) {
                return myDatabase.goalDaoAccess().fetchGoalListById(params[0]);

            }

            @Override
            protected void onPostExecute(Goal goalList) {
                recyclerViewAdapter.addRow(goalList);
            }
        }.execute(id);

    }

    @SuppressLint("StaticFieldLeak")
    private void loadAllGoals() {
        new AsyncTask<String, Void, List<Goal>>() {
            @Override
            protected List<Goal> doInBackground(String... params) {
                return myDatabase.goalDaoAccess().fetchAllGoals();
            }

            @Override
            protected void onPostExecute(List<Goal> goalList) {
                recyclerViewAdapter.updateGoalList(goalList);
            }
        }.execute();
    }

    private void buildDummyGoals() {
        Goal goal = new Goal();
        goal.g_name = "Study for 3 hours per day";
        goal.g_description = "Study for end semester exam";
        goal.duration = 30;
        goal.g_category = "Study";

        Checkingg check = new Checkingg();
        check.c_name = goal.g_name;
        check.c_description = goal.g_description;
        check.c_end = goal.duration;
        check.c_category = goal.g_category;
        check.c_status = 0;

        goalArrayList.add(goal);
        checkArrayList.add(check);

        goal = new Goal();
        goal.g_name = "Study for 5 hours per day";
        goal.g_description = "Study for assessment exam";
        goal.duration = 15;
        goal.g_category = "Study";

        check = new Checkingg();
        check.c_name = goal.g_name;
        check.c_description = goal.g_description;
        check.c_end = goal.duration;
        check.c_category = goal.g_category;
        check.c_status = 0;

        goalArrayList.add(goal);
        checkArrayList.add(check);

        insertList(goalArrayList);
        insertCheckList(checkArrayList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //reset spinners
            spinner.setSelection(0);

            if (requestCode == NEW_GOAL_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
                fetchGoalByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_GOAL_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " rows deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " rows updated", Toast.LENGTH_SHORT).show();
                }

                loadAllGoals();

            }


        } else {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void insertList(List<Goal> goalList) {
        new AsyncTask<List<Goal>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Goal>... params) {
                myDatabase.goalDaoAccess().insertGoalList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(goalList);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertCheckList(List<Checkingg> checkList) {
        new AsyncTask<List<Checkingg>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Checkingg>... params) {
                myDatabase.checkinggDaoAccess().insertCheckList(params[0]);

                return null;

            }

            @Override
            protected void onPostExecute(Void voids) {
                super.onPostExecute(voids);
            }
        }.execute(checkList);

    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
            buildDummyGoals();
        }
    }
}
