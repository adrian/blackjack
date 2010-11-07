package com._17od.blackjack;

/**
 * Represents a card.
 * 
 * @author Adrian Smith
 */
public class Card {

    public enum Rank { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        int value = 0;

        switch (rank) {
        case TWO:
            value = 2;
            break;
        case THREE:
            value = 3;
            break;
        case FOUR:
            value = 4;
            break;
        case FIVE:
            value = 5;
            break;
        case SIX:
            value = 6;
            break;
        case SEVEN:
            value = 7;
            break;
        case EIGHT:
            value = 8;
            break;
        case NINE:
            value = 9;
            break;
        case TEN:
            value = 10;
            break;
        case JACK:
            value = 10;
            break;
        case QUEEN:
            value = 10;
            break;
        case KING:
            value = 10;
            break;
        case ACE:
            value = 11;
            break;
        default:
            throw new InvalidCardRank(rank);
        }
        
        return value;
    }

    public String toString() {
        return rank + " " + suit;
    }

}
