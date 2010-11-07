package com._17od.blackjack.strategy;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com._17od.blackjack.Card;
import com._17od.blackjack.Card.Rank;
import com._17od.blackjack.Card.Suit;
import com._17od.blackjack.Game;
import com._17od.blackjack.GameException;
import com._17od.blackjack.GameRules;
import com._17od.blackjack.Player;
import com._17od.blackjack.decisions.DoubleDown;
import com._17od.blackjack.decisions.Hit;
import com._17od.blackjack.decisions.Split;
import com._17od.blackjack.decisions.Stand;

/**
 * Unit tests for the BasicStrategy class.
 * 
 * @author Adrian Smith
 */
public class BasicStrategyTest {

    @Test
    public void testWhenPlayer5andDealer2() {
        Game game = new Game(new GameRules());
        Player player = new Player();

        game.deal(new Card(Rank.TWO, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.THREE, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.TWO, Suit.DIAMONDS), game.getDealer());

        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof Hit);
    }

    @Test
    public void testWhenPlayerPairOf4sAndDealer6() {
        Game game = new Game(new GameRules());
        Player player = new Player();

        game.deal(new Card(Rank.FOUR, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.FOUR, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SIX, Suit.DIAMONDS), game.getDealer());

        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof Split);
    }

    @Test
    public void testWhenPlayerPairOfAcesAndDealer7() {
        Game game = new Game(new GameRules());
        Player player = new Player();

        game.deal(new Card(Rank.TEN, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.TEN, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SEVEN, Suit.DIAMONDS), game.getDealer());

        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof Stand);
    }

    @Test
    public void testWhenPlayerAce7AndDealer7() {
        Game game = new Game(new GameRules());
        Player player = new Player();

        game.deal(new Card(Rank.ACE, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SEVEN, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SEVEN, Suit.DIAMONDS), game.getDealer());

        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof Stand);
    }

    @Test(expected=GameException.class)
    public void testWhenPlayerBust() {
        Game game = new Game(new GameRules());
        Player player = new Player();

        game.deal(new Card(Rank.QUEEN, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.KING, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SEVEN, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.FIVE, Suit.DIAMONDS), game.getDealer());

        BasicStrategy strategy = new BasicStrategy(game, player);

        strategy.whatNext();
    }

    @Test
    public void testDouble() {
        Game game = new Game(new GameRules());
        Player player = new Player();

        game.deal(new Card(Rank.FIVE, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SIX, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.TWO, Suit.DIAMONDS), game.getDealer());

        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof DoubleDown);
    }

    @Test
    public void testDoubleAfterSplitWhenNotAllowed() {
        GameRules rules = new GameRules();
        rules.setDoubleAfterSplit(false);
        Game game = new Game(rules);
        Player player = new Player();

        game.deal(new Card(Rank.FIVE, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SIX, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.TWO, Suit.DIAMONDS), game.getDealer());
        player.getHand().setCameFromSplit(true);

        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof Hit);
    }


    @Test
    public void testDoubleAfterSplitWhenSplitAllowed() {
        GameRules rules = new GameRules();
        rules.setDoubleAfterSplit(true);
        Game game = new Game(rules);
        Player player = new Player();

        game.deal(new Card(Rank.FIVE, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.SIX, Suit.DIAMONDS), player);
        game.deal(new Card(Rank.TWO, Suit.DIAMONDS), game.getDealer());
        player.getHand().setCameFromSplit(true);
        
        BasicStrategy strategy = new BasicStrategy(game, player);

        assertTrue(strategy.whatNext() instanceof DoubleDown);
    }

}
