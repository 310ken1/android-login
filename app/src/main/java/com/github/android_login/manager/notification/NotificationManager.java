package com.github.android_login.manager.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.StatFs;
import android.util.Log;

import com.github.android_login.common.Notifier;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationManager extends Notifier<NotificationListener> {
    private static final String TAG = NotificationManager.class.getSimpleName();
    private static final boolean DEBUG = true;

    private final Context context;

    private final int batteryStep = 5;
    private int batteryThresholdMax = 50;
    private int batteryThreshold = batteryThresholdMax;

    private final int storageStep = 5;
    private final String storagePath = "/sdcard";
    private int storageThresholdMax = 50;
    private int storageThreshold = storageThresholdMax;
    private Timer timer;
    private final int interval = 10000;

    private final NotificationState state = new NotificationState();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) return;
            switch (intent.getAction()) {
                case Intent.ACTION_BATTERY_CHANGED:
                    checkBattery(intent);
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    checkWifi(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public NotificationManager(Context context) {
        this.context = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(receiver, filter);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkStorage();
            }
        }, 0, interval);
    }

    public void close() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
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
            notify(new NotificationBattery(level));
        }
        if (DEBUG) Log.d(TAG, "batteryThreshold=" + batteryThreshold);
    }

    private void checkStorage() {
        StatFs statFs = new StatFs(storagePath);
        double total = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
        double free = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
        int rate = (int) (free / total * 100.0);
        if (DEBUG) Log.d(TAG,
                "total=" + total +
                        " free=" + free +
                        " rate=" + rate +
                        " storageThreshold=" + storageThreshold);
        if (storageThreshold < rate) {
            int value = threshold(rate, storageStep);
            storageThreshold = Math.min(value, batteryThresholdMax);
        } else {
            int value = threshold(rate, storageStep);
            storageThreshold = Math.max(value, 0);
            notify(new NotificationBattery(rate));
        }
        if (DEBUG) Log.d(TAG, "storageThreshold=" + storageThreshold);
    }

    private void checkWifi(Intent intent) {
        int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_DISABLED);
        boolean change = false;
        switch (state) {
            case WifiManager.WIFI_STATE_DISABLED:
                if (DEBUG) Log.d(TAG, "WiFi:OFF");
                this.state.wifi = new State(false);
                change = true;
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                if (DEBUG) Log.d(TAG, "WiFi:ON");
                this.state.wifi = new State(true);
                change = true;
                break;
            default:
                break;
        }
        if (change) {
            notify(this.state);
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
