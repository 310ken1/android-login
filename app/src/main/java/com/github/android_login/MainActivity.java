package com.github.android_login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.manager.notification.NotificationBattery;
import com.github.android_login.manager.notification.NotificationState;
import com.github.android_login.ui.alert.AlertViewModel;
import com.github.android_login.ui.login.LoginViewModel;
import com.github.android_login.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private AlertViewModel model;
    private AlertDialog batteryDialog = null;

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
            if (notification instanceof NotificationBattery) {
                showBatteryAlert((NotificationBattery) notification);
            } else if (notification instanceof NotificationState) {
                NotificationState n = (NotificationState) notification;
                Log.d(TAG, "wifi:" + n.wifi.enable +
                        " bluetooth:" + n.bluetooth.enable);
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

    private void showBatteryAlert(NotificationBattery notification) {
        if (null != batteryDialog) batteryDialog.dismiss();
        batteryDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_dialog_title)
                .setMessage(String.format(getString(R.string.alert_dialog_message_format), notification.level))
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    batteryDialog = null;
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}
