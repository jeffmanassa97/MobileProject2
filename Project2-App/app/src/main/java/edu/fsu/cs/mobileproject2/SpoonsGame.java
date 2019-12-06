package edu.fsu.cs.mobileproject2;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class SpoonsGame extends AppCompatActivity {

    private static final String DEBUG_TAG = "Gesture";
    private GestureDetector mDetect;

    /* ----- NOTE -----
    ACES ARE HIGH, 14 stands for ACE
     */
    String[] Faces = {"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    String[] Suit = {"Heart", "Club", "Diamond", "Spade"};

    String testCard = new String();

    String[] inHand = new String[4];
    String[] op1Hand = new String[4];
    int[] op1HandFaces = new int[4];
    String[] op2Hand = new String[4];
    int[] op2HandFaces = new int[4];
    String[] op3Hand = new String[4];
    int[] op3HandFaces = new int[4];

    String nextCard = new String();

    String[] alreadyUsed = new String[52];

    boolean won = false;
    boolean otherWon = false;
    int cardsDealt = 0;
    int placeInUsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoons_game);

        /*-----  POPULATE ALL CARDS  -----*/
        for (int i = 0; i <= 16; i++) {
            //go through everyone's hand and deal 4 cards each
            String card = giveCard();
            if (i < 4) {
                //take 4 cards and note what ones you have as to be sure not to duplicate any cards
                inHand[i] = card;
                cardsDealt++;
            } else if (i >= 4 && i <= 7) {
                //populate opponent 1's hand
                op1Hand[i - 4] = card;
                cardsDealt++;
            } else if (i >= 8 && i <= 11) {
                //populate opponent 2's hand
                op2Hand[i - 8] = card;
                cardsDealt++;
            } else if (i >= 12 && i <= 15) {
                //populate opponent 3's hand
                op3Hand[i - 12] = card;
                cardsDealt++;
            }
        }

        /*-----  SHOW USERS CARDS  -----*/
        showCard1();
        showCard2();
        showCard3();
        showCard4();

        //deal the card that is offered to be the next card for the user
        nextCard = giveCard();
        showNextOption(nextCard);

        /*pull the faces, since that is really all the we are going to check for on the backend for
          a winning condition since we assume that there are not going to be any repeating cards in
          the hand (we handle this with alreadyUsed array)
         */
        op1HandFaces = theFaces(op1Hand);
        op2HandFaces = theFaces(op2Hand);
        op3HandFaces = theFaces(op3Hand);

        final Button spoon1 = (Button)findViewById(R.id.spoon1);
        final Button spoon2 = (Button)findViewById(R.id.spoon2);
        final Button spoon3 = (Button)findViewById(R.id.spoon3);

        RadioGroup PlayersHand = (RadioGroup)findViewById(R.id.playersHand);

        RadioButton CardOne = (RadioButton)findViewById(R.id.hand1);
        RadioButton CardTwo = (RadioButton)findViewById(R.id.hand2);
        RadioButton CardThree = (RadioButton)findViewById(R.id.hand3);
        RadioButton CardFour = (RadioButton)findViewById(R.id.hand4);

        RadioButton pickUp = (RadioButton)findViewById(R.id.radioButton13);

        spoon1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkPlayerWin();
                if(won == false && otherWon == false){
                    Toast.makeText(getApplicationContext(), "Opps, you grabbed a spoon too soon!", Toast.LENGTH_LONG).show();
                }else if(won == true && otherWon == false){
                    spoon1.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "YAY! YOU WON!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spoon2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkPlayerWin();
                if(won == false && otherWon == false){
                    Toast.makeText(getApplicationContext(), "Opps, you grabbed a spoon too soon!", Toast.LENGTH_LONG).show();
                }else if(won == true && otherWon == false){
                    spoon2.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "YAY! YOU WON!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spoon3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkPlayerWin();
                if(won == false && otherWon == false){
                    Toast.makeText(getApplicationContext(), "Opps, you grabbed a spoon too soon!", Toast.LENGTH_LONG).show();
                }else if(won == true && otherWon == false){
                    spoon3.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "YAY! YOU WON!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        CardOne.setOnClickListener(new View.OnClickListener(){
            //used clicked the first card to get rid of it and replace with optional card,
            //replace card One in hand with optional card, and get a new opt card
            @Override
            public void onClick(View v){
                //replace card in slot one with optional card
                inHand[0] = nextCard;
                showCard1();

                //generate new optional card
                String card = giveCard();
                showNextOption(card);
                nextCard = card;
            }
        });

        CardTwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //replace card in slot two with optional card
                inHand[1] = nextCard;
                showCard2();

                //generate new optional card
                String card = giveCard();
                showNextOption(card);
                nextCard = card;
            }
        });

        CardThree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //replace card in slot one with optional card
                inHand[2] = nextCard;
                showCard3();

                //generate new optional card
                String card = giveCard();
                showNextOption(card);
                nextCard = card;
            }
        });

        CardFour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //replace card in slot one with optional card
                inHand[3] = nextCard;
                showCard4();

                //generate new optional card
                String card = giveCard();
                showNextOption(card);
                nextCard = card;
            }
        });

        pickUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //player decided to remove the card that was given to them, so they get a new one
                //but keep all of their other cards
                String card = giveCard();
                showNextOption(card);
                nextCard = card;
            }
        });
     }

