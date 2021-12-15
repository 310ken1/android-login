package com.github.android_login;

import android.content.Context;

import com.github.android_login.manager.account.AccountManager;
import com.github.android_login.manager.log.LogManager;

public class AppContainer {

    public AccountManager accountManager;
    public LogManager logManager;

    public AppContainer(Context context) {
        accountManager = new AccountManager(context);
        logManager = new LogManager();
    }
}
