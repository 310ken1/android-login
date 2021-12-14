package com.github.android_login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.ui.login.LoginViewModel;
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

    @Override
    protected void onPause() {
        super.onPause();
        LoginViewModel model = new ViewModelProvider(this).get(LoginViewModel.class);
        model.logout();
    }

    @Override
    public void onBackPressed() {

    }
}
