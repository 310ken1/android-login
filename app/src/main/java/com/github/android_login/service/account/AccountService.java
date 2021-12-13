package com.github.android_login.service.account;

import android.content.Context;

import androidx.room.Room;

public class AccountService {
    private static AccountDatabase instance = null;

    private AccountService() {
    }

    public static AccountDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AccountDatabase.class, "Users").build();
        }
        return instance;
    }
}
