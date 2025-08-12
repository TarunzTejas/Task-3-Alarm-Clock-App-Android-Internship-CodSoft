package com.example.alarmclockapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AlarmAdapter.OnAlarmToggleListener, AlarmAdapter.OnAlarmDeleteListener {

    private TimePicker timePicker;
    private Button setAlarmButton;
    private RecyclerView recyclerView;

    private AlarmDatabase db;
    private AlarmAdapter adapter;
    private List<Alarm> alarmList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.time_picker);
        setAlarmButton = findViewById(R.id.set_alarm_button);
        recyclerView = findViewById(R.id.recycler_view);

        db = AlarmDatabase.getInstance(this);
        alarmList = db.alarmDao().getAll();

        adapter = new AlarmAdapter(this, alarmList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setAlarmButton.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            setNewAlarm(hour, minute);
        });
    }

    private void setNewAlarm(int hour, int minute) {
        Alarm alarm = new Alarm(hour, minute, true,"Alarm label");
        db.alarmDao().insert(alarm);
        alarmList.clear();
        alarmList.addAll(db.alarmDao().getAll());
        adapter.notifyDataSetChanged();

        scheduleAlarm(alarm);
        Toast.makeText(this, "Alarm set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
    }

    private void scheduleAlarm(Alarm alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
        calendar.set(Calendar.MINUTE, alarm.minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1); // Next day
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("hour", alarm.hour);
        intent.putExtra("minute", alarm.minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (alarm.hour * 60 + alarm.minute),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Alarm alarm) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (alarm.hour * 60 + alarm.minute),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onToggle(Alarm alarm) {
        db.alarmDao().update(alarm);
        if (alarm.isActive) {
            scheduleAlarm(alarm);
            Toast.makeText(this, "Alarm turned ON", Toast.LENGTH_SHORT).show();
        } else {
            cancelAlarm(alarm);
            Toast.makeText(this, "Alarm turned OFF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDelete(Alarm alarm) {
        cancelAlarm(alarm);
        db.alarmDao().delete(alarm);
        alarmList.clear();
        alarmList.addAll(db.alarmDao().getAll());
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show();
    }
}


