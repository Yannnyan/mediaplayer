package com.main.mediaplayer.services;

import android.content.Context;

import com.main.mediaplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlaylistService {
    private int currentMedia = 0;
    ArrayList<String> playlist;
    String playlistDir;
    public PlaylistService(Context context) {
        playlistDir = context.getString(R.string.playlist_path);
        File dir = new File(playlistDir);
        playlist = new ArrayList<>();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            String name = file.getName();
            playlist.add(name);
        }
    }
    public String getMedia() {
        return playlist.get(currentMedia);
    }
    public String nextMedia() {
        int nextMediaIndex = (++currentMedia) % playlist.size();
        return playlist.get(nextMediaIndex);
    }
    public String prevMedia() {
        int prevMediaIndex = (--currentMedia) % playlist.size();
        return playlist.get(prevMediaIndex);
    }
}
