package com.github.android_login.service.account;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
