package com.github.android_login.manager.account;

import android.content.Context;

import com.github.android_login.service.account.AccountDatabase;
import com.github.android_login.service.account.AccountService;
import com.github.android_login.service.account.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();

    private final Context context;
    private final AccountDatabase database;
    private User current = null;

    public AccountManager(Context context) {
        this.context = context;
        this.database = AccountService.getInstance(context);

        new Thread(() -> {
            List<User> users = database.userDao().getAll();
            if (null == users || 0 == users.size()) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update("test".getBytes());
                    database.userDao().insertAll(new User(1234, md.digest(), 0));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    public int getCurrentAuthority() {
        return current == null ? -1 : current.authority;
    }

    public void login(int id, String password, LoginCallback callback) {
        new Thread(() -> {
            List<User> users = database.userDao().loadAllByIds(new int[]{id});
            if (null != users && 0 < users.size()) {
                if (users.get(0).id == id) {
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA-256");
                        md.update(password.getBytes());
                        boolean result = Arrays.equals(md.digest(), users.get(0).password);
                        if (result) current = users.get(0);
                        callback.onResult(id, result);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        callback.onResult(id, false);
                    }
                }
            } else {
                callback.onResult(id, false);
            }
        }).start();
    }

    public void logout() {
        current = null;
    }
}
