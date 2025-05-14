package com.main.mediaplayer.events;

public class MediaPlayerEvent {
    public enum MediaEventTypes {
        START_PLAYING,
        STOP_PLAYING,
        SEEK_INTO,
        SKIP
    }
    public MediaEventTypes eventType;
    public MediaPlayerEvent(MediaEventTypes event) {
        this.eventType = event;
    }
}