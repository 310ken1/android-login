package com.github.android_login.service.log;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger {

    private final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>();
    private boolean active;

    public Logger(@NonNull WriterFactory factory) {
        active = true;

        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            BufferedWriter writer = null;
            try {
                long last = System.currentTimeMillis();
                while (active) {
                    LogEntry entry = logQueue.take();
                    writer = factory.getWriter(entry);
                    if (null == writer) throw new IOException();
                    writer.write(entry.getLog());
                    writer.newLine();

                    long time = System.currentTimeMillis();
                    if (time - last > 1000) {
                        writer.flush();
                        last = time;
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void close() {
        active = false;
        logQueue.add(null);
    }

    public void write(@NonNull LogEntry entry) {
        logQueue.add(entry);
    }
}
