package com.github.android_login.manager.notification;

public class State {
    public final long timestamp;
    public final boolean enable;

    public State(boolean enable) {
        this.timestamp = System.currentTimeMillis();
        this.enable = enable;
    }
}
