package com.main.mediaplayer.services;

import android.util.Log;

import com.main.mediaplayer.events.AppLifeCycleEvent;
import com.main.mediaplayer.events.MediaPlayerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ListenerService {
    private static String EXECUTE_MSG_ERROR(String msg)
    {
        return String.format("Could not execute the command %s", msg);
    }
    private static String EXECUTE_MSG_SUCCESS(String msg) {
        return String.format("Executing the command %s", msg);
    }
    public boolean isListenerActive() {
        return sock != null && sock.isBound();
    }

    private ServerSocket sock;
    private int port;
    private byte[] buffer = new byte[8192];
    public ListenerService(int port) {
        EventBus.getDefault().register(this);
        this.port = port;
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(AppLifeCycleEvent event) {
        Log.i("INFO","APP STARTED");
        switch (event.event) {
            case START_APP:
                    startListening();
                break;
            case STOP_APP:
            case RESTART_APP:
                if (isListenerActive()) {
                    stopListening();
                }
                break;
        }
    }
    public void stopListening() {
        try{
            if (sock.isBound()){
                sock.close();
            }
            EventBus.getDefault().unregister(this);
        }
        catch (IOException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }
    public void startListening(){

        try {
            if (sock != null) {
                sock.close();
            }
            sock = new ServerSocket(port);
            Log.i("INFO", "App started listening on port " + port);
            while (true) {
                Socket conn = sock.accept();
                InputStream stream = conn.getInputStream();
                int msgLen = stream.read(buffer,0,buffer.length);
                String command = new String(buffer,0,msgLen, StandardCharsets.UTF_8);
                Log.i("INFO", command);
                OutputStream outStream = conn.getOutputStream();
                if (!parseMessage(command)) {
                    outStream.write(EXECUTE_MSG_ERROR(command).getBytes(StandardCharsets.UTF_8));
                }
                else{
                    outStream.write(EXECUTE_MSG_SUCCESS(command).getBytes(StandardCharsets.UTF_8));
                }
                conn.close();
            }
        }
        catch (IOException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }
    private boolean parseMessage(String command) {
        MediaPlayerEvent.MediaEventTypes eventType;
        try {
            eventType = MediaPlayerEvent.MediaEventTypes.valueOf(command);
            EventBus.getDefault().post(new MediaPlayerEvent(eventType));
            return true;
        }
        catch (IllegalArgumentException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }
}
