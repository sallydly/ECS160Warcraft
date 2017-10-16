package com.example.gadau.warcraftmock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class MultiPlayer extends AppCompatActivity {
    private double x1, x2, y1, y2;
    private static final double MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
    }

    /**
     * Finishes activity upon a left to right swipe
     *
     * @param motionEvent : Records input details from the touch screen
     * @return : Returns true if you have consumed the event, false if you haven't.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        double deltaX, deltaY;
        final int MOTION_EVENT_ACTION = motionEvent.getAction();

        //This block has different cases for what the finger is doing (touching screen, moving, etc)
        switch(MOTION_EVENT_ACTION) {
            //ACTION_DOWN happens when finger first makes contact
            case MotionEvent.ACTION_DOWN:
                //Get initial coordinate of finger
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Constantly get coordinates of finger
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();

                //Calculate distance moved from Initial Position
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
