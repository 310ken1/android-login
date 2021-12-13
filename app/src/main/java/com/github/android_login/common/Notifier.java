package com.github.android_login.common;

import java.util.ArrayList;

public abstract class Notifier<T> {
    private final ArrayList<T> listeners = new ArrayList<T>();

    public void add(T listener) {
        listeners.add(listener);
    }

    public void remote(T listener) {
        listeners.remove(listener);
    }

    protected void notify(Notice<T> notice) {
        for (T listener : listeners) {
            notice.exec(listener);
        }
    }

    public interface Notice<T> {
        void exec(T listener);
    }
}
