package com.github.android_login.manager.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.github.android_login.common.Notifier;

public class NotificationManager extends Notifier<NotificationListener> {
    private static final String TAG = NotificationManager.class.getSimpleName();
    private static final boolean DEBUG = true;

    private final Context context;

    private final int batteryStep = 5;
    private int batteryThresholdMax = 50;
    private int batteryThreshold = batteryThresholdMax;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) return;
            switch (intent.getAction()) {
                case Intent.ACTION_BATTERY_CHANGED:
                    checkBattery(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public NotificationManager(Context context) {
        this.context = context;

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(receiver, filter);
    }

    public void close() {
        context.unregisterReceiver(receiver);
    }

    private void checkBattery(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if (DEBUG) Log.d(TAG,
                "isCharging=" + isCharging +
                        " level=" + level +
                        " batteryThreshold=" + batteryThreshold);
        if (batteryThreshold < level) {
            int value = threshold(level, batteryStep);
            batteryThreshold = Math.min(value, batteryThresholdMax);
        } else {
            int value = threshold(level, batteryStep);
            batteryThreshold = Math.max(value, 0);
            notify(new NotificationBattery(true, level));
        }
    }

    private void notify(Notification notification) {
        notify(listener -> {
            listener.onNotify(notification);
        });
    }

    private int threshold(int value, int step) {
        int base = (0 == (value % step)) ? value - 1 : value;
        return (base / step) * step;
    }
}