/* -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- END OF onCreate METHOD -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- */

    public static int getRandomIndex(int length){
        //helps with dealCard
        return (int)(Math.random()*((length)));
    }

    public String dealCard(){
        //gives a random card
        int FaceIndex = getRandomIndex(Faces.length);
        String face = Faces[FaceIndex];
        int SuitIndex = getRandomIndex(Suit.length);
        String suit = Suit[SuitIndex];
        String card = face + suit;
        testCard = card;
        return card;
    }

    public String giveCard(){
        //should check if card has been used
        String newCard = dealCard();
        boolean alreadyUsedBool = false;
        boolean usedContains;

        if(usedContains = Arrays.asList(alreadyUsed).contains(newCard)){
            alreadyUsedBool = true;
        }
        while(alreadyUsedBool == true) {
            newCard = dealCard();
            if(usedContains = Arrays.asList(alreadyUsed).contains(newCard)){
                alreadyUsedBool = true;
            }else{
                alreadyUsedBool = false;
            }
        }

        usedContains = Arrays.asList(alreadyUsed).contains(newCard);

        //hasnt been used, good to use place into the used array
        if(placeInUsed < 51) {
            //be sure there is room, and the entire deck hasn't been used
            alreadyUsed[placeInUsed] = newCard;
            placeInUsed++;
        }
        else if(placeInUsed == 51){
            placeInUsed = 0;
            Arrays.fill(alreadyUsed,null);
            for(int i = 0; i < 4; i++) {
                alreadyUsed[placeInUsed] = inHand[i];
                placeInUsed++;
                alreadyUsed[placeInUsed] = op1Hand[i];
                placeInUsed++;
                alreadyUsed[placeInUsed] = op2Hand[i];
                placeInUsed++;
                alreadyUsed[placeInUsed] = op3Hand[i];
                placeInUsed++;
            }
            alreadyUsed[placeInUsed] = nextCard;
            placeInUsed++;
        }
        return newCard;
    }

    public int[] theFaces(String[] cards){
        int[] sendBack = new int[4];
        for (int i = 0; i < 4; i++){
            if(cards[i].contains("Ace")){
                sendBack[i] = 14;
            }
            else if(cards[i].contains("King")){
                sendBack[i] = 13;
            }
            else if(cards[i].contains("Queen")){
                sendBack[i] = 12;
            }
            else if(cards[i].contains("Jack")){
                sendBack[i] = 11;
            }
            else if(cards[i].contains("10")){
                sendBack[i] = 10;
            }
            else if(cards[i].contains("9")){
                sendBack[i] = 9;
            }
            else if(cards[i].contains("8")){
                sendBack[i] = 8;
            }
            else if(cards[i].contains("7")){
                sendBack[i] = 7;
            }
            else if(cards[i].contains("6")){
                sendBack[i] = 6;
            }
            else if(cards[i].contains("5")){
                sendBack[i] = 5;
            }
            else if(cards[i].contains("4")){
                sendBack[i] = 4;
            }
            else if(cards[i].contains("3")){
                sendBack[i] = 3;
            }
            else if(cards[i].contains("2")){
                sendBack[i] = 2;
            }
        }
        return sendBack;
    }

    /* These functions help to show the cards that are being displayed to the user, may not be the
        most efficient way, but it was the best way I could come up with
     */
    public void showCard1(){
        ImageView card1SuitClub = (ImageView)findViewById(R.id.card1club);
        ImageView card1SuitSpade = (ImageView)findViewById(R.id.card1spade);
        ImageView card1SuitHeart = (ImageView)findViewById(R.id.card1heart);
        ImageView card1SuitDiamond = (ImageView)findViewById(R.id.card1diamond);

        card1SuitClub.setVisibility(View.INVISIBLE);
        card1SuitSpade.setVisibility(View.INVISIBLE);
        card1SuitHeart.setVisibility(View.INVISIBLE);
        card1SuitDiamond.setVisibility(View.INVISIBLE);

        TextView card1Face = (TextView)findViewById(R.id.card1facevalue);

        if(inHand[0].contains("Club")){
            card1SuitClub.setVisibility(View.VISIBLE);
        }else if(inHand[0].contains("Diamond")){
            card1SuitDiamond.setVisibility(View.VISIBLE);
        }else if(inHand[0].contains("Heart")){
            card1SuitHeart.setVisibility(View.VISIBLE);
        }else if(inHand[0].contains("Spade")){
            card1SuitSpade.setVisibility(View.VISIBLE);
        }

        if(inHand[0].contains("Ace")){
            card1Face.setText("A");
        }else if(inHand[0].contains("King")){
            card1Face.setText("K");
        }else if(inHand[0].contains("Queen")){
            card1Face.setText("Q");
        }else if(inHand[0].contains("Jack")){
            card1Face.setText("J");
        }else if(inHand[0].contains("10")){
            card1Face.setText("10");
        }else if(inHand[0].contains("9")){
            card1Face.setText("9");
        }else if(inHand[0].contains("8")){
            card1Face.setText("8");
        }else if(inHand[0].contains("7")){
            card1Face.setText("7");
        }else if(inHand[0].contains("6")){
            card1Face.setText("6");
        }else if(inHand[0].contains("5")){
            card1Face.setText("5");
        }else if(inHand[0].contains("4")){
            card1Face.setText("4");
        }else if(inHand[0].contains("3")){
            card1Face.setText("3");
        }else if(inHand[0].contains("2")){
            card1Face.setText("2");
        }
    }

    public void showCard2(){
        ImageView card2SuitClub = (ImageView)findViewById(R.id.card2club);
        ImageView card2SuitSpade = (ImageView)findViewById(R.id.card2spade);
        ImageView card2SuitHeart = (ImageView)findViewById(R.id.card2heart);
        ImageView card2SuitDiamond = (ImageView)findViewById(R.id.card2diamond);

        card2SuitClub.setVisibility(View.INVISIBLE);
        card2SuitSpade.setVisibility(View.INVISIBLE);
        card2SuitHeart.setVisibility(View.INVISIBLE);
        card2SuitDiamond.setVisibility(View.INVISIBLE);

        TextView card2Face = (TextView)findViewById(R.id.card2facevalue);

        if(inHand[1].contains("Club")){
            card2SuitClub.setVisibility(View.VISIBLE);
        }else if(inHand[1].contains("Diamond")){
            card2SuitDiamond.setVisibility(View.VISIBLE);
        }else if(inHand[1].contains("Heart")){
            card2SuitHeart.setVisibility(View.VISIBLE);
        }else if(inHand[1].contains("Spade")){
            card2SuitSpade.setVisibility(View.VISIBLE);
        }

        if(inHand[1].contains("Ace")){
            card2Face.setText("A");
        }else if(inHand[1].contains("King")){
            card2Face.setText("K");
        }else if(inHand[1].contains("Queen")){
            card2Face.setText("Q");
        }else if(inHand[1].contains("Jack")){
            card2Face.setText("J");
        }else if(inHand[1].contains("10")){
            card2Face.setText("10");
        }else if(inHand[1].contains("9")){
            card2Face.setText("9");
        }else if(inHand[1].contains("8")){
            card2Face.setText("8");
        }else if(inHand[1].contains("7")){
            card2Face.setText("7");
        }else if(inHand[1].contains("6")){
            card2Face.setText("6");
        }else if(inHand[1].contains("5")){
            card2Face.setText("5");
        }else if(inHand[1].contains("4")){
            card2Face.setText("4");
        }else if(inHand[1].contains("3")){
            card2Face.setText("3");
        }else if(inHand[1].contains("2")){
            card2Face.setText("2");
        }

    }

    public void showCard3(){
        ImageView card3SuitClub = (ImageView)findViewById(R.id.card3club);
        ImageView card3SuitSpade = (ImageView)findViewById(R.id.card3spade);
        ImageView card3SuitHeart = (ImageView)findViewById(R.id.card3heart);
        ImageView card3SuitDiamond = (ImageView)findViewById(R.id.card3diamond);

        card3SuitClub.setVisibility(View.INVISIBLE);
        card3SuitSpade.setVisibility(View.INVISIBLE);
        card3SuitHeart.setVisibility(View.INVISIBLE);
        card3SuitDiamond.setVisibility(View.INVISIBLE);

        TextView card3Face = (TextView)findViewById(R.id.card3facevalue);

        if(inHand[2].contains("Club")){
            card3SuitClub.setVisibility(View.VISIBLE);
        }else if(inHand[2].contains("Diamond")){
            card3SuitDiamond.setVisibility(View.VISIBLE);
        }else if(inHand[2].contains("Heart")){
            card3SuitHeart.setVisibility(View.VISIBLE);
        }else if(inHand[2].contains("Spade")){
            card3SuitSpade.setVisibility(View.VISIBLE);
        }

        if(inHand[2].contains("Ace")){
            card3Face.setText("A");
        }else if(inHand[2].contains("King")){
            card3Face.setText("K");
        }else if(inHand[2].contains("Queen")){
            card3Face.setText("Q");
        }else if(inHand[2].contains("Jack")){
            card3Face.setText("J");
        }else if(inHand[2].contains("10")){
            card3Face.setText("10");
        }else if(inHand[2].contains("9")){
            card3Face.setText("9");
        }else if(inHand[2].contains("8")){
            card3Face.setText("8");
        }else if(inHand[2].contains("7")){
            card3Face.setText("7");
        }else if(inHand[2].contains("6")){
            card3Face.setText("6");
        }else if(inHand[2].contains("5")){
            card3Face.setText("5");
        }else if(inHand[2].contains("4")){
            card3Face.setText("4");
        }else if(inHand[2].contains("3")){
            card3Face.setText("3");
        }else if(inHand[2].contains("2")){
            card3Face.setText("2");
        }

    }

    public void showCard4(){
        ImageView card4SuitClub = (ImageView)findViewById(R.id.card4club);
        ImageView card4SuitSpade = (ImageView)findViewById(R.id.card4spade);
        ImageView card4SuitHeart = (ImageView)findViewById(R.id.card4heart);
        ImageView card4SuitDiamond = (ImageView)findViewById(R.id.card4diamond);

        TextView card4Face = (TextView)findViewById(R.id.card4facevalue);

        card4SuitClub.setVisibility(View.INVISIBLE);
        card4SuitSpade.setVisibility(View.INVISIBLE);
        card4SuitHeart.setVisibility(View.INVISIBLE);
        card4SuitDiamond.setVisibility(View.INVISIBLE);

        if(inHand[3].contains("Club")){
            card4SuitClub.setVisibility(View.VISIBLE);
        }else if(inHand[3].contains("Diamond")){
            card4SuitDiamond.setVisibility(View.VISIBLE);
        }else if(inHand[3].contains("Heart")){
            card4SuitHeart.setVisibility(View.VISIBLE);
        }else if(inHand[3].contains("Spade")){
            card4SuitSpade.setVisibility(View.VISIBLE);
        }

        if(inHand[3].contains("Ace")){
            card4Face.setText("A");
        }else if(inHand[3].contains("King")){
            card4Face.setText("K");
        }else if(inHand[3].contains("Queen")){
            card4Face.setText("Q");
        }else if(inHand[3].contains("Jack")){
            card4Face.setText("J");
        }else if(inHand[3].contains("10")){
            card4Face.setText("10");
        }else if(inHand[3].contains("9")){
            card4Face.setText("9");
        }else if(inHand[3].contains("8")){
            card4Face.setText("8");
        }else if(inHand[3].contains("7")){
            card4Face.setText("7");
        }else if(inHand[3].contains("6")){
            card4Face.setText("6");
        }else if(inHand[3].contains("5")){
            card4Face.setText("5");
        }else if(inHand[3].contains("4")){
            card4Face.setText("4");
        }else if(inHand[3].contains("3")){
            card4Face.setText("3");
        }else if(inHand[3].contains("2")){
            card4Face.setText("2");
        }
    }

    public void showNextOption(String nextCard){
        ImageView nextOptionSpade = (ImageView)findViewById(R.id.nextcardspade);
        ImageView nextOptionHeart = (ImageView)findViewById(R.id.nextcardheart);
        ImageView nextOptionClub = (ImageView)findViewById(R.id.nextcardclub);
        ImageView nextOptionDiamond = (ImageView)findViewById(R.id.nextcarddiamond);

        nextOptionSpade.setVisibility(View.INVISIBLE);
        nextOptionHeart.setVisibility(View.INVISIBLE);
        nextOptionClub.setVisibility(View.INVISIBLE);
        nextOptionDiamond.setVisibility(View.INVISIBLE);

        TextView nextCardFace = (TextView)findViewById(R.id.nextcardFaceValue);

        if(nextCard.contains("Club")){
            nextOptionClub.setVisibility(View.VISIBLE);
        }else if(nextCard.contains("Diamond")){
            nextOptionDiamond.setVisibility(View.VISIBLE);
        }else if(nextCard.contains("Heart")){
            nextOptionHeart.setVisibility(View.VISIBLE);
        }else if(nextCard.contains("Spade")){
            nextOptionSpade.setVisibility(View.VISIBLE);
        }

        if(nextCard.contains("Ace")){
            nextCardFace.setText("A");
        }else if(nextCard.contains("King")){
            nextCardFace.setText("K");
        }else if(nextCard.contains("Queen")){
            nextCardFace.setText("Q");
        }else if(nextCard.contains("Jack")){
            nextCardFace.setText("J");
        }else if(nextCard.contains("10")){
            nextCardFace.setText("10");
        }else if(nextCard.contains("9")){
            nextCardFace.setText("9");
        }else if(nextCard.contains("8")){
            nextCardFace.setText("8");
        }else if(nextCard.contains("7")){
            nextCardFace.setText("7");
        }else if(nextCard.contains("6")){
            nextCardFace.setText("6");
        }else if(nextCard.contains("5")){
            nextCardFace.setText("5");
        }else if(nextCard.contains("4")){
            nextCardFace.setText("4");
        }else if(nextCard.contains("3")){
            nextCardFace.setText("3");
        }else if(nextCard.contains("2")){
            nextCardFace.setText("2");
        }

    }

    public void checkPlayerWin(){
        int first = -1;
        int second = -2;
        int third = -3;
        int fourth = -4;

        if(inHand[0].contains("Ace")){
            first = 14;
        }else if(inHand[0].contains("King")){
            first = 13;
        }else if(inHand[0].contains("Queen")){
            first = 12;
        }else if(inHand[0].contains("Jack")){
            first = 11;
        }else if(inHand[0].contains("10")){
            first = 10;
        }else if(inHand[0].contains("9")){
            first = 9;
        }else if(inHand[0].contains("8")){
            first = 8;
        }else if(inHand[0].contains("7")){
            first = 7;
        }else if(inHand[0].contains("6")){
            first = 6;
        }else if(inHand[0].contains("5")){
            first = 5;
        }else if(inHand[0].contains("4")){
            first = 4;
        }else if(inHand[0].contains("3")){
            first = 3;
        }else if(inHand[0].contains("2")){
            first = 2;
        }

        if(inHand[1].contains("Ace")){
            second = 14;
        }else if(inHand[1].contains("King")){
            second = 13;
        }else if(inHand[1].contains("Queen")){
            second = 12;
        }else if(inHand[1].contains("Jack")){
            second = 11;
        }else if(inHand[1].contains("10")){
            second = 10;
        }else if(inHand[1].contains("9")){
            second = 9;
        }else if(inHand[1].contains("8")){
            second = 8;
        }else if(inHand[1].contains("7")){
            second = 7;
        }else if(inHand[1].contains("6")){
            second = 6;
        }else if(inHand[1].contains("5")){
            second = 5;
        }else if(inHand[1].contains("4")){
            second = 4;
        }else if(inHand[1].contains("3")){
            second = 3;
        }else if(inHand[1].contains("2")){
            second = 2;
        }

        if(inHand[2].contains("Ace")){
            third = 14;
        }else if(inHand[2].contains("King")){
            third = 13;
        }else if(inHand[2].contains("Queen")){
            third = 12;
        }else if(inHand[2].contains("Jack")){
            third = 11;
        }else if(inHand[2].contains("10")){
            third = 10;
        }else if(inHand[2].contains("9")){
            third = 9;
        }else if(inHand[2].contains("8")){
            third = 8;
        }else if(inHand[2].contains("7")){
            third = 7;
        }else if(inHand[2].contains("6")){
            third = 6;
        }else if(inHand[2].contains("5")){
            third = 5;
        }else if(inHand[2].contains("4")){
            third = 4;
        }else if(inHand[2].contains("3")){
            third = 3;
        }else if(inHand[2].contains("2")){
            third = 2;
        }

        if(inHand[3].contains("Ace")){
            fourth = 14;
        }else if(inHand[3].contains("King")){
            fourth = 13;
        }else if(inHand[3].contains("Queen")){
            fourth = 12;
        }else if(inHand[3].contains("Jack")){
            fourth = 11;
        }else if(inHand[3].contains("10")){
            fourth = 10;
        }else if(inHand[3].contains("9")){
            fourth = 9;
        }else if(inHand[3].contains("8")){
            fourth = 8;
        }else if(inHand[3].contains("7")){
            fourth = 7;
        }else if(inHand[3].contains("6")){
            fourth = 6;
        }else if(inHand[3].contains("5")){
            fourth = 5;
        }else if(inHand[3].contains("4")){
            fourth = 4;
        }else if(inHand[3].contains("3")){
            fourth = 3;
        }else if(inHand[3].contains("2")){
            fourth = 2;
        }

        if(first == second && first == third && first == fourth && second == third && second == fourth && third == fourth){
            won = true;
        }
    }
}
