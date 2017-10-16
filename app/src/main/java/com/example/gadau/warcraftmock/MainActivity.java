package com.example.gadau.warcraftmock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer musicPlayer;
    private MediaPlayer buttonSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Make a constant or preference to determine playback
        musicPlayer = MediaPlayer.create(this, R.raw.music_menu);
        buttonSound = MediaPlayer.create(this, R.raw.misc_tick);
        musicPlayer.start();
        musicPlayer.setLooping(true);
    }

    public void onButtonClick(View view) {
        Intent mainMenuIntent;
        final int VIEW_ID = view.getId();

        switch(VIEW_ID) {
            case R.id.singlePlayerButton:
                mainMenuIntent = new Intent(this, SinglePlayer.class);
                break;
            case R.id.multiPlayerButton:
                mainMenuIntent = new Intent(this, MultiPlayer.class);
                break;
            case R.id.optionsButton:
                mainMenuIntent = new Intent(this, Options.class);
                break;
            default:
                return;
        }

        buttonSound.start();
        buttonSound.setLooping(false);
        startActivity(mainMenuIntent);
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

    @Override
    protected void onStop() {
        super.onStop();
        musicPlayer.stop();
    }

    //TODO: Set Buttons

    //TODO: Remember to stop music when launching new activity
}
