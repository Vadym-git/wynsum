package com.wynsumart.wynsum.business_logic;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MusicPlayerService extends Service {
    MediaPlayer musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
//                binding.musicSwitcherButton.setVisibility(View.VISIBLE);
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


}
