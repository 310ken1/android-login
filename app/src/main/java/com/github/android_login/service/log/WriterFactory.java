package com.github.android_login.service.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedWriter;

public interface WriterFactory {
    @Nullable
    BufferedWriter getWriter(@NonNull LogEntry entry);
}
