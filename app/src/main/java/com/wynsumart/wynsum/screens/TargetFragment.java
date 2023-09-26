package com.wynsumart.wynsum.screens;

import static com.google.android.material.color.MaterialColors.getColor;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

public class TargetFragment extends Fragment {
    private TargetFragmentLayoutBinding binding;
    private final static String TARGET_ID_KAY = "TARGET_ID";
    private int targetId = -1;
    private TargetFragmentVM model;
    private float animationProgress;
    private MediaPlayer musicPlayer;

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
        targetId = getArguments().getInt(TARGET_ID_KAY);
        musicPlayer = initMediaPlayer();
        setMusicTrack("https://cdn.pixabay.com/audio/2023/09/13/audio_ee2221f74c.mp3");
    }

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
        binding.playTargetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.startMeditation();
            }
        });
        binding.musicSwitcherButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked & model.isTimerGoing().getValue()){
                    musicPlayer.start();
                }
                if (!isChecked & musicPlayer.isPlaying()) {musicPlayer.pause();}
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
                changeAnimation();
                binding.playTargetButton.setImageResource(R.drawable.play_pause_24);
                if (binding.musicSwitcherButton.isChecked()){
                    musicPlayer.start();
                }
            } else {
                animationProgress = binding.playAnimation.getProgress();
                binding.playAnimation.pauseAnimation();
                changeAnimation();
                binding.playTargetButton.setImageResource(R.drawable.play_24);
                if (musicPlayer.isPlaying()){musicPlayer.pause();}
            }
        });
        changeAnimation();
    }

    private MediaPlayer initMediaPlayer(){
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

    private void setMusicTrack(String link){
        musicPlayer.reset();
        try {
            musicPlayer.setDataSource(link);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        musicPlayer.prepareAsync(); // might take long! (for buffering, etc)
    }

    private void changeAnimation() {
        resetAnimationView();
        LottieAnimationView animationView = binding.playAnimation;
        animationView.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getContext().getColor(R.color.primaryGreenDark), PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );
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