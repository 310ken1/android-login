package com.github.android_login;

import android.app.Application;

public class AndroidLoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }

    public AppContainer appContainer = null;
}
