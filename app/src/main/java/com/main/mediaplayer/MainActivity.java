package com.main.mediaplayer;

import android.content.Intent;
import android.os.Bundle;

//import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.main.mediaplayer.services.MediaPlayerService;

public class MainActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        // Start media playback
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        ContextCompat.startForegroundService(this, intent);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
