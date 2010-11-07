package com._17od.blackjack.strategy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com._17od.blackjack.Card;
import com._17od.blackjack.Card.Rank;
import com._17od.blackjack.Card.Suit;
import com._17od.blackjack.Game;
import com._17od.blackjack.GameRules;
import com._17od.blackjack.Player;

/**
 * Test the HiLo Strategy
 * 
 * @author Adrian Smith
 */
public class HiLoStrategyTest {

    @Test
    /**
     * Test the running count is being maintained correctly when a low card is
     * dealt. 
     */
    public void testRunningCountWithLowCard() {
        Game game = new Game(new GameRules());
        Player player = new Player();
        HiLoStrategy strategy = new HiLoStrategy(game, player);
        
        game.deal(new Card(Rank.TWO, Suit.CLUBS), player);
        
        assertEquals(1, strategy.getRunningCount());
    }
    
    @Test
    /**
     * Test the running count is being maintained correctly when a high card is
     * dealt. 
     */
    public void testRunningCountWithHighCard() {
        Game game = new Game(new GameRules());
        Player player = new Player();
        HiLoStrategy strategy = new HiLoStrategy(game, player);
        
        game.deal(new Card(Rank.TEN, Suit.CLUBS), player);
        
        assertEquals(-1, strategy.getRunningCount());
    }
    
    @Test
    /**
     * Test the running count is being maintained correctly when a neutral card
     * is dealt. 
     */
    public void testRunningCountWithNeutralCard() {
        Game game = new Game(new GameRules());
        Player player = new Player();
        HiLoStrategy strategy = new HiLoStrategy(game, player);
        
        game.deal(new Card(Rank.SEVEN, Suit.CLUBS), player);
        
        assertEquals(0, strategy.getRunningCount());
    }

    @Test
    /**
     * Deal a few cards so that we have a negative true count. Check
     * that the strategy returns 1 unit (the minimum bet). 
     */
    public void testAmountToBetWithNegativeTrueCount() {
        GameRules gameRules = new GameRules();
        gameRules.setNumberOfDecks(6);
        gameRules.setMinimumBet(5);
        Game game = new Game(gameRules);
        Player player = new Player();
        HiLoStrategy strategy = new HiLoStrategy(game, player);
        
        game.deal(new Card(Rank.SEVEN, Suit.CLUBS), player);
        game.deal(new Card(Rank.TWO, Suit.CLUBS), player);
        game.deal(new Card(Rank.TEN, Suit.CLUBS), player);
        game.deal(new Card(Rank.QUEEN, Suit.CLUBS), player);
        game.deal(new Card(Rank.ACE, Suit.CLUBS), player);
        game.deal(new Card(Rank.THREE, Suit.CLUBS), player);
        
        assertEquals(-1, strategy.getRunningCount());
        assertEquals((gameRules.getNumberOfDecks() * 52) - 6, 
                strategy.getCardsLeft());
        assertEquals(gameRules.getMinimumBet(), strategy.amountToBet());
    }

    @Test
    /**
     * Deal a few cards so that we have a true count of 4. Check
     * that the strategy returns 5 units (the minimum bet * 5). 
     */
    public void testAmountToBetWithTrueCountOf4() {
        GameRules gameRules = new GameRules();
        gameRules.setNumberOfDecks(1);
        gameRules.setMinimumBet(15);
        Game game = new Game(gameRules);
        Player player = new Player();
        HiLoStrategy strategy = new HiLoStrategy(game, player);
        
        game.deal(new Card(Rank.TWO, Suit.CLUBS), player);
        game.deal(new Card(Rank.THREE, Suit.CLUBS), player);
        game.deal(new Card(Rank.FOUR, Suit.CLUBS), player);
        game.deal(new Card(Rank.FIVE, Suit.CLUBS), player);
        game.deal(new Card(Rank.SIX, Suit.CLUBS), player);
        game.deal(new Card(Rank.TWO, Suit.CLUBS), player);
        game.deal(new Card(Rank.THREE, Suit.CLUBS), player);
        game.deal(new Card(Rank.TEN, Suit.CLUBS), player);
        game.deal(new Card(Rank.QUEEN, Suit.CLUBS), player);
        game.deal(new Card(Rank.ACE, Suit.CLUBS), player);
        game.deal(new Card(Rank.KING, Suit.CLUBS), player);
        
        assertEquals(3, strategy.getRunningCount());
        assertEquals((gameRules.getNumberOfDecks() * 52) - 11, 
                strategy.getCardsLeft());
        assertEquals(gameRules.getMinimumBet() * 5, strategy.amountToBet());
    }
}
