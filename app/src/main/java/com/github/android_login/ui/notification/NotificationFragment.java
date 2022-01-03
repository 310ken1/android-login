package com.github.android_login.ui.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.R;
import com.github.android_login.databinding.NotificationFragmentBinding;
import com.github.android_login.manager.notification.NotificationState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotificationFragment extends DialogFragment {
    public static final String TAG = NotificationFragment.class.getSimpleName();

    private NotificationViewModel mViewModel;
    private NotificationFragmentBinding binding;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotificationFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        updateState(mViewModel.getState());
        mViewModel.getStateLiveData().observe(this, this::updateState);
    }

    private void updateState(NotificationState state) {
        Log.d(TAG, "updateState");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss", Locale.JAPAN);
        List<Map<String, String>> data = new ArrayList<>();
        if (!state.bluetooth.enable) {
            data.add(new HashMap<String, String>() {{
                put("timestamp", sdf.format(state.bluetooth.timestamp));
                put("message", "Bluetooth OFF");
            }});
        }
        if (!state.wifi.enable) {
            data.add(new HashMap<String, String>() {{
                put("timestamp", sdf.format(state.wifi.timestamp));
                put("message", "Wi-Fi OFF");
            }});
        }

        SimpleAdapter adapter = new SimpleAdapter(requireContext(), data, R.layout.notification_item,
                new String[]{"timestamp", "message"}, new int[]{R.id.timestamp, R.id.message});
        binding.listView.setAdapter(adapter);
    }
}
