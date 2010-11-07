package com._17od.blackjack;

/**
 * Used when an attempt is made to do something that isn't allowed, e.g. playing
 * a bust hand.
 * 
 * @author Adrian Smith
 */
@SuppressWarnings("serial")
public class GameException extends RuntimeException {

    public GameException(String message) {
        super(message);
    }

}
