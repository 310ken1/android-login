package com.github.android_login.service.log;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger {

    private final BlockingQueue<Entry> logQueue = new LinkedBlockingQueue<>();
    private boolean active;

    public Logger(@NonNull WriterFactory factory) {
        active = true;

        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            BufferedWriter writer = null;
            try {
                while (active) {
                    Entry entry = logQueue.take();
                    if (entry instanceof LogEntry) {
                        LogEntry e = (LogEntry) entry;
                        writer = factory.getWriter(e);
                        if (null == writer) throw new IOException();
                        writer.write(e.getLog());
                        writer.newLine();
                        writer.flush();
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
        logQueue.add(new CloseEntry());
    }

    public void write(@NonNull Entry entry) {
        logQueue.add(entry);
    }
}
