package com.github.android_login.service.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyWriterFactory implements WriterFactory {
    private final String dir;
    private final String name;
    private BufferedWriter writer;
    private File file;

    public DailyWriterFactory(@NonNull String dir, @NonNull String name) {
        this.dir = dir;
        this.name = name;
    }

    @Override
    public @Nullable
    BufferedWriter getWriter(@NonNull LogEntry entry) {
        File file = createFile(dir, name);
        if (!file.equals(this.file)) {
            this.file = file;
            this.writer = createWriter(file);
        }
        ;
        return this.writer;
    }

    public @NonNull
    File createFile(@NonNull String dir, @NonNull String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return new File(dir + sdf.format(Calendar.getInstance().getTime()) + name);
    }

    public @Nullable
    BufferedWriter createWriter(@NonNull File file) {
        BufferedWriter writer = null;
        try {
            file.createNewFile();
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, true), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }
}
