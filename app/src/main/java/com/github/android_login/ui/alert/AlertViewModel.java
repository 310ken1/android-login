package com.github.android_login.ui.alert;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.github.android_login.AndroidLoginApplication;
import com.github.android_login.manager.notification.Notification;
import com.github.android_login.manager.notification.NotificationListener;

public class AlertViewModel extends AndroidViewModel {
    final public MutableLiveData<Notification> notificationLiveData = new MutableLiveData<>();

    private final AndroidLoginApplication app;

    public AlertViewModel(@NonNull Application application) {
        super(application);
        app = (AndroidLoginApplication) application;

        app.appContainer.notificationManager.add(
                (NotificationListener) notificationLiveData::postValue);
    }

    public Notification getNotification() {
        return notificationLiveData.getValue();
    }
}