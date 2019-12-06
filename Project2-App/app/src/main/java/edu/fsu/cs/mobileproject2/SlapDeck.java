package edu.fsu.cs.mobileproject2;

import java.util.List;

public class SlapDeck {

    private int numCards;
    private List<Card> cards;
    private Card bottomCard;
    private Card topCard;

    public SlapDeck(int numCards, List<Card> cards, Card bottomCard, Card topCard){
        this.bottomCard = bottomCard;
        this.numCards = numCards;
        this.cards = cards;
        this.topCard = topCard;
    }

    public Card getBottomCard() {
        return bottomCard;
    }

    public int getNumCards() {
        return numCards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setBottomCard(Card bottomCard) {
        this.bottomCard = bottomCard;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }
}
