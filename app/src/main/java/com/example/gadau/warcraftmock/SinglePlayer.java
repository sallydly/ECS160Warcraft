package com.example.gadau.warcraftmock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SinglePlayer extends AppCompatActivity {
    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        musicPlayer = MediaPlayer.create(this, R.raw.music_game1);
        musicPlayer.start();
        musicPlayer.setLooping(true);


        ImageView view = (ImageView) findViewById(R.id.tile);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.terrain);

        int offset = 50;
        Bitmap dstBitmap = Bitmap.createBitmap( //tile is 32x32
                32 + offset * 2, // Width
                32 + offset * 2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        Rect rectangle = new Rect(50, 50, 82, 82);
        Canvas canvas = new Canvas(dstBitmap);


        canvas.drawBitmap(bmp, new Rect(0, 160, 32, 192), rectangle, null);

        view.setImageBitmap(dstBitmap);

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
