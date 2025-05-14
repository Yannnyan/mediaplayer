package com.main.mediaplayer;

public class MediaPlayerEvent {
    public enum MediaEventTypes {
        START_PLAYING,
        STOP_PLAYING,
        SEEK_INTO,
        SKIP
    }
    MediaEventTypes eventType;
}