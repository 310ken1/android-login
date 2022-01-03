package com.github.android_login.manager.notification;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.StatFs;
import android.util.Log;

import com.github.android_login.common.Notifier;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

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
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    checkBluetooth(intent);
                default:
                    break;
            }
        }
    };

    int count = 10;
    int life = 10 * 1000;

    public NotificationManager(Context context) {
        this.context = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(receiver, filter);

        ConcurrentHashMap<Integer, State> map = new ConcurrentHashMap<>();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkStorage();
//                if (0 < count) {
//                    map.put(count, new State(true));
//                    count--;
//                } else {
//                    long current = System.currentTimeMillis();
//                    for(Iterator<State> i = map.values().iterator(); i.hasNext();){
//                        State s = i.next();
//                        if((s.timestamp + life) < current){
//                            i.remove();
//                        }
//                    }
//                }
//                Log.d(TAG, "size=" + map.size());
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

    public NotificationState getState() {
        return this.state;
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
        final int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
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

    private void checkBluetooth(Intent intent) {
        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                BluetoothAdapter.ERROR);
        boolean change = false;
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                if (DEBUG) Log.d(TAG, "Bluetooth:OFF");
                this.state.bluetooth = new State(false);
                change = true;
                break;
            case BluetoothAdapter.STATE_ON:
                if (DEBUG) Log.d(TAG, "Bluetooth:ON");
                this.state.bluetooth = new State(true);
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
