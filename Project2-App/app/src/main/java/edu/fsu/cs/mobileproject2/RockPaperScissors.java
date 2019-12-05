package edu.fsu.cs.mobileproject2;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;

public class RockPaperScissors extends AppCompatActivity {

    ImageView playerCard;
    ImageView computerCard;
    ImageView winBanner;
    ImageView loseBanner;

    TextView playerScore;
    int playerNum;
    TextView computerScore;
    int computerNum;

    Button rockButton;
    Button paperButton;
    Button scissorsButton;

    SensorManager mSensorManager;
    Sensor lightSensor;
    SensorEventListener lightEventListener;

    boolean accelerometerPresent;
    Sensor flipSensor;
    SensorEventListener flipEventListener;

    Sensor shakeSensor;
    SensorEventListener shakeEventListener;
    float acelVal;  //Current Acceleration value and gravity
    float acelLast; //Last Acceleration value and gravity
    float shake;    //Difference of Acceleration value and gravity

    boolean lockOption = false;
    int playerOption = 0;
    int computerOption = 0;
    int playerWin = 0;

    Handler pauseHandle = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rps);

        playerCard = findViewById(R.id.player);
        computerCard = findViewById(R.id.computer);

        winBanner = findViewById(R.id.winPic);
        loseBanner = findViewById(R.id.losePic);

        playerScore = findViewById(R.id.playerScore);
        computerScore = findViewById(R.id.computerScore);

        rockButton = findViewById(R.id.rockButton);
        paperButton = findViewById(R.id.paperButton);
        scissorsButton = findViewById(R.id.scissorsButton);

        playerScore.setText("0");
        computerScore.setText("0");

        winBanner.setImageResource(android.R.color.transparent);
        loseBanner.setImageResource(android.R.color.transparent);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = mSensorManager.getDefaultSensor(TYPE_LIGHT);
        shakeSensor = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);
        flipSensor = mSensorManager.getDefaultSensor(TYPE_ACCELEROMETER);

        if (lightSensor == null) {
            Toast.makeText(this, "This device does not have a light sensor, please use a device with a light sensor.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            mSensorManager.registerListener(lightEventListener, lightSensor, 1000000, 1000000);
        }

        List<Sensor> sensorList = mSensorManager.getSensorList(TYPE_ACCELEROMETER);
        if (sensorList.size() > 0) {
            accelerometerPresent = true;
            mSensorManager.registerListener(flipEventListener, flipSensor, 1000000, 1000000);
            mSensorManager.registerListener(shakeEventListener, shakeSensor, 1000000, 1000000);
        } else {
            Toast.makeText(this, "This device does not have a accelerometer sensor, please use a device with a accelerometer sensor.", Toast.LENGTH_LONG).show();
            finish();
        }


        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
                    if (sensorEvent.values[0] == 0.0) {
                        //paper
                        if (lockOption == false) {
                            playerOption = 1;
                            computerOption = createRandomNumber();
                            playerCard.setImageResource(R.drawable.paper);
                            switch(computerOption)
                            {
                                case 1:
                                    computerCard.setImageResource(R.drawable.paper);
                                    Toast.makeText(getApplicationContext(), "TIE", Toast.LENGTH_LONG).show();
                                    break;
                                case 2:
                                    computerCard.setImageResource(R.drawable.rock);
                                    playerNum += 1;
                                    playerScore.setText(Integer.toString(playerNum));
                                    break;
                                case 3:
                                    computerCard.setImageResource(R.drawable.scissors);
                                    computerNum += 1;
                                    computerScore.setText(Integer.toString(computerNum));
                                    break;
                            }
                            createPause();
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        shakeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == TYPE_ACCELEROMETER) {
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];

                    acelLast = acelVal;
                    acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
                    float delta = acelVal - acelLast;
                    shake = shake * 0.9f + delta;

                    if (shake > 12) {
                        //rock
                        if (lockOption == false) {
                            playerOption = 2;
                            computerOption = createRandomNumber();
                            playerCard.setImageResource(R.drawable.rock);

                            switch(computerOption)
                            {
                                case 1:
                                    computerCard.setImageResource(R.drawable.paper);
                                    computerNum += 1;
                                    computerScore.setText(Integer.toString(computerNum));
                                    break;
                                case 2:
                                    computerCard.setImageResource(R.drawable.rock);
                                    Toast.makeText(getApplicationContext(), "TIE", Toast.LENGTH_LONG).show();
                                    break;
                                case 3:
                                    computerCard.setImageResource(R.drawable.scissors);
                                    winBanner.setVisibility(View.VISIBLE);
                                    playerNum += 1;
                                    playerScore.setText(Integer.toString(playerNum));
                                    break;
                            }
                        }
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        flipEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == TYPE_ACCELEROMETER) {
                    if (sensorEvent.values[2] <= 0) {
                        //scissors
                        if (lockOption == false) {
                            playerOption = 3;
                            computerOption = createRandomNumber();
                            playerCard.setImageResource(R.drawable.scissors);

                            switch(computerOption)
                            {
                                case 1:
                                    computerCard.setImageResource(R.drawable.paper);
                                    playerNum += 1;
                                    playerScore.setText(Integer.toString(playerNum));
                                    break;
                                case 2:
                                    computerCard.setImageResource(R.drawable.rock);
                                    computerNum += 1;
                                    computerScore.setText(Integer.toString(computerNum));
                                    break;
                                case 3:
                                    computerCard.setImageResource(R.drawable.scissors);
                                    Toast.makeText(getApplicationContext(), "TIE", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        rockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockOption = true;
                playerOption = 2;
                computerOption = createRandomNumber();
                playerCard.setImageResource(R.drawable.rock);
                switch(computerOption)
                {
                    case 1:
                        computerCard.setImageResource(R.drawable.paper);
                        playerWin = 2;
                        computerNum += 1;
                        computerScore.setText(Integer.toString(computerNum));
                        checkWin(playerWin);
                        lockOption = false;
                        break;
                    case 2:
                        computerCard.setImageResource(R.drawable.rock);
                        Toast.makeText(getApplicationContext(), "TIE", Toast.LENGTH_LONG).show();
                        lockOption = false;
                        break;
                    case 3:
                        computerCard.setImageResource(R.drawable.scissors);
                        playerWin = 1;
                        playerNum += 1;
                        playerScore.setText(Integer.toString(playerNum));
                        checkWin(playerWin);
                        lockOption = false;
                        break;
                }
            }
        });

        paperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockOption = true;
                playerOption = 1;
                computerOption = createRandomNumber();
                playerCard.setImageResource(R.drawable.paper);

                switch(computerOption)
                {
                    case 1:
                        computerCard.setImageResource(R.drawable.paper);
                        Toast.makeText(getApplicationContext(), "TIE", Toast.LENGTH_LONG).show();
                        lockOption = false;
                        break;
                    case 2:
                        computerCard.setImageResource(R.drawable.rock);
                        playerNum += 1;
                        playerScore.setText(Integer.toString(playerNum));
                        lockOption = false;
                        break;
                    case 3:
                        computerCard.setImageResource(R.drawable.scissors);
                        computerNum += 1;
                        computerScore.setText(Integer.toString(computerNum));
                        lockOption = false;
                        break;
                }
            }
        });

        scissorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockOption = true;
                playerOption = 3;
                computerOption = createRandomNumber();
                playerCard.setImageResource(R.drawable.scissors);

                switch(computerOption)
                {
                    case 1:
                        computerCard.setImageResource(R.drawable.paper);
                        playerNum += 1;
                        playerScore.setText(Integer.toString(playerNum));
                        lockOption = false;
                        break;
                    case 2:
                        computerCard.setImageResource(R.drawable.rock);
                        computerNum += 1;
                        computerScore.setText(Integer.toString(computerNum));
                        lockOption = false;
                        break;
                    case 3:
                        computerCard.setImageResource(R.drawable.scissors);
                        lockOption = false;
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(lightEventListener, lightSensor, 1000000, 1000000);
        mSensorManager.registerListener(shakeEventListener, shakeSensor, 1000000, 1000000);
        mSensorManager.registerListener(flipEventListener, flipSensor, 1000000, 1000000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(lightEventListener);
        mSensorManager.unregisterListener(shakeEventListener);
        mSensorManager.unregisterListener(flipEventListener);
    }

    public int createRandomNumber() {
        int random = new Random().nextInt(3) + 1;
        return random;
    }

    public void createPause()
    {
        pauseHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetOption();
            }
        }, 4000);
    }

    public void checkWin(int winNum)
    {
        Activity act = this;
        if(winNum == 0)
        {
            Toast.makeText(getApplicationContext(), "TIE", Toast.LENGTH_LONG).show();
        }
        else if(winNum == 1)
        {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    winBanner.setImageResource(R.drawable.wintrans);
                    createPause();
                    winBanner.setImageResource(android.R.color.transparent);
                    playerWin = 0;
                }
            });
        }
        else
        {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loseBanner.setImageResource(R.drawable.losetrans);
                    createPause();
                    loseBanner.setImageResource(android.R.color.transparent);
                    playerWin = 0;
                }
            });
        }
    }

    public void resetOption()
    {
        playerOption = 0;
        computerOption = 0;
    }
}
