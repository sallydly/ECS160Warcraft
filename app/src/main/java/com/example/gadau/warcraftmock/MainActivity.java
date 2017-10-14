package com.example.gadau.warcraftmock;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: Make a constant or preference to determine playback
        mediaPlayer = MediaPlayer.create(this, R.raw.menu);
        mediaPlayer1 = MediaPlayer.create(this, R.raw.tick);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
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

        mediaPlayer1.start();
        mediaPlayer1.setLooping(false);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    //TODO: Set Buttons

    //TODO: Remember to stop music when launching new activity
}
