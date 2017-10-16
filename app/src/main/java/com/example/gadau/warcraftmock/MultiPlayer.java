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

    /*
     * Currently overriden to finish activity upon a left to right swipe
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float deltaX, deltaY;

        //This block has different cases for what the finger is doing (touching screen, moving, etc)
        switch(motionEvent.getAction()) {
            //ACTION_DOWN happens when finger first makes contact
            case MotionEvent.ACTION_DOWN:
                //Get initial coords of finger
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Constantly get coords of finger
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();

                //Calculate distance moved from ACTION_DOWN
                deltaX = x2 - x1;
                deltaY = y2 - y1;

                //If finger has moved far enough and not too diagonally
                if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MIN_DISTANCE / 2) {
                    //If finger is to the right of initial x1
                    if (x2 > x1) {
                        //finish activity
                        finish();
                    }
                }
                break;
        }
        return super.onTouchEvent(motionEvent);

    }
}
