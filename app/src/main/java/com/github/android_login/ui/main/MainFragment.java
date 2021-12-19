package com.github.android_login.ui.main;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.github.android_login.R;
import com.github.android_login.databinding.MainFragmentBinding;
import com.github.android_login.ui.login.LoginFragment;
import com.github.android_login.ui.login.LoginViewModel;
import com.github.android_login.ui.map.MapFragment;

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
        Log.d(TAG, "onCreateView()");
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.map.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            MapFragment fragment = (MapFragment) fragmentManager.findFragmentByTag(MapFragment.TAG);
            if (null == fragment) {
                fragmentManager.beginTransaction()
                        .add(R.id.container, MapFragment.class, null, MapFragment.TAG)
                        .setReorderingAllowed(true)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .show(fragment)
                        .commit();
            }
        });

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
        Log.d(TAG, "onResume()");
        LoginViewModel model = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        if (!model.isLoggedIn()) {
            LoginFragment.newInstance().show(getChildFragmentManager(), LoginFragment.TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        binding = null;
    }
}
