package edu.fsu.cs.mobileproject2;

public class Card {
    private int number;
    private String suit;

    public Card(int number, String suit){
        this.number = number;
        this.suit = suit;
    }

    public String getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }
}
