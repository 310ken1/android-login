package com.github.android_login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.android_login.ui.login.LoginFragment;
import com.github.android_login.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }
}