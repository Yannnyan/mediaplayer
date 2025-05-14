package com.main.mediaplayer;

import android.util.Log;

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
    public static ListenerService listenerService;

    private ServerSocket sock;
    private int port;
    private byte[] buffer = new byte[8192];
    public ListenerService(int port) {
        this.port = port;
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public static void onLifecycleEvent(AppLifeCycleEvent event) {
        switch (event.event) {
            case START_APP:
                if (listenerService == null) {
                    listenerService = new ListenerService(51234);
                    listenerService.startListening();
                }
                break;
            case STOP_APP:
            case RESTART_APP:
                if (listenerService != null && listenerService.isListenerActive()) {
                    listenerService.stopListening();
                }
                break;
        }
    }
    public void stopListening() {
        try{
            if (sock.isBound()){
                sock.close();
            }
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
            while (true) {
                Socket conn = sock.accept();
                InputStream stream = conn.getInputStream();
                int msgLen = stream.read(buffer,0,buffer.length);
                OutputStream outStream = conn.getOutputStream();
                if (!parseMessage(msgLen)) {
                    outStream.write(EXECUTE_MSG_ERROR(new String(buffer, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
                }
                else{
                    outStream.write(EXECUTE_MSG_SUCCESS(new String(buffer, StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8));
                }
                conn.close();
            }

        }
        catch (IOException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }
    private boolean parseMessage(int msgLen) {
        String msg = new String(buffer, StandardCharsets.UTF_8);
        MediaPlayerEvent.MediaEventTypes eventType;
        try {
            eventType = MediaPlayerEvent.MediaEventTypes.valueOf(msg);
            EventBus.getDefault().post(eventType);
            return true;
        }
        catch (IllegalArgumentException e) {
            Log.e("ERROR", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }
}
