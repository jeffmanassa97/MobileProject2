package edu.fsu.cs.mobileproject2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlapActivity extends AppCompatActivity {

    ImageButton slapDeck;
    ImageButton opponentDeck;
    ImageButton yourDeck;
    List<Card> opponentPrizeDeck;
    List<Card> yourPrizeDeck;
    List<Card> yourDealDeck;
    List<Card> opponentsDealDeck;
    List<Card> shuffleDeck;
    List<Card> middleDeck;
    Random rand;
    Card bottomCard;
    TextView numYourPrizeCards;
    TextView numOppenentsPrizeCards;
    int numOfPulls = 0;
    boolean faceCard = false;
    GestureDetector detector;
    int totalTurnsToWin = 0;

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())) return false;


            if (e1.getY() - e2.getY() > 120 && Math.abs(velocityY) > 200) {
                //Toast.makeText(getApplicationContext(), "You swiped up!", Toast.LENGTH_SHORT).show();
                if(middleDeck.size() <= 1){
                    Toast.makeText(SlapActivity.this, "Not enough cards.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(CheckRules()){
                        //add cards to their pile first!!!!!!
                        opponentPrizeDeck.addAll(middleDeck);
                        numOppenentsPrizeCards.setText(opponentPrizeDeck.size() + "/52");
                        middleDeck.clear();
                        slapDeck.setImageDrawable(getDrawable(R.drawable.grayback));
                        CheckForVictory();
                    }
                    else{
                        if(opponentsDealDeck.size() >= 2){
                            middleDeck.add(0,opponentsDealDeck.get(opponentsDealDeck.size()-1));
                            middleDeck.add(0,opponentsDealDeck.get(opponentsDealDeck.size()-2));
                            opponentsDealDeck.remove(opponentsDealDeck.size()-1);
                            opponentsDealDeck.remove(opponentsDealDeck.size()-1);
                        }
                        else{
                            middleDeck.addAll(opponentsDealDeck);
                            opponentsDealDeck.clear();
                        }
                    }
                }
            } else if (e2.getY() - e1.getY() > 120 && Math.abs(velocityY) > 200) {
                if(middleDeck.size() <= 1){
                    Toast.makeText(SlapActivity.this, "Not enough cards.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(CheckRules()){
                        //add cards to their pile first!!!!!!
                        yourPrizeDeck.addAll(middleDeck);
                        numYourPrizeCards.setText(yourPrizeDeck.size() + "/52");
                        middleDeck.clear();
                        slapDeck.setImageDrawable(getDrawable(R.drawable.grayback));
                        CheckForVictory();
                    }
                    else{
                        if(opponentsDealDeck.size() >= 2){
                            middleDeck.add(0,yourDealDeck.get(yourDealDeck.size()-1));
                            middleDeck.add(0,yourDealDeck.get(yourDealDeck.size()-2));
                            yourDealDeck.remove(yourDealDeck.size()-1);
                            yourDealDeck.remove(yourDealDeck.size()-1);
                        }
                        else{
                            middleDeck.addAll(yourDealDeck);
                            yourDealDeck.clear();
                        }
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slap_activity);

        RelativeLayout theLayout = findViewById(R.id.relativeLayout);
        theLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

        //initializing detector to detect whether we swiped up or down for each player
        detector = new GestureDetector(this, new MyGestureDetector());
        /////////////////////////////////////////////////////////////////////////////

        //initializing the seed for random number generation for shuffling the deck
        rand = new Random();
        /////////////////////////////

        //initializing image buttons and decks///////////
        middleDeck = new ArrayList<Card>();
        slapDeck = findViewById(R.id.slapdeck);
        slapDeck.setClickable(false);
        opponentDeck = findViewById(R.id.opponentdeck);
        yourDeck = findViewById(R.id.yourdeck);
        yourDealDeck = new ArrayList<>();
        opponentsDealDeck = new ArrayList<>();
        opponentPrizeDeck = new ArrayList<>();
        yourPrizeDeck = new ArrayList<>();
        shuffleDeck = new ArrayList<>();
        numYourPrizeCards = findViewById(R.id.numPrizeCards);
        numOppenentsPrizeCards = findViewById(R.id.numPrizeCards2);
        /////////////////////////////////////////////////



        //initializing, shuffling deck, then dealing it to players
        PassOutCards();


        opponentDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalTurnsToWin++;

                if(opponentsDealDeck.size() == 0){
                    yourDeck.setClickable(true);
                    opponentDeck.setClickable(false);
                }

                else if(!faceCard){
                    yourDeck.setClickable(true);
                    opponentDeck.setClickable(false);

                    if(opponentsDealDeck.size() > 0)
                        PutCardInMiddle(opponentsDealDeck);
                    if(middleDeck.get(middleDeck.size()-1).getNumber() > 10 && opponentsDealDeck.size() > 0){
                        faceCard = true;
                        numOfPulls = middleDeck.get(middleDeck.size()-1).getNumber() - 10;
                    }
                }
                else{
                    if(opponentsDealDeck.size() > 0) {
                        PutCardInMiddle(opponentsDealDeck);
                        --numOfPulls;

                        if(middleDeck.get(middleDeck.size()-1).getNumber() > 10){
                            faceCard = true;
                            numOfPulls = middleDeck.get(middleDeck.size()-1).getNumber()-10;
                            opponentDeck.setClickable(false);
                            yourDeck.setClickable(true);
                        }

                        else if(numOfPulls == 0){
                            //if they pulled all necessary cards and didn't pull a face, then
                            //opponent gets all of the cards in the middle for their prize deck
                            if(middleDeck.get(middleDeck.size()-1).getNumber() < 11){
                                GetPrizeCardsFromMiddle(1);
                            }
                            numOfPulls = 0;
                            faceCard = false;
                            opponentDeck.setClickable(false);
                            yourDeck.setClickable(true);
                        }
                    }
                    else{
                        GetPrizeCardsFromMiddle(1);
                        numOfPulls = 0;
                        faceCard = false;
                        opponentDeck.setClickable(false);
                        yourDeck.setClickable(true);
                    }
                }

                if(opponentsDealDeck.size() == 0 && yourDealDeck.size() == 0) {
                    yourPrizeDeck.clear();
                    opponentPrizeDeck.clear();
                    middleDeck.clear();
                    PassOutCards();
                    numYourPrizeCards.setText("0/52");
                    numOppenentsPrizeCards.setText("0/52");
                }

            }
        });

        yourDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalTurnsToWin++;
                if(yourDealDeck.size() == 0){
                    yourDeck.setClickable(false);
                    opponentDeck.setClickable(true);
                }

                else if(!faceCard){
                    if(opponentsDealDeck.size() > 0){
                        opponentDeck.setClickable(true);
                        yourDeck.setClickable(false);
                    }
                    if(yourDealDeck.size() > 0)
                        PutCardInMiddle(yourDealDeck);
                    if(middleDeck.get(middleDeck.size()-1).getNumber() > 10 && opponentsDealDeck.size() > 0){
                        faceCard = true;
                        numOfPulls = middleDeck.get(middleDeck.size()-1).getNumber() - 10;
                    }
                }
                else{
                    //if the opponent played a face card, do everything below

                    if(yourDealDeck.size() > 0){
                        PutCardInMiddle(yourDealDeck);
                        --numOfPulls;

                        if(middleDeck.get(middleDeck.size()-1).getNumber() > 10){
                            faceCard = true;
                            numOfPulls = middleDeck.get(middleDeck.size()-1).getNumber()-10;
                            opponentDeck.setClickable(true);
                            yourDeck.setClickable(false);
                        }
                        else if(numOfPulls == 0){
                            //if they pulled all necessary cards and didn't pull a face, then
                            //opponent gets all of the cards in the middle for their prize deck
                            if(middleDeck.get(middleDeck.size()-1).getNumber() < 11){
                                GetPrizeCardsFromMiddle(2);
                            }
                            numOfPulls = 0;
                            faceCard = false;
                            opponentDeck.setClickable(true);
                            yourDeck.setClickable(false);
                        }
                    }
                    else{
                        GetPrizeCardsFromMiddle(2);
                        numOfPulls = 0;
                        faceCard = false;
                        opponentDeck.setClickable(true);
                        yourDeck.setClickable(false);
                    }
                }
                if(opponentsDealDeck.size() == 0 && yourDealDeck.size() == 0){
                    yourPrizeDeck.clear();
                    opponentPrizeDeck.clear();
                    middleDeck.clear();
                    PassOutCards();
                    numYourPrizeCards.setText("0/52");
                    numOppenentsPrizeCards.setText("0/52");
                }


            }
        });
        ////////////////////////////////////////////////////////////


    }

    public void InitializeDeck(List<Card> theDeck){
        String suit = "o";
        theDeck.clear();

        for (int i = 0; i < 52; i++)	// for each card in the deck:
        {
            switch (i / 13)	 			// and a suit.
            {
                case 0:   suit = "c";	 break;
                case 1:   suit = "d";	break;
                case 2:   suit = "h";	break;
                case 3:   suit = "s";	break;
            }
            theDeck.add(i, new Card((i%13+1), suit));		//assign it a numeric value (1 - 13)
        }
    }

    public void ShuffleDeck(List<Card> theDeck){
        for (int i = 0; i < 3; i++)		//Shuffle 3 times.
        {
            for (int j = 0; j < 52; j++)    //Rearrange each card (0 - 51).
            {
                int r = rand.nextInt(52);   //Pick a location to swap j-th card with
                Card c = theDeck.get(j);
                theDeck.set(j, theDeck.get(r));
                theDeck.set(r, c);
            }
        }
    }

    public void PutCardInMiddle(List<Card> theirDeck){
        Card theirTopCard = theirDeck.get(theirDeck.size()-1);

        if(middleDeck.size() == 0){
            bottomCard = theirTopCard;
        }

        Log.e("Card = ", theirTopCard.getSuit() + theirTopCard.getNumber());

        if(theirTopCard.getNumber() == 1){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.ah));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.as));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.ad));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.ac));
                middleDeck.add(theirTopCard);
            }
        }
        else if(theirTopCard.getNumber() == 2){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.twoh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.twos));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.twod));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.twoc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 3){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.threeh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.threes));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.threed));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.threec));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 4){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.fourh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.fours));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.fourd));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.fourc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 5){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.fiveh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.fives));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.fived));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.fivec));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 6){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.sixh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.sixs));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.sixd));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.sixc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 7){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.sevenh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.sevens));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.sevend));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.sevenc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 8){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.eighth));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.eights));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.eightd));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.eightc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 9){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.nineh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.nines));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.nined));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.ninec));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 10){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.tenh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.tens));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.tend));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.tenc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 11){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.jh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.js));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.jd));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.jc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 12){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.qh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.qs));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.qd));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.qc));
                middleDeck.add(theirTopCard);
            }

        }
        else if(theirTopCard.getNumber() == 13){
            if(theirTopCard.getSuit().equals("h")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.kh));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("s")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.ks));
                middleDeck.add(theirTopCard);
            }
            else if(theirTopCard.getSuit().equals("d")){
                slapDeck.setImageDrawable(getDrawable(R.drawable.kd));
                middleDeck.add(theirTopCard);
            }
            else{
                slapDeck.setImageDrawable(getDrawable(R.drawable.kc));
                middleDeck.add(theirTopCard);
            }
        }
        theirDeck.remove(theirDeck.size()-1);
    }

    //check rules when they hit the middle deck to see if they get the cards/points
    public boolean CheckRules(){
        //checking to see if double numbers were put down (ex. 5 then 5)
        Log.e("We are here: ", "  Pls work");
        Log.e("Comparing", "Num: " + middleDeck.get(middleDeck.size()-1).getNumber() + middleDeck.get(middleDeck.size()-3).getNumber());
        if(middleDeck.get(middleDeck.size()-1).getNumber() ==
                middleDeck.get(middleDeck.size()-2).getNumber()){
            numOfPulls = 0;
            faceCard = false;
            return true;
        }
        //if there is a "sandwich" (ex. 5 then 3 then 5)
        else if(middleDeck.size() >= 3 && middleDeck.get(middleDeck.size()-1).getNumber() == middleDeck.get(middleDeck.size()-3).getNumber()){
            numOfPulls = 0;
            faceCard = false;
            Log.e("WE ARE HERE! ", "YAY BUT WORK!");
            return true;
        }
        //if there is a queen and king card next to each other
        else if((middleDeck.get(middleDeck.size()-1).getNumber() == 13 && middleDeck.get(middleDeck.size()-2).getNumber() == 12)
                || (middleDeck.get(middleDeck.size()-1).getNumber() == 12 && middleDeck.get(middleDeck.size()-2).getNumber() == 13)){
            numOfPulls = 0;
            faceCard = false;
            return true;
        }
        //if number of top card = number of bottom card
        else if(middleDeck.get(middleDeck.size()-1).getNumber() == bottomCard.getNumber()){
            numOfPulls = 0;
            faceCard = false;
            return true;
        }
        //checking for 4 in a row ascending (ex. 9-10-jack-queen)
        else if(middleDeck.size() >= 4 && (middleDeck.get(middleDeck.size()-1).getNumber() ==
                middleDeck.get(middleDeck.size()-2).getNumber() + 1) &&
                (middleDeck.get(middleDeck.size()-1).getNumber() ==
                        middleDeck.get(middleDeck.size()-3).getNumber() + 2) &&
                (middleDeck.get(middleDeck.size()-1).getNumber() ==
                        middleDeck.get(middleDeck.size()-4).getNumber() + 3)){
            numOfPulls = 0;
            faceCard = false;
            return true;
        }
        //checking for 4 in a row descending (ex. queen-jack-10-9)
        else if(middleDeck.size() >= 4 && (middleDeck.get(middleDeck.size()-1).getNumber() ==
                middleDeck.get(middleDeck.size()-2).getNumber() - 1) &&
                (middleDeck.get(middleDeck.size()-1).getNumber() ==
                        middleDeck.get(middleDeck.size()-3).getNumber() -2) &&
                (middleDeck.get(middleDeck.size()-1).getNumber() ==
                        middleDeck.get(middleDeck.size()-4).getNumber() - 3)){
            numOfPulls = 0;
            faceCard = false;
            return true;
        }
        //2 consecutive cards = 10 when added
        else if(middleDeck.get(middleDeck.size()-1).getNumber() + middleDeck.get(middleDeck.size()-2).getNumber() == 10){
            numOfPulls = 0;
            faceCard = false;
            return true;
        }


        return false;
    }

    public void GetPrizeCardsFromMiddle(int player){
        if(player == 1){
            yourPrizeDeck.addAll(middleDeck);
            numYourPrizeCards.setText(yourPrizeDeck.size() + "/52");
            middleDeck.clear();
            slapDeck.setImageDrawable(getDrawable(R.drawable.grayback));
        }
        else{
            opponentPrizeDeck.addAll(middleDeck);
            numOppenentsPrizeCards.setText(opponentPrizeDeck.size() + "/52");
            //numYourPrizeCards.setText(yourPrizeDeck.size() + "/52");
            middleDeck.clear();
            slapDeck.setImageDrawable(getDrawable(R.drawable.grayback));
        }

        if(yourPrizeDeck.size() == 52){
            Toast.makeText(SlapActivity.this, "CONGRATULATIONS TO PLAYER 1! YOU WIN!", Toast.LENGTH_LONG).show();
            slapDeck.setClickable(false);
            yourDeck.setClickable(false);
            opponentDeck.setClickable(false);
        }
        else if(opponentPrizeDeck.size() == 52){
            Toast.makeText(SlapActivity.this, "CONGRATULATIONS TO PLAYER 2! YOU WIN!", Toast.LENGTH_LONG).show();
            slapDeck.setClickable(false);
            yourDeck.setClickable(false);
            opponentDeck.setClickable(false);
        }
    }

    public void PassOutCards(){
        InitializeDeck(shuffleDeck);
        ShuffleDeck(shuffleDeck);
        for(int i = 0; i < 26; i++){
            yourDealDeck.add(i, shuffleDeck.get(i));
        }
        for(int i = 0; i < 26; i++){
            opponentsDealDeck.add(i, shuffleDeck.get(i+26));
        }
    }

@Override public boolean onTouchEvent(MotionEvent event) {
    if (detector.onTouchEvent(event))
        return true;
    else
        return false;
}

public void CheckForVictory(){
        if(yourPrizeDeck.size() == 52){
            Toast.makeText(this, "Congratulations! Player 1 wins!", Toast.LENGTH_LONG).show();
        }
        else if(opponentPrizeDeck.size() == 52){
            Toast.makeText(this, "Congratulations! Player 2 wins!", Toast.LENGTH_LONG).show();
        }
}


}


