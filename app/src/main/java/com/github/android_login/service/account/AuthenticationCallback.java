package com.github.android_login.service.account;

public interface AuthenticationCallback {
    void onResult(String id, boolean authentication, User user);
}
