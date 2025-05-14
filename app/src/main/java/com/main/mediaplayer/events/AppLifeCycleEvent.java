package com.main.mediaplayer.events;

public class AppLifeCycleEvent {
    public enum AppLifeCycleStages {
        START_APP,
        STOP_APP,
        RESTART_APP
    }
    public AppLifeCycleStages event;
    public AppLifeCycleEvent(AppLifeCycleStages event) {
        this.event = event;
    }
}
