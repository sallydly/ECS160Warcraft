package com.example.gadau.warcraftmock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float deltaX, deltaY;

        TextView textView = (TextView) findViewById(R.id.optionsTextView);
        TextView xCoordDisplay = (TextView) findViewById(R.id.xCoordOptions);
        TextView yCoordDisplay = (TextView) findViewById(R.id.yCoordOptions);

        textView.setText(motionEvent.actionToString(motionEvent.getActionMasked()));
        xCoordDisplay.setText(Float.toString(motionEvent.getX()));
        yCoordDisplay.setText(Float.toString(motionEvent.getY()));
        return super.onTouchEvent(motionEvent);

    }
}
