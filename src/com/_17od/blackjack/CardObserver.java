package com._17od.blackjack;

/**
 * An observer is an object interested in seeing what cards are dealt. 
 * 
 * @author Adrian Smith
 */
public interface CardObserver {

    /**
     * Each time a card is dealt this method will be called.
     * 
     * @param card The card dealt.
     */
    public void notify(Card card);
    
}
