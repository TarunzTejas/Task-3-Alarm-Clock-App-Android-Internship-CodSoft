package com.example.alarmclockapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmRingActivity extends AppCompatActivity {

    Button stopAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        stopAlarmButton = findViewById(R.id.stopAlarmButton);

        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmReceiver.stopAlarm(); // stop the sound
                finish(); // close this activity
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure alarm stops even if activity is closed
        AlarmReceiver.stopAlarm();
    }
}





