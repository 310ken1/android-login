package com.github.android_login.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.R;
import com.github.android_login.databinding.FragmentLoginBinding;

public class LoginFragment extends DialogFragment {
    public static final String TAG = LoginFragment.class.getSimpleName();

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private final TextWatcher afterTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            loginViewModel.loginDataChanged(
                    binding.username.getText().toString(),
                    binding.password.getText().toString());
        }
    };

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setStyle(STYLE_NORMAL, R.style.Theme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        loginViewModel.getLoginFormValid().observe(getViewLifecycleOwner(),
                loginFormState -> binding.login.setEnabled(loginFormState));
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(),
                loginResult -> {
                    if (loginResult) dismiss();
                }
        );

        binding.username.addTextChangedListener(afterTextChangedListener);
        binding.password.addTextChangedListener(afterTextChangedListener);

        binding.login.setOnClickListener(v -> {
            loginViewModel.login(
                    binding.username.getText().toString(),
                    binding.password.getText().toString());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        binding = null;
    }
}
