package com._17od.blackjack.strategy;

import com._17od.blackjack.decisions.Decision;

/**
 * Represents the strategy a Player may adopt.
 * 
 * @author Adrian Smith
 */
public interface Strategy {

    public enum DecisionEnum { STAND, HIT, DOUBLE, DOUBLE_OR_HIT, 
        DOUBLE_OR_STAND, SPLIT, SURRENDER }

    public int amountToBet();
    
    /**
     * Get the next move. What that move is is a decision for the concrete
     * strategy to make.
     * @return The next move to make
     */
    public Decision whatNext();

}
