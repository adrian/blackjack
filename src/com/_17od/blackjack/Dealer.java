package com._17od.blackjack;

public class Dealer extends Player {

    /**
     * Returns the first card the dealer was dealt, their face-up card.
     * @return the dealer's faceup card
     */
    public Card getFaceUpCard() {
        return getHand().getCards().get(0);
    }

}
