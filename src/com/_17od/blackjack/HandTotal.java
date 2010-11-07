package com._17od.blackjack;

public class HandTotal {

    private int total;
    private boolean soft;

    public HandTotal(int total, boolean soft) {
        this.total = total;
        this.soft = soft;
    }

    public int getTotal() {
        return total;
    }
    
    public boolean isSoft() {
        return soft;
    }

    public String toString() {
        StringBuilder totalString = new StringBuilder();
        totalString.append("(");
        totalString.append(soft ? "Soft " : "Hard ");
        totalString.append(total);
        totalString.append(")");
        return totalString.toString();
    }

}
