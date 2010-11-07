package com._17od.blackjack.strategy;

import com._17od.blackjack.Game;
import com._17od.blackjack.Player;

/**
 * Create in instance of the HiLo strategy
 * 
 * @author Adrian Smith
 */
public class HiLoStrategyBuilder implements StrategyBuilder {

    @Override
    /**
     * Create an instance of the HiLoStrategy
     * @param game The game this strategy will be part of
     * @param player The player that will own this strategy
     * @return an instance of the HiLoStrategy
     */
    public Strategy create(Game game, Player player) {
        HiLoStrategy strategy = new HiLoStrategy(game, player);
        player.setStrategy(strategy);
        return strategy;
    }

}
