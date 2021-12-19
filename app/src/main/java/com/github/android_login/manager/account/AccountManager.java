package com.github.android_login.manager.account;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.android_login.service.account.AccountService;
import com.github.android_login.service.account.User;

public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();

    private final AccountService service;
    private User current = null;

    public AccountManager(Context context) {
        this.service = new AccountService(context);
        init();
    }

    public User getLoginUser() {
        return current;
    }

    public boolean isLoggedIn() {
        return null != current;
    }

    public void login(String id, @NonNull String password, @NonNull LoginCallback callback) {
        service.authentication(id, password, (userid, auth, user) -> {
            if (auth) current = user;
            callback.onResult(auth);
        });
    }

    public void logout() {
        current = null;
    }

    private void init() {
        service.insert("1234", "test", 0, result -> {
        });
    }
}
