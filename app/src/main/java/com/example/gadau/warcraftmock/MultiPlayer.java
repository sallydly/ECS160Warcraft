package com.example.gadau.warcraftmock;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class MultiPlayer extends AppCompatActivity {
    float x1, x2, y1, y2;
    static final float MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float deltaX, deltaY;

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();
                deltaX = x2 - x1;
                deltaY = y2 - y1;

                if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MIN_DISTANCE / 2) {
                    if (x2 > x1) {
                        Toast.makeText(this, "swipe from left to right", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
        return super.onTouchEvent(motionEvent);

    }
}
