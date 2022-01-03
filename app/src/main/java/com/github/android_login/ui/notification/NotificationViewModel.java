package com.github.android_login.ui.notification;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.github.android_login.AndroidLoginApplication;
import com.github.android_login.manager.notification.NotificationManager;
import com.github.android_login.manager.notification.NotificationState;

public class NotificationViewModel extends AndroidViewModel {
    private static final String TAG = NotificationViewModel.class.getSimpleName();
    private MutableLiveData<NotificationState> stateLiveData = new MutableLiveData<>();
    private NotificationManager notificationManager;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationManager = ((AndroidLoginApplication) application).appContainer.notificationManager;

        notificationManager.add(notification -> {
            Log.d(TAG, "update NotificationState");
            if (notification instanceof NotificationState) {
                NotificationState state = (NotificationState) notification;
                stateLiveData.postValue(state);
            }
        });
    }

    public MutableLiveData<NotificationState> getStateLiveData() {
        return stateLiveData;
    }

    public NotificationState getState() {
        return notificationManager.getState();
    }
}
