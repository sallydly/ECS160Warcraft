package com.example.gadau.warcraftmock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

    }

    /**
     * Overridden to display MotionEvent values
     * e.g. x coordinate from motionEvent.getX()
     *
     * @param motionEvent : Event to process
     * @return : Returns true if you have consumed the event, false if you haven't.
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        double deltaX, deltaY;

        TextView textView = (TextView)findViewById(R.id.optionsTextView);
        TextView xCoordDisplay = (TextView)findViewById(R.id.xCoordOptions);
        TextView yCoordDisplay = (TextView)findViewById(R.id.yCoordOptions);

        textView.setText(MotionEvent.actionToString(motionEvent.getActionMasked()));
        xCoordDisplay.setText(String.valueOf(motionEvent.getX()));
        yCoordDisplay.setText(String.valueOf(motionEvent.getY()));

        return super.onTouchEvent(motionEvent);
    }
}
