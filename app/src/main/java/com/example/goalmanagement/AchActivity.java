package com.example.goalmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AchRecyclerViewAdapter.ClickListener {

    MyDatabase myDatabase;
    RecyclerView recyclerView;
    Spinner spinner;
    AchRecyclerViewAdapter recyclerViewAdapter;
    private String[] categories = {
            "All",
            "Exercise",
            "Health",
            "Medicine",
            "Study"
    };

    ArrayList<Checkingg> checkinggArrayList = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));

    public static final int NEW_GOAL_REQUEST_CODE = 200;
    public static final int UPDATE_GOAL_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ach);

        initViews();
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, MyDatabase.DB_NAME).fallbackToDestructiveMigration().build();

        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

    }

    private void initViews() {
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new AchRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void launchIntent(int id) {
        startActivityForResult(new Intent(AchActivity.this, CheckinggNoteActivity.class).putExtra("id", id), UPDATE_GOAL_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (position == 0) {
            loadAllAchs();
        } else {
            String string = parent.getItemAtPosition(position).toString();
            loadFilteredAch(string);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @SuppressLint("StaticFieldLeak")
    private void loadFilteredAch(String category) {
        new AsyncTask<String, Void, List<Ach>>() {
            @Override
            protected List<Ach> doInBackground(String... params) {
                return myDatabase.achDaoAccess().fetchAchListByCategory(params[0]);

            }

            @Override
            protected void onPostExecute(List<Ach> achList) {
                recyclerViewAdapter.updateAchList(achList);
            }
        }.execute(category);

    }


    @SuppressLint("StaticFieldLeak")
    private void loadAllAchs() {
        new AsyncTask<String, Void, List<Ach>>() {
            @Override
            protected List<Ach> doInBackground(String... params) {
                return myDatabase.achDaoAccess().fetchAllAchs();
            }

            @Override
            protected void onPostExecute(List<Ach> achList) {
                recyclerViewAdapter.updateAchList(achList);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchAchByIdAndInsert(int id) {
        new AsyncTask<Integer, Void, Ach>() {
            @Override
            protected Ach doInBackground(Integer... params) {
                return myDatabase.achDaoAccess().fetchAchListById(params[0]);

            }

            @Override
            protected void onPostExecute(Ach achList) {
                recyclerViewAdapter.addRow(achList);
            }
        }.execute(id);

    }

    //OnActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //reset spinners
            spinner.setSelection(0);

            if (requestCode == NEW_GOAL_REQUEST_CODE) {
                long id = data.getLongExtra("id", -1);
                Toast.makeText(getApplicationContext(), "Row inserted", Toast.LENGTH_SHORT).show();
                fetchAchByIdAndInsert((int) id);

            } else if (requestCode == UPDATE_GOAL_REQUEST_CODE) {

                boolean isDeleted = data.getBooleanExtra("isDeleted", false);
                int number = data.getIntExtra("number", -1);
                if (isDeleted) {
                    Toast.makeText(getApplicationContext(), number + " row updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), number + " row updated", Toast.LENGTH_SHORT).show();
                }

                loadAllAchs();

            }


        } else {
            Toast.makeText(getApplicationContext(), "No action done by user", Toast.LENGTH_SHORT).show();
        }
    }


}
