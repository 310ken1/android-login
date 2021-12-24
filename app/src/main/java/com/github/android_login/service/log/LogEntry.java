package com.github.android_login.service.log;

import androidx.annotation.NonNull;

public interface LogEntry extends Entry {
    @NonNull
    String getLog();
}
