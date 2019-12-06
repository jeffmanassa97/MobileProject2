package edu.fsu.cs.mobileproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class YahtzeeActivity extends AppCompatActivity implements PickNumberOfNames.goToSetNames, SetNames.goToGame{

    NumberPicker numPlayers;
    Button next;
    Intent nextPage;
    Bundle numNamesForNextPage;
    int numberOfTotalPlayers;
    List<String> playerNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        start();


    }

    public void start(){
        PickNumberOfNames fragment = new PickNumberOfNames();
        String tag = PickNumberOfNames.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment, tag).commit();
    }

    public void GoToSetNames(){
        SetNames fragment = new SetNames();
        String tag = SetNames.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment, tag).commit();
    }

    public void GoToGame(){
        PlayYahtzee fragment = new PlayYahtzee();
        String tag = PlayYahtzee.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragment, tag).commit();
    }


}
