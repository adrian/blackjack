package com._17od.blackjack;

import java.util.ArrayList;

import com._17od.blackjack.Card.Rank;

/**
 * Represents the cards a player is dealt.
 *  
 * @author Adrian Smith
 */
public class Hand {

    private ArrayList<Card> cards = new ArrayList<Card>();
    private boolean cameFromSplit;

    /**
     * Add a new card to the hand
     * @param card The card to add to the hand
     */
    public void add(Card card) {
        cards.add(card);
    }

    /**
     * Calculate the hand total.
     * 
     * @return A HandTotal object with the total and weather or not it's a soft
     * total. 
     */
    public HandTotal calculateTotal() {
        int total = 0;
        boolean soft = false;

        // Total up all the cards. Aces have a value of 11 at this stage.
        for (Card card : cards) {
            total += card.getValue();
        }

        // If the total is > 21 then one by one give each ace a value of 1 until
        // the total falls <= 21 or we run out of aces
        int i = 0;
        int numAces = getNumAces();
        while (total > 21 && i < numAces) {
            total -= 10;
            i++;
        }

        // If we still have aces left that are counted as 11 then this is a soft
        // hand
        if (i < numAces) {
            soft = true;
        }

        return new HandTotal(total, soft);
    }

    /**
     * Has a pair of hands with the same rank. This only works if there are two
     * cards in the hand. If there are more than two then false is returned.
     * 
     * @return true if the hand has a pair of any type.
     */
    public boolean isPair() {
        if (cards.size() == 2) {
            if (cards.get(0).getRank() == cards.get(1).getRank()) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Card> getCards() {
        return cards;
    }

    private int getNumAces() {
        int num = 0;

        for (Card card : cards) {
            if (card.getRank() == Rank.ACE) {
                num += 1;
            }
        }
        
        return num;
    }

    public boolean cameFromSplit() {
        return cameFromSplit;
    }

    public void setCameFromSplit(boolean cameFromSplit) {
        this.cameFromSplit = cameFromSplit;
    }

    public void clear() {
        cards.clear();
    }

    public String toString() {
        StringBuilder hand = new StringBuilder();
        for (int i=0; i<cards.size(); i++) {
            Card card = cards.get(i);
            hand.append(card.toString());
            if (i < cards.size() - 1) {
                hand.append(", ");
            }
        }
        hand.append(" ");
        hand.append(calculateTotal());
        hand.append(" ");
        hand.append("cameFromSplit=");
        hand.append(cameFromSplit);
        return hand.toString();
    }
}
