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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MediaPlayerService extends Service {
    enum MediaEventTypes {
        START_APP,
        STOP_APP,
        START_PLAYING,
        STOP_PLAYING,
        SEEK_INTO,
        SKIP
    }
    public static class MediaPlayerEvent {
        MediaEventTypes eventType;
    }

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;

    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";
    private static final String CHANNEL_ID = "MyServiceChannel";


    @Subscribe(threadMode = ThreadMode.ASYNC)
    private void onMessage(MediaPlayerEvent event) {
        switch (event.eventType) {
            case START_APP:
                break;
            case STOP_APP:
                break;
            case START_PLAYING:
                break;
            case STOP_PLAYING:
                break;
            case SEEK_INTO:
                break;
            case SKIP:
                break;
            default:
        }
    }

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
        channel.setDescription("channel for foreground service notification");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Background Sound")
                .setContentText("Playing audio...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, notification);

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
