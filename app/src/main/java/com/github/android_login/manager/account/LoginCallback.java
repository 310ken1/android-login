package com.github.android_login.manager.account;

import com.github.android_login.service.account.User;

public interface LoginCallback {
    void onResult(int id, boolean login);
}
