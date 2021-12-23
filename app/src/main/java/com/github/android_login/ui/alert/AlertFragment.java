package com.github.android_login.ui.alert;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.databinding.AlertFragmentBinding;
import com.github.android_login.manager.notification.Notification;
import com.github.android_login.manager.notification.NotificationHigh;

public class AlertFragment extends DialogFragment {
    public static final String TAG = AlertFragment.class.getSimpleName();

    private AlertViewModel mViewModel;
    private AlertFragmentBinding binding;

    public static AlertFragment newInstance() {
        return new AlertFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AlertFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AlertViewModel.class);
        Notification notification = mViewModel.getNotification();
        if (notification instanceof NotificationHigh) {
            NotificationHigh high = (NotificationHigh)notification;
            binding.text.setText(String.format("battery:%b storage:%b", high.battery, high.storage));
        }
    }
}
