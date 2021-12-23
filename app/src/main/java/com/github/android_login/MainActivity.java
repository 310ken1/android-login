package com.github.android_login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.manager.notification.Notification;
import com.github.android_login.manager.notification.NotificationHigh;
import com.github.android_login.ui.alert.AlertFragment;
import com.github.android_login.ui.alert.AlertViewModel;
import com.github.android_login.ui.login.LoginViewModel;
import com.github.android_login.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private AlertViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        model = new ViewModelProvider(this).get(AlertViewModel.class);
        model.notificationLiveData.observe(this, notification -> {
            if (notification instanceof NotificationHigh) {
                AlertFragment.newInstance().show(getSupportFragmentManager(), AlertFragment.TAG);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoginViewModel model = new ViewModelProvider(this).get(LoginViewModel.class);
        model.logout();
    }

    @Override
    public void onBackPressed() {

    }
}
