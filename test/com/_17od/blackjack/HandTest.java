package com._17od.blackjack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com._17od.blackjack.Card.Rank;
import com._17od.blackjack.Card.Suit;

/**
 * Unit tests for the Hand class.
 * 
 * @author Adrian Smith
 */
public class HandTest {

    @Test
    /**
     * Test the value of individual cards.
     */
    public void testIndividualCardTotals() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        assertEquals(2, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.THREE, Suit.CLUBS));
        assertEquals(3, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.FOUR, Suit.CLUBS));
        assertEquals(4, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.FIVE, Suit.CLUBS));
        assertEquals(5, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.SIX, Suit.CLUBS));
        assertEquals(6, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.SEVEN, Suit.CLUBS));
        assertEquals(7, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.EIGHT, Suit.CLUBS));
        assertEquals(8, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.NINE, Suit.CLUBS));
        assertEquals(9, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.TEN, Suit.CLUBS));
        assertEquals(10, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.JACK, Suit.CLUBS));
        assertEquals(10, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.KING, Suit.CLUBS));
        assertEquals(10, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.QUEEN, Suit.CLUBS));
        assertEquals(10, hand.calculateTotal().getTotal());

        hand = new Hand();
        hand.add(new Card(Rank.ACE, Suit.CLUBS));
        assertEquals(11, hand.calculateTotal().getTotal());
    }

    @Test
    /**
     * Test the hard total of a hand with multiple cards.
     */
    public void testHandTotalWithMultipleCards() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        hand.add(new Card(Rank.KING, Suit.CLUBS));
        hand.add(new Card(Rank.QUEEN, Suit.CLUBS));
        assertEquals(22, hand.calculateTotal().getTotal());
    }

    @Test
    /**
     * Test the soft total of a hand with multiple cards.
     */
    public void testHardHandWithMultipleCards() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        hand.add(new Card(Rank.KING, Suit.CLUBS));
        hand.add(new Card(Rank.ACE, Suit.CLUBS));
        HandTotal handTotal = hand.calculateTotal();
        assertEquals(13, handTotal.getTotal());
        assertFalse(handTotal.isSoft());
    }

    @Test
    public void testHandIsPair() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        assertTrue(hand.isPair());
    }

    @Test
    public void testHandIsPairWithMoreThan2Cards() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        hand.add(new Card(Rank.THREE, Suit.CLUBS));
        assertFalse(hand.isPair());
    }

    @Test
    public void testHandNotPair() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        hand.add(new Card(Rank.KING, Suit.CLUBS));
        assertFalse(hand.isPair());
    }

    @Test
    public void testSoftTotal() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.ACE, Suit.CLUBS));
        hand.add(new Card(Rank.KING, Suit.CLUBS));
        HandTotal handTotal = hand.calculateTotal();
        assertEquals(21, handTotal.getTotal());
        assertTrue(handTotal.isSoft());
    }

    @Test
    public void testSoftTotalWithTwoAces() {
        Hand hand = new Hand();
        hand.add(new Card(Rank.ACE, Suit.CLUBS));
        hand.add(new Card(Rank.ACE, Suit.CLUBS));
        hand.add(new Card(Rank.TWO, Suit.CLUBS));
        HandTotal handTotal = hand.calculateTotal();
        assertEquals(14, handTotal.getTotal());
        assertTrue(handTotal.isSoft());
    }

}
