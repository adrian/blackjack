package com._17od.blackjack.strategy;

import com._17od.blackjack.Game;
import com._17od.blackjack.Player;

/**
 * Build an instance of a BasicStrategy class.
 * 
 * @author Adrian Smith
 */
public class BasicStrategyBuilder implements StrategyBuilder {

    @Override
    public Strategy create(Game game, Player player) {
        BasicStrategy strategy = new BasicStrategy(game, player);
        player.setStrategy(strategy);
        return strategy;
    }

}
