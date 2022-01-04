package com.example.goalmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;

public class GoalNoteActivity extends AppCompatActivity {

    Spinner spinner;
    EditText inTitle, inDesc, inDu;
    Button btnDone, btnDelete;
    boolean isNewGoal = false;

    private String[] categories = {
            "Exercise",
            "Health",
            "Medicine",
            "Study"
    };

    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    MyDatabase myDatabase;

    Goal updateGoal;
    Checkingg updateCheck;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_note);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        spinner = findViewById(R.id.gspinner);
        inTitle = findViewById(R.id.ingTitle);
        inDesc = findViewById(R.id.ingDescription);
        inDu = findViewById(R.id.inDuration);
        btnDone = findViewById(R.id.gbtnDone);
        btnDelete = findViewById(R.id.gbtnDelete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).build();

        int goal_id = getIntent().getIntExtra("id", -100);
        int check_id = goal_id;

        if (goal_id == -100)
            isNewGoal = true;

        if (!isNewGoal) {
            fetchGoalById(goal_id);
            fetchCheckById(check_id);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewGoal) {
                    Goal goal = new Goal();
                    goal.g_name = inTitle.getText().toString();
                    goal.g_description = inDesc.getText().toString();
                    goal.duration = Integer.parseInt(inDu.getText().toString());
                    goal.g_category = spinner.getSelectedItem().toString();

                    Checkingg check = new Checkingg();
                    check.c_name = inTitle.getText().toString();
                    check.c_description = inDesc.getText().toString();
                    check.c_category = spinner.getSelectedItem().toString();
                    check.c_end = Integer.parseInt(inDu.getText().toString());
                    check.c_status = 0;

                    insertRow(goal);
                    insertCheckRow(check);
                } else {

                    updateGoal.g_name = inTitle.getText().toString();
                    updateGoal.g_description = inDesc.getText().toString();
                    updateGoal.duration = Integer.parseInt(inDu.getText().toString());
                    updateGoal.g_category = spinner.getSelectedItem().toString();

                    updateCheck.c_name = inTitle.getText().toString();
                    updateCheck.c_description = inDesc.getText().toString();
                    updateCheck.c_category = spinner.getSelectedItem().toString();
                    updateCheck.c_end = Integer.parseInt(inDu.getText().toString());
                    updateCheck.c_status = status;

                    updateRow(updateGoal);
                    updateCheckRow(updateCheck);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRow(updateGoal);
                deleteCheckRow(updateCheck);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchGoalById(final int goal_id) {
        new AsyncTask<Integer, Void, Goal>() {
            @Override
            protected Goal doInBackground(Integer... params) {

                return myDatabase.goalDaoAccess().fetchGoalListById(params[0]);

            }

            @Override
            protected void onPostExecute(Goal goal) {
                super.onPostExecute(goal);
                inTitle.setText(goal.g_name);
                inDesc.setText(goal.g_description);
                inDu.setText(String.valueOf(goal.duration));
                spinner.setSelection(spinnerList.indexOf(goal.g_category));

                updateGoal = goal;
            }
        }.execute(goal_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void fetchCheckById(final int check_id) {
        new AsyncTask<Integer, Void, Checkingg>() {
            @Override
            protected Checkingg doInBackground(Integer... params) {

                return myDatabase.checkinggDaoAccess().fetchCheckListById(params[0]);

            }

            @Override
            protected void onPostExecute(Checkingg check) {
                super.onPostExecute(check);
                status = check.c_status;
                updateCheck = check;
            }
        }.execute(check_id);

    }

    @SuppressLint("StaticFieldLeak")
    private void insertRow(Goal goal) {
        new AsyncTask<Goal, Void, Long>() {
            @Override
            protected Long doInBackground(Goal... params) {
                return myDatabase.goalDaoAccess().insertGoal(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(goal);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertCheckRow(Checkingg check) {
        new AsyncTask<Checkingg, Void, Long>() {
            @Override
            protected Long doInBackground(Checkingg... params) {
                return myDatabase.checkinggDaoAccess().insertCheck(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(check);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteRow(Goal goal) {
        new AsyncTask<Goal, Void, Integer>() {
            @Override
            protected Integer doInBackground(Goal... params) {
                return myDatabase.goalDaoAccess().deleteGoal(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(goal);

    }

    @SuppressLint("StaticFieldLeak")
    private void deleteCheckRow (Checkingg check) {
        new AsyncTask<Checkingg, Void, Integer>() {
            @Override
            protected Integer doInBackground(Checkingg... params) {
                return myDatabase.checkinggDaoAccess().deleteCheck(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isDeleted", true).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(check);

    }

    @SuppressLint("StaticFieldLeak")
    private void updateRow(Goal goal) {
        new AsyncTask<Goal, Void, Integer>() {
            @Override
            protected Integer doInBackground(Goal... params) {
                return myDatabase.goalDaoAccess().updateGoal(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(goal);

    }

    @SuppressLint("StaticFieldLeak")
    private void updateCheckRow(Checkingg check) {
        new AsyncTask<Checkingg, Void, Integer>() {
            @Override
            protected Integer doInBackground(Checkingg... params) {
                return myDatabase.checkinggDaoAccess().updateCheck(params[0]);
            }

            @Override
            protected void onPostExecute(Integer number) {
                super.onPostExecute(number);

                Intent intent = getIntent();
                intent.putExtra("isNew", false).putExtra("number", number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(check);

    }
}
