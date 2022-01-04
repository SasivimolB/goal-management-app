package com.example.goalmanagement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imtodo, imgoal, imach, imsth;
    Drawable drawt, drawg, drawa, draws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imtodo = findViewById(R.id.todoiconn);
        imgoal = findViewById(R.id.goaliconn);
        imach = findViewById(R.id.achiconn);
        imsth = findViewById(R.id.sthiconn);
        imtodo.setImageResource(0);
        imgoal.setImageResource(0);
        imach.setImageResource(0);
        imsth.setImageResource(0);
        drawt = getResources().getDrawable(R.drawable.todoicon);
        drawg = getResources().getDrawable(R.drawable.goalicon);
        drawa = getResources().getDrawable(R.drawable.achievementicon);
        draws = getResources().getDrawable(R.drawable.checkinicon);
        imtodo.setImageDrawable(drawt);
        imgoal.setImageDrawable(drawg);
        imach.setImageDrawable(drawa);
        imsth.setImageDrawable(draws);



        CardView todoCard = findViewById(R.id.todocard);
        todoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                startActivity(intent);
            }
        });

        CardView goalCard = findViewById(R.id.goalcard);
        goalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                startActivity(intent);
            }
        });

        CardView achCard = findViewById(R.id.achcard);
        achCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AchActivity.class);
                startActivity(intent);
            }
        });

        CardView checkCard = findViewById(R.id.checkincard);
        checkCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckinggActivity.class);
                startActivity(intent);
            }
        });
    }

    public void sharedpref(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("FirstTime", false)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,21);
            calendar.set(Calendar.MINUTE,47);
            if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.DAY_OF_MONTH, 1);
            Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTime", true);
            editor.apply();
        }
    }
}