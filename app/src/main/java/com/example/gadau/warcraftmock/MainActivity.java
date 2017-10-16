package com.example.gadau.warcraftmock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer musicPlayer;
    MediaPlayer buttonSound;

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

    public void onBtnClick(View view) {
        Intent intent;

        switch(view.getId()) {
            case R.id.singlePlayerBtn:
                intent = new Intent(this, SinglePlayer.class);
                break;
            case R.id.multiPlayerBtn:
                intent = new Intent(this, MultiPlayer.class);
                break;
            case R.id.optionsBtn:
                intent = new Intent(this, Options.class);
                break;
            default:
                return;
        }

        buttonSound.start();
        buttonSound.setLooping(false);
        startActivity(intent);
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
