package com._17od.blackjack.strategy;

import com._17od.blackjack.Game;
import com._17od.blackjack.Player;

/**
 * Concreate implementations of this class are used to build instances of
 * Stratgy classes.  
 * 
 * @author Adrian Smith
 */
public interface StrategyBuilder {

    public Strategy create(Game game, Player player);

}
