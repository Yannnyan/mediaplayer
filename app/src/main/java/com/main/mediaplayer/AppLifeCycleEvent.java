package com.main.mediaplayer;

public class AppLifeCycleEvent {
    public enum AppLifeCycleStages {
        START_APP,
        STOP_APP,
        RESTART_APP
    }
    public AppLifeCycleStages event;
}
