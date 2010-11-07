package com._17od.blackjack;

import java.util.ArrayList;

/**
 * Represents a blackjack game.
 *  
 * @author Adrian Smith
 */
public class Game {

    private Dealer dealer;
    private GameRules rules;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<CardObserver> cardObservers = new ArrayList<CardObserver>();

    public Game(GameRules rules) {
        dealer = new Dealer();
        this.rules = rules;
    }

    /**
     * Deal the card to the given player and notify all observers of the card
     * dealt.
     * 
     * @param card the card to deal
     * @param player The player to deal a card to.
     */
    public void deal(Card card, Player player) {
        player.dealCard(card);

        for (CardObserver cardObserver : cardObservers) {
            cardObserver.notify(card);
        }
    }

    /**
     * Add the given observer to the list. This object will be notified each
     * time a car is dealt 
     * @param cardObserver The observer to notify when a card is dealt
     */
    public void addCardObserver(CardObserver cardObserver) {
        cardObservers.add(cardObserver);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Dealer getDealer() {
        return dealer;
    }

    public GameRules getRules() {
        return rules;
    }

}
