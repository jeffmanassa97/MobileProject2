package edu.fsu.cs.mobileproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class War extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "War activity";
    ImageView deal;
    ImageView player1Card;
    ImageView player2Card;
    TextView player1Score;
    TextView player2Score;
    int player1, player2;

    private SensorManager mSensorManager;
    private Sensor mGravitySensor;
    private boolean mFacingDown;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float factor = 0.95F;

        if (event.sensor == mGravitySensor) {
            boolean nowDown = event.values[2] < -SensorManager.GRAVITY_EARTH * factor;
            if (nowDown != mFacingDown) {
                if (nowDown) {
                    deal();
                    Log.i(TAG, "DOWN");
                } else {
                    Log.i(TAG, "UP");
                }
                mFacingDown = nowDown;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war);

        player1 = 0;
        player2 = 0;
        deal = findViewById(R.id.deal);
        player1Card = findViewById(R.id.Player1);
        player2Card = findViewById(R.id.Player2);
        player1Score = findViewById(R.id.Player1ScoreNum);
        player2Score = findViewById(R.id.Player2ScoreNum);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        player1Score.setText(Integer.toString(player1));
        player2Score.setText(Integer.toString(player2));

        if (mGravitySensor == null) {
            Log.w(TAG, "Device has no gravity sensor");
        }

        deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deal();
            }
        });

        //deal.setOnTouchListener(touchListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGravitySensor != null) {
            mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void deal(){

        Random r = new Random();
        // Randomize two numbers
        int leftRandomNumber = r.nextInt(13) + 2; // 2-14
        int rightRandomNumber = r.nextInt(13) + 2;

        // Change the image views
        String mDrawableName;
        int resID;
        Resources res = getResources();
        mDrawableName = "card" + leftRandomNumber;
        resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        player1Card.setImageResource(resID);
        mDrawableName = "card" + rightRandomNumber;
        resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        player2Card.setImageResource(resID);

        // Compare the numbers
        if (leftRandomNumber > rightRandomNumber){
            player1 += 1;
            player1Score.setText(Integer.toString(player1));
        }
        else if (leftRandomNumber < rightRandomNumber){
            player2 += 1;
            player2Score.setText(Integer.toString(player2));
        }
    }
}
