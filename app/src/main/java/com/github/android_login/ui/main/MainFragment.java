package com.github.android_login.ui.main;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.databinding.MainFragmentBinding;
import com.github.android_login.ui.login.LoginFragment;
import com.github.android_login.ui.login.LoginViewModel;

public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    private MainViewModel mViewModel;
    private MainFragmentBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.vibrator.setOnClickListener(v -> {
            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{0, 1000, 400, 200, 400, 200}, -1);
        });

        binding.alerm.setOnClickListener(v -> {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(requireContext(), uri);
            ringtone.play();
        });
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
