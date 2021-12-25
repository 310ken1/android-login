package com.github.android_login.manager.notification;

public class NotificationBattery implements Notification {
    public final boolean enable;
    public final int level;

    public NotificationBattery(boolean enable, int level) {
        this.enable = enable;
        this.level = level;
    }
}
