package com.wynsumart.wynsum.screens;

import static com.google.android.material.color.MaterialColors.getColor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.wynsumart.wynsum.MainActivity;
import com.wynsumart.wynsum.R;
import com.wynsumart.wynsum.databinding.TargetFragmentLayoutBinding;
import com.wynsumart.wynsum.view_models.TargetFragmentVM;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class TargetFragment extends Fragment {
    private TargetFragmentLayoutBinding binding;
    private final static String TARGET_ID_KAY = "TARGET_ID";
    private int targetId = -1;
    private TargetFragmentVM model;
    private float animationProgress;
    private MediaPlayer musicPlayer;
    private Handler handler;
    private boolean isMeditationGoing;
    private long startTimeMillis = 0; // for long click

    public static TargetFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.getInt(TARGET_ID_KAY, id);
        TargetFragment fragment = new TargetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        targetId = getArguments().getInt(TARGET_ID_KAY);
        musicPlayer = initMediaPlayer();
        setMusicTrack("https://cdn.pixabay.com/audio/2023/09/13/audio_ee2221f74c.mp3");
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // NOT WORKING YET
                if (isMeditationGoing) {
                    showDialog();
//                    ((MainActivity) requireActivity()).showToast("Pause long press");
                } else {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TargetFragmentLayoutBinding.inflate(inflater, container, false);
        binding.buttonBackTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).goBack();
            }
        });
        binding.musicSwitcherButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked & model.isTimerGoing().getValue()) {
                    musicPlayer.start();
                }
                if (!isChecked & musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                }
            }
        });
        binding.playTargetButton.setOnTouchListener(new View.OnTouchListener() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    binding.loaderAnimation.setVisibility(View.VISIBLE);
                    binding.loaderAnimation.playAnimation();
                    binding.loaderAnimation.setSpeed(1.4F);
                    changeAnimation(-1);
                }
            };
            Runnable vibrate = new Runnable() {
                @Override
                public void run() {
                    model.finishMeditation();
                    binding.loaderAnimation.pauseAnimation();
                    ((Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(Vibrator.VIBRATION_EFFECT_SUPPORT_YES);
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // need to rewrite with Rx or Threads
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTimeMillis = System.currentTimeMillis();
                        model.startMeditation();
                        handler.postDelayed(runnable, 500);
                        handler.postDelayed(vibrate, 5000);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(runnable);
                        handler.removeCallbacks(vibrate);
                        binding.loaderAnimation.pauseAnimation();
                        binding.loaderAnimation.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(TargetFragmentVM.class);
        model.getTimeLeft().observe(getViewLifecycleOwner(), timeLeft -> {
            binding.sessionProgress.setProgress(Math.toIntExact(timeLeft));
        });
        model.isTimerGoing().observe(getViewLifecycleOwner(), isGoing -> {
            if (isGoing) {
                binding.playAnimation.playAnimation();
                binding.playAnimation.setProgress(animationProgress);
                changeAnimation(-1);
                binding.playTargetButton.setBackgroundResource(R.drawable.play_pause_24);
                if (binding.musicSwitcherButton.isChecked()) {
                    musicPlayer.start();
                }
            } else {
                animationProgress = binding.playAnimation.getProgress();
                binding.playAnimation.pauseAnimation();
                changeAnimation(-1);
                binding.playTargetButton.setBackgroundResource(R.drawable.play_24);
                if (musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                }
            }
        });
        model.isMeditationGoing().observe(getViewLifecycleOwner(), isMGoing -> {
            isMeditationGoing = isMGoing;
            Log.d("iks", "STOPPED");
            if (!isMGoing) {
                if (musicPlayer != null) {
                    if (musicPlayer.isPlaying()) {
                        musicPlayer.pause();
                    }
                    musicPlayer.seekTo(0);
                }
            }
        });
    }

    private MediaPlayer initMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.musicSwitcherButton.setVisibility(View.VISIBLE);
            }
        });
        return mediaPlayer;
    }

    private void setMusicTrack(String link) {
        musicPlayer.reset();
        try {
            musicPlayer.setDataSource(link);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        musicPlayer.prepareAsync(); // might take long! (for buffering, etc)
    }

    private void changeAnimation(int colour) {
        resetAnimationView();
        LottieAnimationView animationView = binding.playAnimation;
        animationView.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getContext().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );

        binding.loaderAnimation.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getContext().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                    }
                });
    }

    private void resetAnimationView() {
        binding.playAnimation.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
        binding.loaderAnimation.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return null;
                    }
                }
        );
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setIcon(null)
                .setTitle("Finish session?")
                .setMessage("Would you like to finish the session?")
                .setPositiveButton("Yes", null)
                .setNeutralButton("No", null)
                .create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.sessionProgress.setMax(Math.toIntExact(model.getTimeLeft().getValue()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.release();
        musicPlayer = null;
    }
}