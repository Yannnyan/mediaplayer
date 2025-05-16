package com.main.mediaplayer.events;

public class DownloadEvent {
    public enum DownloadEventType {
        DOWNLOADING,
        FINISHED_DOWNLOADING,
        NO_UPDATE
    }

    DownloadEventType eventType;
    public DownloadEvent(DownloadEventType eventType) {
        this.eventType = eventType;
    }
}
