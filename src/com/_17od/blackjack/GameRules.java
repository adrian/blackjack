package com._17od.blackjack;

/**
 * This class maintains the rules for a game of blackjack.
 * 
 * @author Adrian Smith
 */
public class GameRules {

    private boolean surrenderAllowed;
    private boolean doubleOn91011Only;
    private boolean doubleOn1011Only;
    private boolean doubleAfterSplit;
    private int minimumBet;
    private int numberOfDecks;

    /**
     * Set the default games rules
     */
    public GameRules() {
        minimumBet = 5;
        numberOfDecks = 1;
        surrenderAllowed = false;
        doubleOn91011Only = false;
        doubleOn1011Only = false;
        doubleAfterSplit = true;
    }

    public boolean isSurrenderAllowed() {
        return surrenderAllowed;
    }

    public void setSurrenderAllowed(boolean surrenderAllowed) {
        this.surrenderAllowed = surrenderAllowed;
    }

    public boolean doubleOn91011Only() {
        return doubleOn91011Only;
    }

    public void setDoubleOn91011Only(boolean doubleOn91011Only) {
        this.doubleOn91011Only = doubleOn91011Only;
    }

    public boolean doubleOn1011Only() {
        return doubleOn1011Only;
    }

    public void setDoubleOn1011Only(boolean doubleOn1011Only) {
        this.doubleOn1011Only = doubleOn1011Only;
    }

    public boolean doubleAfterSplit() {
        return doubleAfterSplit;
    }

    public void setDoubleAfterSplit(boolean doubleAfterSplit) {
        this.doubleAfterSplit = doubleAfterSplit;
    }

    public int getMinimumBet() {
        return minimumBet;
    }

    public void setMinimumBet(int bet) {
        this.minimumBet = bet;
    }

    public int getNumberOfDecks() {
        return numberOfDecks;
    }

    public void setNumberOfDecks(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
    }

    public String toString() {
        StringBuilder rules= new StringBuilder();
        rules.append("surrenderAllowed=");
        rules.append(surrenderAllowed);
        rules.append(", ");
        rules.append("doubleOn91011Only=");
        rules.append(doubleOn91011Only);
        rules.append(", ");
        rules.append("doubleOn1011Only=");
        rules.append(doubleOn1011Only);
        rules.append(", ");
        rules.append("doubleAfterSplit=");
        rules.append(doubleAfterSplit);
        rules.append(", ");
        rules.append("minimumBet=");
        rules.append(minimumBet);
        rules.append(", ");
        rules.append("numberOfDecks=");
        rules.append(numberOfDecks);
        return rules.toString();
    }
}
