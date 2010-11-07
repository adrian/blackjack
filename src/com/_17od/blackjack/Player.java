package com._17od.blackjack;

import com._17od.blackjack.strategy.Strategy;

/**
 * Represents a player in a {@link Game} of blackjack.
 *  
 * @author Adrian Smith
 */
public class Player {

    private Strategy strategy;
    private Hand hand;

    public Player() {
        hand = new Hand();
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Hand getHand() {
        return hand;
    }

    public void dealCard(Card card) {
        hand.add(card);
    }

}
