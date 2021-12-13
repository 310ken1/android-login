package com.github.android_login;

import android.content.Context;

import com.github.android_login.manager.account.AccountManager;

public class AppContainer {

    public AccountManager accountManager;

    public AppContainer(Context context) {
        accountManager = new AccountManager(context);
    }
}
