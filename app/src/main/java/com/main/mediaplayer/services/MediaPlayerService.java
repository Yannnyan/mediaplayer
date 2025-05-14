package com.main.mediaplayer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.main.mediaplayer.R;
import com.main.mediaplayer.events.AppLifeCycleEvent;
import com.main.mediaplayer.events.MediaPlayerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Objects;

public class MediaPlayerService extends Service {
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private ListenerService listenerService;
    private PlaylistService playlistService;
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";
    private static final String CHANNEL_ID = "MyServiceChannel";

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(MediaPlayerEvent event) {
        Log.i("INFO", "Recieved Event " + event.eventType.name());
        try{
            switch (event.eventType) {
                case START_PLAYING:
                    mediaPlayer.start();
                    break;
                case STOP_PLAYING:
                    mediaPlayer.stop();
                    break;
                case SEEK_INTO:
                    break;
                case SKIP:
                    mediaPlayer.setDataSource(playlistService.nextMedia());
                    mediaPlayer.start();
                    break;
                default:
            }
        }
        catch (IOException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
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
        try {
            EventBus.getDefault().register(this);
            listenerService = new ListenerService(51234);
            playlistService = new PlaylistService(getApplicationContext());
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(playlistService.getMedia());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                try {
                    mp.setDataSource(playlistService.nextMedia());
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("INFO", "Starting service");
        EventBus.getDefault().post(new AppLifeCycleEvent(AppLifeCycleEvent.AppLifeCycleStages.START_APP));
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
        EventBus.getDefault().post(AppLifeCycleEvent.AppLifeCycleStages.STOP_APP);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
