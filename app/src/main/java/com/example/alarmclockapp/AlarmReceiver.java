package com.example.alarmclockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver {

    private static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Wake the device if the screen is off
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "alarmApp:WakeLock");
        wakeLock.acquire(3000); // Hold wake lock for 3 seconds

        // Start playing alarm tone from raw folder
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarmtone); // Your file in res/raw/alarmtone.mp3
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // Launch AlarmRingActivity
        Intent alarmIntent = new Intent(context, AlarmRingActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(alarmIntent);
    }

    public static void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}




