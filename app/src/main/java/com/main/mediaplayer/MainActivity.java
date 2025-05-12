package com.main.mediaplayer;

import android.content.Intent;
import android.os.Bundle;

//import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        // Start media playback
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        ContextCompat.startForegroundService(this, intent);

    }
}
