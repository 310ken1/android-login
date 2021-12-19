package com.github.android_login.service.account;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountService {
    private static final String TAG = AccountService.class.getSimpleName();

    private static final String dbName = "account";
    private static AccountDatabase db = null;

    public AccountService(Context context) {
        db = Room.databaseBuilder(context, AccountDatabase.class, dbName).build();
    }

    public void authentication(String id, @NonNull String password,
                               @NonNull AuthenticationCallback callback) {
        if (null == db) {
            callback.onResult(id, false, null);
        } else {
            new Thread(() -> {
                List<User> users = db.userDao().get(new String[]{id});
                byte[] hash = createHash(password);
                if (null != users && 0 < users.size() && null != hash) {
                    boolean result = Arrays.equals(hash, users.get(0).password);
                    User user = result ? users.get(0) : null;
                    callback.onResult(id, result, user);
                } else {
                    callback.onResult(id, false, null);
                }
            }).start();
        }
    }

    public void get(@NonNull String id, UsersCallback callback) {
        if (null == db) {
            callback.onResult(null);
        } else {
            new Thread(() -> {
                User user = db.userDao().get(id);
                callback.onResult(new ArrayList<User>() {{
                    add(user);
                }});
            }).start();
        }
    }

    public void update(@NonNull String id, @NonNull String password, int authority,
                       @NonNull ResultCallback callback) {
        if (null == db) {
            callback.onResult(false);
        } else {
            new Thread(() -> {
                User user = db.userDao().get(id);
                byte[] hash = createHash(password);
                if (user != null && null != hash) {
                    db.userDao().update(new User(id, hash, authority));
                    callback.onResult(true);
                } else {
                    callback.onResult(false);
                }
            }).start();
        }
    }

    public void insert(@NonNull String id, @NonNull String password, int authority,
                       @NonNull ResultCallback callback) {
        if (null == db) {
            callback.onResult(false);
        } else {
            new Thread(() -> {
                User user = db.userDao().get(id);
                byte[] hash = createHash(password);
                if (null == user && null != hash) {
                    db.userDao().insert(new User(id, hash, authority));
                    callback.onResult(true);
                } else {
                    callback.onResult(false);
                }
            }).start();
        }
    }

    private @Nullable
    byte[] createHash(@NonNull String password) {
        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
