package com.github.android_login.common;

import java.util.ArrayList;

public abstract class Notifier<T> {
    private final ArrayList<T> listeners = new ArrayList<T>();

    public void add(T listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void remove(T listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected void notify(Notice<T> notice) {
        synchronized(listeners) {
            for (T listener : listeners) {
                notice.exec(listener);
            }
        }
    }

    public interface Notice<T> {
        void exec(T listener);
    }
}
