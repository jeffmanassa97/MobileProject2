package edu.fsu.cs.mobileproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        Button htpERS = (Button)findViewById(R.id.ers_info_button);
        Button htpRPS = (Button)findViewById(R.id.rps_info_button);
        Button htpSpoons = (Button)findViewById(R.id.spoons_info_button);
        Button htpWar = (Button)findViewById(R.id.war_info_button);
        Button htpYahtzee = (Button)findViewById(R.id.button15);
        Button playWar = (Button)findViewById(R.id.play_war_button);

        htpERS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectGameActivity.this, HowToPlayERS.class));
            }
        });

        htpRPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectGameActivity.this, HowToPlayRPS.class));
            }
        });

        htpSpoons.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectGameActivity.this, HowToPlaySpoons.class));
            }
        });

        htpWar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectGameActivity.this, HowToPlayWar.class));
            }
        });

        htpYahtzee.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectGameActivity.this, HowToPlayYahtzee.class));
            }
        });

        playWar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectGameActivity.this, War.class));
            }
        });
    }
}
