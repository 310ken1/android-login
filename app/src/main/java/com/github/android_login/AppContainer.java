package com.github.android_login;

import android.content.Context;

import com.github.android_login.manager.account.AccountManager;
import com.github.android_login.manager.log.LogManager;
import com.github.android_login.manager.notification.Notification;
import com.github.android_login.manager.notification.NotificationManager;

public class AppContainer {

    public AccountManager accountManager;
    public LogManager logManager;
    public NotificationManager notificationManager;

    public AppContainer(Context context) {
        accountManager = new AccountManager(context);
        logManager = new LogManager();
        notificationManager = new NotificationManager(context);
    }
}
