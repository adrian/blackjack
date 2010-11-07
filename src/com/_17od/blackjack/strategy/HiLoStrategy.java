package com._17od.blackjack.strategy;

import com._17od.blackjack.Card;
import com._17od.blackjack.CardObserver;
import com._17od.blackjack.Game;
import com._17od.blackjack.Player;

/**
 * The Hi Lo strategy is a card counting technique. See 
 * <a href="http://en.wikipedia.org/wiki/Blackjack#Card_counting">here</a> 
 * for more details.
 * <p>
 * This strategy extends BasicStrategy and relies on it to determine what
 * decision a player should make once he's been dealt a hand. The aim of this
 * strategy is to determine the amount that should be bet.   
 * 
 * @author Adrian Smith
 */
public class HiLoStrategy extends BasicStrategy implements CardObserver {

    private int runningCount;
    private int cardsLeft;

    public HiLoStrategy(Game game, Player player) {
        super(game, player);
        initialiseShoe();
        game.addCardObserver(this);
    }

    @Override
    /**
     * Determine how much to bet based on the running count and a very simple
     * spread bet table
     * @return the amount to bet
     */
    public int amountToBet() {
        // Determine the true count. This is the running count / number of decks
        // left
        float runningCountFloat = runningCount;
        float cardsLeftFloat = cardsLeft;
        int trueCount = Math.round(runningCountFloat / (cardsLeftFloat / 52));
        
        // Based on the trueCount consult a spread bet table to decide how many
        // units should be bet.
        // This is a very simple table but will suffice for the task in hand.
        int units = 0;
        if (trueCount <= 0) {
            units = 1;
        } else if (trueCount == 1) {
            units = 2;
        } else if (trueCount == 2) {
            units = 3;
        } else if (trueCount == 3) {
            units = 4;
        } else if (trueCount == 4) {
            units = 5;
        } else if (trueCount >= 5) {
            units = 6;
        }
        
        return gameRules.getMinimumBet() * units;
    }

    @Override
    /**
     * This method will be called when a card is dealt. It's used to keep a 
     * running count.
     * <p>
     * Each card is assigned a value. This value is added to a running total.
     * <p>
     * The card values are:
     * <ul>
     *   <li>2, 3, 4, 5, 6 are given the value +1</li>
     *   <li>10, Jack, Queen, King and Ace are given the value -1</li>
     *   <li>7, 8, 9 have no value</li>
     * </ul>
     */
    public void notify(Card card) {
        switch(card.getRank()) {
        case TWO:
            runningCount += 1;
            break;
        case THREE:
            runningCount += 1;
            break;
        case FOUR:
            runningCount += 1;
            break;
        case FIVE:
            runningCount += 1;
            break;
        case SIX:
            runningCount += 1;
            break;
        case TEN:
            runningCount -= 1;
            break;
        case JACK:
            runningCount -= 1;
            break;
        case QUEEN:
            runningCount -= 1;
            break;
        case KING:
            runningCount -= 1;
            break;
        case ACE:
            runningCount -= 1;
            break;
        }
        
        cardsLeft -= 1;
        
        // If there are not more cards then reinitialise the shoe
        if (cardsLeft == 0) {
            initialiseShoe();
        }
    }

    public int getRunningCount() {
        return runningCount;
    }

    public int getCardsLeft() {
        return cardsLeft;
    }

    @Override
    public String toString() {
        return "Hi-Lo Strategy"; 
    }

    /**
     * Initialise the running count and refresh the shoe with a full complement
     * of cards.
     */
    private void initialiseShoe() {
        runningCount = 0;
        cardsLeft = 52 * gameRules.getNumberOfDecks();
    }

}
