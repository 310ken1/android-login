package com.github.android_login.service.account;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class AccountService {
    private static final String TAG = AccountService.class.getSimpleName();

    private static final String dbName = "Users";
    private static AccountDatabase db = null;

    public AccountService(Context context) {
        db = Room.databaseBuilder(context, AccountDatabase.class, dbName).build();
    }

    public void collate(int id, @NonNull String password, @NonNull CollateCallback callback) {
        if (null == db) {
            callback.onResult(id, false, null);
        } else {
            new Thread(() -> {
                List<User> users = db.userDao().loadAllByIds(new int[]{id});
                if (null != users && 0 < users.size()) {
                    if (users.get(0).id == id) {
                        try {
                            boolean result = Arrays.equals(createHash(password), users.get(0).password);
                            User user = result ? users.get(0) : null;
                            callback.onResult(id, result, user);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            callback.onResult(id, false, null);
                        }
                    }
                } else {
                    callback.onResult(id, false, null);
                }
            }).start();
        }
    }

    public void update(int id, @NonNull String password, int authority, @NonNull ResultCallback callback) {
        if (null == db) {
            callback.onResult(false);
        } else {
            new Thread(() -> {
                User user = db.userDao().get(id);
                if (user != null) {
                    try {
                        db.userDao().update(new User(id, createHash(password), authority));
                        callback.onResult(true);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        callback.onResult(false);
                    }
                } else {
                    callback.onResult(false);
                }
            }).start();
        }
    }

    public void insert(int id, @NonNull String password, int authority, @NonNull ResultCallback callback) {
        if (null == db) {
            callback.onResult(false);
        } else {
            new Thread(() -> {
                User user = db.userDao().get(id);
                if (null == user) {
                    try {
                        db.userDao().insert(new User(id, createHash(password), authority));
                        callback.onResult(true);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        callback.onResult(false);
                    }
                } else {
                    callback.onResult(false);
                }
            }).start();
        }
    }

    private byte[] createHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        return md.digest();
    }
}
