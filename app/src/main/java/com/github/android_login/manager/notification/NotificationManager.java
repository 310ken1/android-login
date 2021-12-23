package com.github.android_login.manager.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;

import com.github.android_login.common.Notifier;

public class NotificationManager extends Notifier<NotificationListener> {
    private final Context context;
    private final Handler handler;

    private final NotificationHigh high = new NotificationHigh();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.post(() -> {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging =
                        status == BatteryManager.BATTERY_STATUS_CHARGING ||
                                status == BatteryManager.BATTERY_STATUS_FULL;
                int battery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                if (battery < 95) {
                    NotificationManager.this.notify(listener -> {
                        high.battery = true;
                        listener.onNotify(high);
                    });
                }
            });
        }
    };

    public NotificationManager(Context context) {
        this.context = context;
        this.handler = new Handler();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(receiver, filter);
    }

    public void close() {
        context.unregisterReceiver(receiver);
    }
}
