package com.github.android_login.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.android_login.AndroidLoginApplication;
import com.github.android_login.manager.account.AccountManager;
import com.github.android_login.service.account.User;

public class LoginViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> loginFormValid = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private final AccountManager manager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        manager = ((AndroidLoginApplication) application).appContainer.accountManager;
    }

    public LiveData<Boolean> getLoginFormValid() {
        return loginFormValid;
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public User getLoginUser() {
        return manager.getLoginUser();
    }

    public boolean isLoggedIn() {
        return manager.isLoggedIn();
    }

    public void login(String id, String password) {
        manager.login(id, password, loginResult::postValue);
    }

    public void logout() {
        manager.logout();
    }

    public void loginDataChanged(String username, String password) {
        loginFormValid.setValue(isUserNameValid(username) && isPasswordValid(password));
    }

    private boolean isUserNameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 3;
    }
}
