package edu.fsu.cs.mobileproject2;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class PlayYahtzee extends Fragment {

    YahtzeeActivity main;
    List<Player> thePlayers;
    TextView playerName;
    ImageButton die1, die2, die3, die4, die5;
    int select1, select2, select3, select4, select5 = 0;
    int numPlayers;
    int cycleThroughPlayers = 0;
    Button reroll;
    Button finishTurn;
    Spinner pickCategory;
    Resources res;
    boolean goToNextTurn = false;
    int numberOfRoundsElapsed = 0;
    Button crossOutConfirm;
    boolean supposedToCrossOut = false;
    int canReroll = 2;
    boolean gameDone = false;



    public PlayYahtzee(){
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.yahtzeegame, container, false);
        res = getResources();

        //code for displaying whose turn
//        if(cycleThroughPlayers == numPlayers-1){
//            playerName.setText(thePlayers.get(0).getName());
//            cycleThroughPlayers = 0;
//        }
//        else{
//            playerName.setText(thePlayers.get(++cycleThroughPlayers).getName());
//        }

        main = (YahtzeeActivity) getActivity();
        numPlayers = main.numberOfTotalPlayers;
        thePlayers = new ArrayList<>();
        die1 = rootView.findViewById(R.id.die1);
        die2 = rootView.findViewById(R.id.die2);
        die3 = rootView.findViewById(R.id.die3);
        die4 = rootView.findViewById(R.id.die4);
        die5 = rootView.findViewById(R.id.die5);
        crossOutConfirm = rootView.findViewById(R.id.crossOutConfirm);
        crossOutConfirm.setVisibility(View.INVISIBLE);
        playerName = rootView.findViewById(R.id.playerName);
        reroll = rootView.findViewById(R.id.reroll);
        finishTurn = rootView.findViewById(R.id.putInScore);
        pickCategory = rootView.findViewById(R.id.pickCategory);
        pickCategory.setBackgroundColor(res.getColor(android.R.color.white));
        reroll.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
        die1.setBackgroundColor(res.getColor(android.R.color.white));
        die2.setBackgroundColor(res.getColor(android.R.color.white));
        die3.setBackgroundColor(res.getColor(android.R.color.white));
        die4.setBackgroundColor(res.getColor(android.R.color.white));
        die5.setBackgroundColor(res.getColor(android.R.color.white));


        //initializing list of players and setting their names
        for(int i = 0; i < numPlayers; i++){
            thePlayers.add(i, new Player());
            thePlayers.get(i).setName(main.playerNames.get(i));
        }
        playerName.setText(thePlayers.get(0).getName());
        //////////////////////////////////////////////////////
        InitializeDie();
        //setting all of the onClickListeners for the die/////
        die1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select1 == 0){
                    die1.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
                    select1++;
                }
                else{
                    die1.setBackgroundColor(res.getColor(android.R.color.white));
                    select1--;
                }

            }
        });
        die2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select2 == 0){
                    die2.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
                    select2++;
                }
                else{
                    die2.setBackgroundColor(res.getColor(android.R.color.white));
                    select2--;
                }
            }
        });
        die3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select3 == 0){
                    die3.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
                    select3++;
                }
                else{
                    die3.setBackgroundColor(res.getColor(android.R.color.white));
                    select3--;
                }
            }
        });
        die4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select4 == 0){
                    die4.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
                    select4++;
                }
                else{
                    die4.setBackgroundColor(res.getColor(android.R.color.white));
                    select4--;
                }
            }
        });
        die5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select5 == 0){
                    die5.setBackgroundColor(res.getColor(android.R.color.holo_blue_dark));
                    select5++;
                }
                else{
                    die5.setBackgroundColor(res.getColor(android.R.color.white));
                    select5--;
                }
            }
        });
        crossOutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int crossOutCategory = pickCategory.getSelectedItemPosition();
                if(crossOutCategory == 0){
                    Toast.makeText(getContext(), "Pick a valid category to cross out!", Toast.LENGTH_LONG).show();
                }
                else{
                    if(thePlayers.get(cycleThroughPlayers).crossOutOption(crossOutCategory-1)){
                        Toast.makeText(getContext(), "Crossed out successfully!", Toast.LENGTH_LONG).show();
                        crossOutConfirm.setVisibility(View.INVISIBLE);
                        crossOutConfirm.setClickable(false);
                        supposedToCrossOut = false;
                        NextTurn();
                    }
                    else{
                        Toast.makeText(getContext(), "Pick a valid category to cross out!", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        reroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canReroll == 0){
                    Toast.makeText(getContext(), "No more rerolls available. Please choose " +
                            "a category and finish your turn.", Toast.LENGTH_LONG).show();
                }
                else{
                    Reroll();
                }
            }
        });
        finishTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedCategory;

                if(supposedToCrossOut){
                    Toast.makeText(getContext(), "Select a category to cross out and then click confirm!", Toast.LENGTH_LONG).show();
                }
                else if(pickCategory.getSelectedItemPosition() == 0){
                    Toast.makeText(getContext(), "Select a category to score!", Toast.LENGTH_LONG).show();
                }
                else{
                    selectedCategory = pickCategory.getSelectedItemPosition();
                    switch (selectedCategory){
                        case 1:
                            if(!thePlayers.get(cycleThroughPlayers).isOnes()){
                                Toast.makeText(getContext(), "Ones category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Ones category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 2:
                            if(!thePlayers.get(cycleThroughPlayers).isTwos()){
                                Toast.makeText(getContext(), "Twos category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Twos category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 3:
                            if(!thePlayers.get(cycleThroughPlayers).isThrees()){
                                Toast.makeText(getContext(), "Threes category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Threes category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 4:
                            if(!thePlayers.get(cycleThroughPlayers).isFours()){
                                Toast.makeText(getContext(), "Fours category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Fours category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 5:
                            if(!thePlayers.get(cycleThroughPlayers).isFives()){
                                Toast.makeText(getContext(), "Fives category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Fives category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 6:
                            if(!thePlayers.get(cycleThroughPlayers).isSixes()){
                                Toast.makeText(getContext(), "Sixes category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Sixes category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 7:
                            if(!thePlayers.get(cycleThroughPlayers).isThreeKind()){
                                Toast.makeText(getContext(), "Three of a kind category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Three of a Kind category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 8:
                            if(!thePlayers.get(cycleThroughPlayers).isFourKind()){
                                Toast.makeText(getContext(), "Four of a kind category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Four of a Kind category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 9:
                            if(!thePlayers.get(cycleThroughPlayers).isFullHouse()){
                                Toast.makeText(getContext(), "Full house category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Full House category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 10:
                            if(!thePlayers.get(cycleThroughPlayers).isSmallStraight()){
                                Toast.makeText(getContext(), "Small straight category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Small Straight category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 11:
                            if(!thePlayers.get(cycleThroughPlayers).isLargeStraight()){
                                Toast.makeText(getContext(), "Large straight category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Large Straight category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 12:
                            if(!thePlayers.get(cycleThroughPlayers).isYahtzee()){
                                Toast.makeText(getContext(), "Yahtzee category either already filled in or you have invalid die!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "CONGRATULATIONS! You got a Yahtzee!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 13:
                            if(!thePlayers.get(cycleThroughPlayers).isChance()){
                                Toast.makeText(getContext(), "Chance category is already filled in!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Filled in your Chance category!", Toast.LENGTH_LONG).show();
                                NextTurn();
                            }
                            break;
                        case 14:
                            crossOutConfirm.setVisibility(View.VISIBLE);
                            crossOutConfirm.setClickable(true);
                            supposedToCrossOut = true;
                            break;
                    }
                }
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////

        return rootView;
    }

    //initializes die and updates images at beginning of everyone's turn
    public void InitializeDie(){
        thePlayers.get(cycleThroughPlayers).InitializeDie();

        int resID;
        String nameOfDieImage;

        nameOfDieImage = "dice" + thePlayers.get(cycleThroughPlayers).getDie()[0];
        resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
        die1.setImageResource(resID);

        nameOfDieImage = "dice" + thePlayers.get(cycleThroughPlayers).getDie()[1];
        resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
        die2.setImageResource(resID);

        nameOfDieImage = "dice" + thePlayers.get(cycleThroughPlayers).getDie()[2];
        resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
        die3.setImageResource(resID);

        nameOfDieImage = "dice" + thePlayers.get(cycleThroughPlayers).getDie()[3];
        resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
        die4.setImageResource(resID);

        nameOfDieImage = "dice" + thePlayers.get(cycleThroughPlayers).getDie()[4];
        resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
        die5.setImageResource(resID);
    }

    public void NextTurn(){
        die1.setBackgroundColor(res.getColor(android.R.color.white));
        die2.setBackgroundColor(res.getColor(android.R.color.white));
        die3.setBackgroundColor(res.getColor(android.R.color.white));
        die4.setBackgroundColor(res.getColor(android.R.color.white));
        die5.setBackgroundColor(res.getColor(android.R.color.white));
        select1 = 0;
        select2 = 0;
        select3 = 0;
        select4 = 0;
        select5 = 0;
        //gives next person 2 chances to reroll
        canReroll = 2;
        boolean gameDone = false;
        //goes to next player by updating this value
        if(numberOfRoundsElapsed == 12){
            //finish game
            DeclareWinner();
            gameDone = true;
        }

        else if(cycleThroughPlayers == numPlayers-1){
            cycleThroughPlayers = 0;
            numberOfRoundsElapsed++;
        }
        else{
            cycleThroughPlayers++;
        }

        //countdowntimer so we can wait until the last Toast finishes before displaying thew next one
        if(!gameDone){
            new CountDownTimer(1500,1000) {
                @Override
                public void onTick(long arg0) {}

                @Override
                public void onFinish() {
                    Toast.makeText(getContext(), thePlayers.get(cycleThroughPlayers).getName() + "'s turn!", Toast.LENGTH_LONG).show();
                }
            }.start();

            goToNextTurn = false;
            pickCategory.setSelection(0);


            InitializeDie();
            playerName.setText(thePlayers.get(cycleThroughPlayers).getName());
        }
    }

    public void Reroll(){
        int resID;
        String nameOfDieImage;
        int num = 0;


        if(select1 == 1){
            thePlayers.get(cycleThroughPlayers).setOneDie();
            num = thePlayers.get(cycleThroughPlayers).getOneDie();
            nameOfDieImage = "dice" + num;
            resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
            die1.setImageResource(resID);
            die1.setBackgroundColor(res.getColor(android.R.color.white));
            select1--;
        }
        if(select2 == 1){
            thePlayers.get(cycleThroughPlayers).setTwoDie();
            num = thePlayers.get(cycleThroughPlayers).getTwoDie();
            nameOfDieImage = "dice" + num;
            resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
            die2.setImageResource(resID);
            die2.setBackgroundColor(res.getColor(android.R.color.white));
            select2--;
        }
        if(select3 == 1){
            thePlayers.get(cycleThroughPlayers).setThreeDie();
            num = thePlayers.get(cycleThroughPlayers).getThreeDie();
            nameOfDieImage = "dice" + num;
            resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
            die3.setImageResource(resID);
            die3.setBackgroundColor(res.getColor(android.R.color.white));
            select3--;
        }
        if(select4 == 1){
            thePlayers.get(cycleThroughPlayers).setFourDie();
            num = thePlayers.get(cycleThroughPlayers).getFourDie();
            nameOfDieImage = "dice" + num;
            resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
            die4.setImageResource(resID);
            die4.setBackgroundColor(res.getColor(android.R.color.white));
            select4--;
        }
        if(select5 == 1){
            thePlayers.get(cycleThroughPlayers).setFiveDie();
            num = thePlayers.get(cycleThroughPlayers).getFiveDie();
            nameOfDieImage = "dice" + num;
            resID = res.getIdentifier(nameOfDieImage , "drawable", getContext().getPackageName());
            die5.setImageResource(resID);
            die5.setBackgroundColor(res.getColor(android.R.color.white));
            select5--;
        }
        canReroll--;

    }

    public void DeclareWinner(){
        Player sortTemp;

        for(int i = 0; i < numPlayers; i++){
            thePlayers.get(i).TotalUp();
        }

        //sorts die from smallest to largest
        for(int p = 0; p < numPlayers; p++)
        {
            for(int i = 0; i < numPlayers; i++)
            {
                if(thePlayers.get(i).ReturnTotal() > thePlayers.get(i).ReturnTotal())
                {
                    sortTemp = thePlayers.get(i);
                    thePlayers.set(i, thePlayers.get(i+1));
                    thePlayers.set(i+1, sortTemp);
                }
            }
        }

        Toast.makeText(getContext(), "The winner is " + thePlayers.get(numPlayers-1).getName()
                + " with "
        + thePlayers.get(numPlayers-1).ReturnTotal(), Toast.LENGTH_LONG).show();
    }
}
