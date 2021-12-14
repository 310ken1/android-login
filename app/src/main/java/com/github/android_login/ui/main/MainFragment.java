package com.github.android_login.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.R;
import com.github.android_login.ui.login.LoginFragment;
import com.github.android_login.ui.login.LoginViewModel;

public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginViewModel model = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        if (!model.isLoggedIn()) {
            LoginFragment.newInstance().show(getChildFragmentManager(), LoginFragment.TAG);
        }
    }
}
