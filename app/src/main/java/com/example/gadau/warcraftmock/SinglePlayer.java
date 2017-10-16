package com.example.gadau.warcraftmock;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SinglePlayer extends AppCompatActivity {
    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        musicPlayer = MediaPlayer.create(this, R.raw.music_game1);
        musicPlayer.start();
        musicPlayer.setLooping(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayer.start();
    }
}
