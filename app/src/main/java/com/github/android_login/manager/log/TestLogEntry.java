package com.github.android_login.manager.log;

import androidx.annotation.NonNull;

import com.github.android_login.service.log.LogEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TestLogEntry implements LogEntry {
    @NonNull
    @Override
    public String getLog() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.JAPAN);
        String log = sdf.format(Calendar.getInstance().getTime());
        return log;
    }
}
