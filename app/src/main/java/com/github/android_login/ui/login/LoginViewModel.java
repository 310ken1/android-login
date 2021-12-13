package com.github.android_login.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.android_login.AndroidLoginApplication;
import com.github.android_login.manager.account.LoginCallback;

public class LoginViewModel extends AndroidViewModel {
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public int getCurrentAuthority() {
        AndroidLoginApplication app = (AndroidLoginApplication) getApplication();
        return app.appContainer.accountManager.getCurrentAuthority();
    }

    public void login(int id, String password, LoginCallback callback) {
        AndroidLoginApplication app = (AndroidLoginApplication) getApplication();
        app.appContainer.accountManager.login(id, password, callback);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(false));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(false));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }
}
