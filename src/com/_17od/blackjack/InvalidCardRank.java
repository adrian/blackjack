package com._17od.blackjack;

import com._17od.blackjack.Card.Rank;

/**
 * Thrown when a card with an invalid {@link Rank} is encountered.
 * 
 * @author Adrian Smith
 */
@SuppressWarnings("serial")
public class InvalidCardRank extends RuntimeException {

    public InvalidCardRank(Rank rank) {
        super("Found a card with the unknown rank, " + rank);
    }

}
