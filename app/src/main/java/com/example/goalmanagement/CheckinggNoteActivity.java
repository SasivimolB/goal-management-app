package com.example.goalmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

public class CheckinggNoteActivity extends AppCompatActivity {

    TextView goalName;
    CheckBox checkBox1;
    Button btnDone;
    boolean isNewCheck = false;
    int status;
    String cate, desc;
    int end;

    MyDatabase myDatabase;

    Checkingg updateCheck;
    Ach ach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_note);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        goalName = findViewById(R.id.textView);
        checkBox1 = findViewById(R.id.checkBox);
        btnDone = findViewById(R.id.button);

        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).build();

        final int check_id = getIntent().getIntExtra("id", -100);

        if (check_id == -100)
            isNewCheck = true;

        //have info
        if (!isNewCheck) {
            fetchCheckById(check_id);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox1.isChecked()) {
                    Checkingg checkingg = new Checkingg();
                    checkingg.check_id = check_id;
                    checkingg.c_name = goalName.getText().toString();
                    checkingg.c_description = desc;
                    checkingg.c_category = cate;
                    checkingg.c_status = status;
                    checkingg.c_end = end;
                    updateRow(checkingg);

                    if(status>=end)
                    {
                        Ach ach = new Ach();
                        ach.a_name = checkingg.c_name;
                        ach.a_description = checkingg.c_description;
                        ach.a_category = checkingg.c_category;
                        ach.duration = checkingg.c_end;

                        Goal goal = new Goal();
                        goal.goal_id = check_id;
                        goal.g_name = checkingg.c_name;
                        goal.g_description = checkingg.c_description;
                        goal.g_category = checkingg.c_category;
                        goal.duration = checkingg.c_end;

                        insertAchRow(ach);

                        deleteCheckRow(checkingg);
                        deleteGoalRow(goal);
                    }
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchCheckById(final int check_id) {
        new AsyncTask<Integer, Void, Checkingg>() {
            @Override
            protected Checkingg doInBackground(Integer... params) {

                return myDatabase.checkinggDaoAccess().fetchCheckListById(params[0]);

            }

            @Override
            protected void onPostExecute(Checkingg checkingg) {
                super.onPostExecute(checkingg);
                goalName.setText(checkingg.c_name);
                status = checkingg.c_status + 1;
                checkBox1.setText("Day "+ status);
                updateCheck = checkingg;
                desc = checkingg.c_description;
                cate = checkingg.c_category;
                end = checkingg.c_end;
            }
        }.execute(check_id);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteCheckRow (Checkingg checkingg) {
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
        }.execute(checkingg);

    }


    @SuppressLint("StaticFieldLeak")
    private void updateRow(Checkingg check) {
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

    @SuppressLint("StaticFieldLeak")
    private void insertAchRow(Ach ach) {
        new AsyncTask<Ach, Void, Long>() {
            @Override
            protected Long doInBackground(Ach... params) {
                return myDatabase.achDaoAccess().insertAch(params[0]);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                Intent intent = getIntent();
                intent.putExtra("isNew", true).putExtra("id", id);
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute(ach);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteGoalRow(Goal goal) {
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
}
