package com.main.mediaplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MediaPlayerService extends Service {

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;

    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";
    private static final String CHANNEL_ID = "MyServiceChannel";


    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.hello); // your audio file in res/raw
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MyServiceChannel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("PennSkanvTic channel for foreground service notification");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Background Sound")
                .setContentText("Playing audio...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        // 👇 MUST be called immediately
        startForeground(1, notification);

        // Safe to start MediaPlayer after this
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.hello);
            mediaPlayer.setLooping(true);
        }
        mediaPlayer.start();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
