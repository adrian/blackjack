package com._17od.blackjack.strategy;

import com._17od.blackjack.Game;
import com._17od.blackjack.GameException;
import com._17od.blackjack.GameRules;
import com._17od.blackjack.Hand;
import com._17od.blackjack.HandTotal;
import com._17od.blackjack.Player;
import com._17od.blackjack.decisions.Decision;
import com._17od.blackjack.decisions.DoubleDown;
import com._17od.blackjack.decisions.Hit;
import com._17od.blackjack.decisions.Split;
import com._17od.blackjack.decisions.Stand;

/**
 * A simple strategy based on the player's hand and the dealer's face-up card.
 * <p>
 * See <a href="http://en.wikipedia.org/wiki/Blackjack#Basic_strategy">here</a>
 * for more info.
 * <p>
 * This strategy uses a simple lookup table to determine what the next move
 * should be. This could have been achieved using a series of if or switch
 * statements but that would have made for a very long method. What it does
 * instead is use three two dimensional arrays (giving us O(1) lookup time).
 * <p>
 * The first dimension is the player's hand's total. The second is the value of
 * the dealer's faceup card. The three lookup tables are "Hard Totals",
 * "Soft Totals" and "Pairs".
 * <p>
 * The amount bet is the minimum amoutn allowed.
 * 
 * @author Adrian Smith
 */
public class BasicStrategy implements Strategy {

    private Hand playerHand;
    private Hand dealerHand;
    protected GameRules gameRules;

    private DecisionEnum[][] hardTotalsStrategies = new DecisionEnum[16][10];
    private DecisionEnum[][] softTotalsStrategies = new DecisionEnum[8][10];
    private DecisionEnum[][] pairsStrategies = new DecisionEnum[19][10];

    /**
     * 
     * @param playerHand The player's hand
     * @param dealerHand The dealer's hand
     * @param gameRules The game rules
     */
    public BasicStrategy(Game game, Player player) {
        this.playerHand = player.getHand();
        this.dealerHand = game.getDealer().getHand();
        this.gameRules = game.getRules();

        populateHardTotalsStrategies();
        populateSoftTotalsStrategies();
        populatePairsStrategies();
    }

    @Override
    /**
     * Bet the minimum amount the game rules allow.
     * @return the minimum amount the game rules allow
     */
    public int amountToBet() {
        return gameRules.getMinimumBet();
    }

    @Override
    /**
     * Determine the next move based upon the dealer's faceup card and the
     * player's hand.
     * 
     * @return The next move to make.
     */
    public Decision whatNext() {

        // Before going any further check for bust and 21 
        HandTotal playersHandTotal = playerHand.calculateTotal();
        if (playersHandTotal.getTotal() > 21) {
            throw new GameException("Hand is bust");
        } else if (playersHandTotal.getTotal() == 21) {
            return new Stand();
        }

        // Determine the index position to use on the dealer dimension
        int dealerArrayPos = dealerHand.getCards().get(0).getValue() - 2;

        // Lookup the next move
        DecisionEnum decisionEnum = null;
        if (playerHand.isPair()) {
            int playerArrayPos = playerHand.getCards().get(0).getValue() - 2;
            decisionEnum = pairsStrategies[playerArrayPos][dealerArrayPos];
        } else if (playersHandTotal.isSoft()) {
            int playerArrayPos = playersHandTotal.getTotal() - 13;
            decisionEnum = softTotalsStrategies[playerArrayPos][dealerArrayPos];
        } else {
            int playerArrayPos = playersHandTotal.getTotal() - 5;
            decisionEnum = hardTotalsStrategies[playerArrayPos][dealerArrayPos];
        }

        // If we got a DOUBLE_OR_? then figure out if we can double. If we can't
        // then take the other option.
        Decision decision = null;
        if (decisionEnum == DecisionEnum.DOUBLE_OR_HIT) {
            if (canDouble()) {
                decision = new DoubleDown();
            } else {
                decision = new Hit();
            }
        } else if (decisionEnum == DecisionEnum.DOUBLE_OR_STAND) {
            if (canDouble()) {
                decision = new DoubleDown();
            } else {
                decision = new Stand();
            }
        } else if (decisionEnum == DecisionEnum.HIT) {
            decision = new Hit();
        } else if (decisionEnum == DecisionEnum.STAND) {
            decision = new Stand();
        } else if (decisionEnum == DecisionEnum.SURRENDER) {
            decision = new Stand();
        } else if (decisionEnum == DecisionEnum.SPLIT) {
            decision = new Split();
        }

        return decision;
    }

    /**
     * Figures out if we can double.
     * <p>
     * A double can only happen on the first two cards.
     * <p>
     * The "Double on Split" rule determines weather a double can happen after
     * a split.
     * <p>
     * If the Reno rule is in play then a double is only allowed if the hand is
     * 9/10/11 or alternatively 10/11.
     * 
     * @return true if the hand can be doubled
     */
    private boolean canDouble() {
        boolean canDouble = false;

        if (playerHand.getCards().size() == 2) {
            if ((playerHand.cameFromSplit() && gameRules.doubleAfterSplit()) || 
                    !playerHand.cameFromSplit()) {
                if (gameRules.doubleOn91011Only()) {
                    int total = playerHand.calculateTotal().getTotal();
                    if (total >=9 && total <= 11) {
                        canDouble = true;
                    }
                } else if (gameRules.doubleOn1011Only()) {
                    int total = playerHand.calculateTotal().getTotal();
                    if (total ==10 && total == 11) {
                        canDouble = true;
                    }
                } else {
                    canDouble = true;
                }
            }
        }

        return canDouble;
    }

    /**
     * Populate a two dimensional array based on the players hard total and the
     * dealers faceup card.
     * 
     * To make the arrays as small as possible the value of the cards is mapped
     * to specific locations in the array.
     *  
     * A player's hard total is reduced by 5. For example, a total of 12 will be
     * mapped to location 7 in the first dimension.
     * 
     * For a dealer the value is reduced by 2. Ace is treated as a hard 11
     * before being reduced to 9.
     */
    private void populateHardTotalsStrategies() {
        // Dealer 2
        hardTotalsStrategies[0][0] = DecisionEnum.HIT;
        hardTotalsStrategies[1][0] = DecisionEnum.HIT;
        hardTotalsStrategies[2][0] = DecisionEnum.HIT;
        hardTotalsStrategies[3][0] = DecisionEnum.HIT;
        hardTotalsStrategies[5][0] = DecisionEnum.HIT;
        hardTotalsStrategies[4][0] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][0] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][0] = DecisionEnum.HIT;
        hardTotalsStrategies[8][0] = DecisionEnum.STAND;
        hardTotalsStrategies[9][0] = DecisionEnum.STAND;
        hardTotalsStrategies[10][0] = DecisionEnum.STAND;
        hardTotalsStrategies[11][0] = DecisionEnum.STAND;
        hardTotalsStrategies[12][0] = DecisionEnum.STAND;
        hardTotalsStrategies[13][0] = DecisionEnum.STAND;
        hardTotalsStrategies[14][0] = DecisionEnum.STAND;
        hardTotalsStrategies[15][0] = DecisionEnum.STAND;

        // Dealer 3
        hardTotalsStrategies[0][1] = DecisionEnum.HIT;
        hardTotalsStrategies[1][1] = DecisionEnum.HIT;
        hardTotalsStrategies[2][1] = DecisionEnum.HIT;
        hardTotalsStrategies[3][1] = DecisionEnum.HIT;
        hardTotalsStrategies[5][1] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[4][1] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][1] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][1] = DecisionEnum.HIT;
        hardTotalsStrategies[8][1] = DecisionEnum.STAND;
        hardTotalsStrategies[9][1] = DecisionEnum.STAND;
        hardTotalsStrategies[10][1] = DecisionEnum.STAND;
        hardTotalsStrategies[11][1] = DecisionEnum.STAND;
        hardTotalsStrategies[12][1] = DecisionEnum.STAND;
        hardTotalsStrategies[13][1] = DecisionEnum.STAND;
        hardTotalsStrategies[14][1] = DecisionEnum.STAND;
        hardTotalsStrategies[15][1] = DecisionEnum.STAND;

        // Dealer 4
        hardTotalsStrategies[0][2] = DecisionEnum.HIT;
        hardTotalsStrategies[1][2] = DecisionEnum.HIT;
        hardTotalsStrategies[2][2] = DecisionEnum.HIT;
        hardTotalsStrategies[3][2] = DecisionEnum.HIT;
        hardTotalsStrategies[5][2] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[4][2] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][2] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][2] = DecisionEnum.STAND;
        hardTotalsStrategies[8][2] = DecisionEnum.STAND;
        hardTotalsStrategies[9][2] = DecisionEnum.STAND;
        hardTotalsStrategies[10][2] = DecisionEnum.STAND;
        hardTotalsStrategies[11][2] = DecisionEnum.STAND;
        hardTotalsStrategies[12][2] = DecisionEnum.STAND;
        hardTotalsStrategies[13][2] = DecisionEnum.STAND;
        hardTotalsStrategies[14][2] = DecisionEnum.STAND;
        hardTotalsStrategies[15][2] = DecisionEnum.STAND;

        // Dealer 5
        hardTotalsStrategies[0][3] = DecisionEnum.HIT;
        hardTotalsStrategies[1][3] = DecisionEnum.HIT;
        hardTotalsStrategies[2][3] = DecisionEnum.HIT;
        hardTotalsStrategies[3][3] = DecisionEnum.HIT;
        hardTotalsStrategies[5][3] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[4][3] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][3] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][3] = DecisionEnum.STAND;
        hardTotalsStrategies[8][3] = DecisionEnum.STAND;
        hardTotalsStrategies[9][3] = DecisionEnum.STAND;
        hardTotalsStrategies[10][3] = DecisionEnum.STAND;
        hardTotalsStrategies[11][3] = DecisionEnum.STAND;
        hardTotalsStrategies[12][3] = DecisionEnum.STAND;
        hardTotalsStrategies[13][3] = DecisionEnum.STAND;
        hardTotalsStrategies[14][3] = DecisionEnum.STAND;
        hardTotalsStrategies[15][3] = DecisionEnum.STAND;

        // Dealer 6
        hardTotalsStrategies[0][4] = DecisionEnum.HIT;
        hardTotalsStrategies[1][4] = DecisionEnum.HIT;
        hardTotalsStrategies[2][4] = DecisionEnum.HIT;
        hardTotalsStrategies[3][4] = DecisionEnum.HIT;
        hardTotalsStrategies[5][4] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[4][4] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][4] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][4] = DecisionEnum.STAND;
        hardTotalsStrategies[8][4] = DecisionEnum.STAND;
        hardTotalsStrategies[9][4] = DecisionEnum.STAND;
        hardTotalsStrategies[10][4] = DecisionEnum.STAND;
        hardTotalsStrategies[11][4] = DecisionEnum.STAND;
        hardTotalsStrategies[12][4] = DecisionEnum.STAND;
        hardTotalsStrategies[13][4] = DecisionEnum.STAND;
        hardTotalsStrategies[14][4] = DecisionEnum.STAND;
        hardTotalsStrategies[15][4] = DecisionEnum.STAND;

        // Dealer 7
        hardTotalsStrategies[0][5] = DecisionEnum.HIT;
        hardTotalsStrategies[1][5] = DecisionEnum.HIT;
        hardTotalsStrategies[2][5] = DecisionEnum.HIT;
        hardTotalsStrategies[3][5] = DecisionEnum.HIT;
        hardTotalsStrategies[5][5] = DecisionEnum.HIT;
        hardTotalsStrategies[4][5] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][5] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][5] = DecisionEnum.HIT;
        hardTotalsStrategies[8][5] = DecisionEnum.HIT;
        hardTotalsStrategies[9][5] = DecisionEnum.HIT;
        hardTotalsStrategies[10][5] = DecisionEnum.HIT;
        hardTotalsStrategies[11][5] = DecisionEnum.HIT;
        hardTotalsStrategies[12][5] = DecisionEnum.STAND;
        hardTotalsStrategies[13][5] = DecisionEnum.STAND;
        hardTotalsStrategies[14][5] = DecisionEnum.STAND;
        hardTotalsStrategies[15][5] = DecisionEnum.STAND;

        // Dealer 8
        hardTotalsStrategies[0][6] = DecisionEnum.HIT;
        hardTotalsStrategies[1][6] = DecisionEnum.HIT;
        hardTotalsStrategies[2][6] = DecisionEnum.HIT;
        hardTotalsStrategies[3][6] = DecisionEnum.HIT;
        hardTotalsStrategies[5][6] = DecisionEnum.HIT;
        hardTotalsStrategies[4][6] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][6] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][6] = DecisionEnum.HIT;
        hardTotalsStrategies[8][6] = DecisionEnum.HIT;
        hardTotalsStrategies[9][6] = DecisionEnum.HIT;
        hardTotalsStrategies[10][6] = DecisionEnum.HIT;
        hardTotalsStrategies[11][6] = DecisionEnum.HIT;
        hardTotalsStrategies[12][6] = DecisionEnum.STAND;
        hardTotalsStrategies[13][6] = DecisionEnum.STAND;
        hardTotalsStrategies[14][6] = DecisionEnum.STAND;
        hardTotalsStrategies[15][6] = DecisionEnum.STAND;

        // Dealer 9
        hardTotalsStrategies[0][7] = DecisionEnum.HIT;
        hardTotalsStrategies[1][7] = DecisionEnum.HIT;
        hardTotalsStrategies[2][7] = DecisionEnum.HIT;
        hardTotalsStrategies[3][7] = DecisionEnum.HIT;
        hardTotalsStrategies[5][7] = DecisionEnum.HIT;
        hardTotalsStrategies[4][7] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[6][7] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][7] = DecisionEnum.HIT;
        hardTotalsStrategies[8][7] = DecisionEnum.HIT;
        hardTotalsStrategies[9][7] = DecisionEnum.HIT;
        hardTotalsStrategies[10][7] = DecisionEnum.HIT;
        hardTotalsStrategies[11][7] = DecisionEnum.SURRENDER;
        hardTotalsStrategies[12][7] = DecisionEnum.STAND;
        hardTotalsStrategies[13][7] = DecisionEnum.STAND;
        hardTotalsStrategies[14][7] = DecisionEnum.STAND;
        hardTotalsStrategies[15][7] = DecisionEnum.STAND;

        // Dealer 10
        hardTotalsStrategies[0][8] = DecisionEnum.HIT;
        hardTotalsStrategies[1][8] = DecisionEnum.HIT;
        hardTotalsStrategies[2][8] = DecisionEnum.HIT;
        hardTotalsStrategies[3][8] = DecisionEnum.HIT;
        hardTotalsStrategies[5][8] = DecisionEnum.HIT;
        hardTotalsStrategies[4][8] = DecisionEnum.HIT;
        hardTotalsStrategies[6][8] = DecisionEnum.DOUBLE_OR_HIT;
        hardTotalsStrategies[7][8] = DecisionEnum.HIT;
        hardTotalsStrategies[8][8] = DecisionEnum.HIT;
        hardTotalsStrategies[9][8] = DecisionEnum.HIT;
        hardTotalsStrategies[10][8] = DecisionEnum.SURRENDER;
        hardTotalsStrategies[11][8] = DecisionEnum.SURRENDER;
        hardTotalsStrategies[12][8] = DecisionEnum.STAND;
        hardTotalsStrategies[13][8] = DecisionEnum.STAND;
        hardTotalsStrategies[14][8] = DecisionEnum.STAND;
        hardTotalsStrategies[15][8] = DecisionEnum.STAND;

        // Dealer Ace
        hardTotalsStrategies[0][9] = DecisionEnum.HIT;
        hardTotalsStrategies[1][9] = DecisionEnum.HIT;
        hardTotalsStrategies[2][9] = DecisionEnum.HIT;
        hardTotalsStrategies[3][9] = DecisionEnum.HIT;
        hardTotalsStrategies[4][9] = DecisionEnum.HIT;
        hardTotalsStrategies[5][9] = DecisionEnum.HIT;
        hardTotalsStrategies[6][9] = DecisionEnum.HIT;
        hardTotalsStrategies[7][9] = DecisionEnum.HIT;
        hardTotalsStrategies[8][9] = DecisionEnum.HIT;
        hardTotalsStrategies[9][9] = DecisionEnum.HIT;
        hardTotalsStrategies[10][9] = DecisionEnum.HIT;
        hardTotalsStrategies[11][9] = DecisionEnum.SURRENDER;
        hardTotalsStrategies[12][9] = DecisionEnum.STAND;
        hardTotalsStrategies[13][9] = DecisionEnum.STAND;
        hardTotalsStrategies[14][9] = DecisionEnum.STAND;
        hardTotalsStrategies[15][9] = DecisionEnum.STAND;
    }

    /**
     * Populate a two dimensional array based on the players soft total and the
     * dealers faceup card.
     * 
     * To make the arrays as small as possible the value of the cards is mapped
     * to specific locations in the array.
     *  
     * A player's soft total is reduced by 13. For example, a hand with an Ace
     * and a 7 (total 18) will be mapped to location 5 in the first dimension.
     * 
     * For a dealer the value is reduced by 2. Ace is treated as a hard 11
     * before being reduced to 9.
     */
    private void populateSoftTotalsStrategies() {
        // Dealer 2
        softTotalsStrategies[0][0] = DecisionEnum.HIT;
        softTotalsStrategies[1][0] = DecisionEnum.HIT;
        softTotalsStrategies[2][0] = DecisionEnum.HIT;
        softTotalsStrategies[3][0] = DecisionEnum.HIT;
        softTotalsStrategies[4][0] = DecisionEnum.HIT;
        softTotalsStrategies[5][0] = DecisionEnum.STAND;
        softTotalsStrategies[6][0] = DecisionEnum.STAND;
        softTotalsStrategies[7][0] = DecisionEnum.STAND;

        // Dealer 3
        softTotalsStrategies[0][1] = DecisionEnum.HIT;
        softTotalsStrategies[1][1] = DecisionEnum.HIT;
        softTotalsStrategies[2][1] = DecisionEnum.HIT;
        softTotalsStrategies[3][1] = DecisionEnum.HIT;
        softTotalsStrategies[4][1] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[5][1] = DecisionEnum.DOUBLE_OR_STAND;
        softTotalsStrategies[6][1] = DecisionEnum.STAND;
        softTotalsStrategies[7][1] = DecisionEnum.STAND;

        // Dealer 4
        softTotalsStrategies[0][2] = DecisionEnum.HIT;
        softTotalsStrategies[1][2] = DecisionEnum.HIT;
        softTotalsStrategies[2][2] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[3][2] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[4][2] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[5][2] = DecisionEnum.DOUBLE_OR_STAND;
        softTotalsStrategies[6][2] = DecisionEnum.STAND;
        softTotalsStrategies[7][2] = DecisionEnum.STAND;

        // Dealer 5
        softTotalsStrategies[0][3] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[1][3] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[2][3] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[3][3] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[4][3] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[5][3] = DecisionEnum.DOUBLE_OR_STAND;
        softTotalsStrategies[6][3] = DecisionEnum.STAND;
        softTotalsStrategies[7][3] = DecisionEnum.STAND;

        // Dealer 6
        softTotalsStrategies[0][4] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[1][4] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[2][4] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[3][4] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[4][4] = DecisionEnum.DOUBLE_OR_HIT;
        softTotalsStrategies[5][4] = DecisionEnum.DOUBLE_OR_STAND;
        softTotalsStrategies[6][4] = DecisionEnum.STAND;
        softTotalsStrategies[7][4] = DecisionEnum.STAND;

        // Dealer 7
        softTotalsStrategies[0][5] = DecisionEnum.HIT;
        softTotalsStrategies[1][5] = DecisionEnum.HIT;
        softTotalsStrategies[2][5] = DecisionEnum.HIT;
        softTotalsStrategies[3][5] = DecisionEnum.HIT;
        softTotalsStrategies[4][5] = DecisionEnum.HIT;
        softTotalsStrategies[5][5] = DecisionEnum.STAND;
        softTotalsStrategies[6][5] = DecisionEnum.STAND;
        softTotalsStrategies[7][5] = DecisionEnum.STAND;

        // Dealer 8
        softTotalsStrategies[0][6] = DecisionEnum.HIT;
        softTotalsStrategies[1][6] = DecisionEnum.HIT;
        softTotalsStrategies[2][6] = DecisionEnum.HIT;
        softTotalsStrategies[3][6] = DecisionEnum.HIT;
        softTotalsStrategies[4][6] = DecisionEnum.HIT;
        softTotalsStrategies[5][6] = DecisionEnum.STAND;
        softTotalsStrategies[6][6] = DecisionEnum.STAND;
        softTotalsStrategies[7][6] = DecisionEnum.STAND;

        // Dealer 9
        softTotalsStrategies[0][7] = DecisionEnum.HIT;
        softTotalsStrategies[1][7] = DecisionEnum.HIT;
        softTotalsStrategies[2][7] = DecisionEnum.HIT;
        softTotalsStrategies[3][7] = DecisionEnum.HIT;
        softTotalsStrategies[4][7] = DecisionEnum.HIT;
        softTotalsStrategies[5][7] = DecisionEnum.HIT;
        softTotalsStrategies[6][7] = DecisionEnum.STAND;
        softTotalsStrategies[7][7] = DecisionEnum.STAND;

        // Dealer 10
        softTotalsStrategies[0][7] = DecisionEnum.HIT;
        softTotalsStrategies[1][7] = DecisionEnum.HIT;
        softTotalsStrategies[2][7] = DecisionEnum.HIT;
        softTotalsStrategies[3][7] = DecisionEnum.HIT;
        softTotalsStrategies[4][7] = DecisionEnum.HIT;
        softTotalsStrategies[5][7] = DecisionEnum.HIT;
        softTotalsStrategies[6][7] = DecisionEnum.STAND;
        softTotalsStrategies[7][7] = DecisionEnum.STAND;

        // Dealer Ace
        softTotalsStrategies[0][7] = DecisionEnum.HIT;
        softTotalsStrategies[1][7] = DecisionEnum.HIT;
        softTotalsStrategies[2][7] = DecisionEnum.HIT;
        softTotalsStrategies[3][7] = DecisionEnum.HIT;
        softTotalsStrategies[4][7] = DecisionEnum.HIT;
        softTotalsStrategies[5][7] = DecisionEnum.HIT;
        softTotalsStrategies[6][7] = DecisionEnum.STAND;
        softTotalsStrategies[7][7] = DecisionEnum.STAND;
    }

    /**
     * Populate a two dimensional array based on what pairs the player has and
     * the dealers faceup card.
     * 
     * To make the arrays as small as possible the value of the cards is mapped
     * to specific locations in the array.
     *  
     * The pair rank is reduced by 2. For example, a rank of 5 will be mapped
     * to location 3 in the first dimension.
     * 
     * For a dealer the value is reduced by 2. Ace is treated as a hard 11
     * before being reduced to 9.
     */
    private void populatePairsStrategies() {
        // Dealer 2
        pairsStrategies[0][0] = DecisionEnum.SPLIT;
        pairsStrategies[1][0] = DecisionEnum.SPLIT;
        pairsStrategies[2][0] = DecisionEnum.HIT;
        pairsStrategies[3][0] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][0] = DecisionEnum.SPLIT;
        pairsStrategies[5][0] = DecisionEnum.SPLIT;
        pairsStrategies[6][0] = DecisionEnum.SPLIT;
        pairsStrategies[7][0] = DecisionEnum.SPLIT;
        pairsStrategies[8][0] = DecisionEnum.STAND;
        pairsStrategies[9][0] = DecisionEnum.SPLIT;

        // Dealer 3
        pairsStrategies[0][1] = DecisionEnum.SPLIT;
        pairsStrategies[1][1] = DecisionEnum.SPLIT;
        pairsStrategies[2][1] = DecisionEnum.HIT;
        pairsStrategies[3][1] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][1] = DecisionEnum.SPLIT;
        pairsStrategies[5][1] = DecisionEnum.SPLIT;
        pairsStrategies[6][1] = DecisionEnum.SPLIT;
        pairsStrategies[7][1] = DecisionEnum.SPLIT;
        pairsStrategies[8][1] = DecisionEnum.STAND;
        pairsStrategies[9][1] = DecisionEnum.SPLIT;

        // Dealer 4
        pairsStrategies[0][2] = DecisionEnum.SPLIT;
        pairsStrategies[1][2] = DecisionEnum.SPLIT;
        pairsStrategies[2][2] = DecisionEnum.HIT;
        pairsStrategies[3][2] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][2] = DecisionEnum.SPLIT;
        pairsStrategies[5][2] = DecisionEnum.SPLIT;
        pairsStrategies[6][2] = DecisionEnum.SPLIT;
        pairsStrategies[7][2] = DecisionEnum.SPLIT;
        pairsStrategies[8][2] = DecisionEnum.STAND;
        pairsStrategies[9][2] = DecisionEnum.SPLIT;

        // Dealer 5
        pairsStrategies[0][3] = DecisionEnum.SPLIT;
        pairsStrategies[1][3] = DecisionEnum.SPLIT;
        pairsStrategies[2][3] = DecisionEnum.SPLIT;
        pairsStrategies[3][3] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][3] = DecisionEnum.SPLIT;
        pairsStrategies[5][3] = DecisionEnum.SPLIT;
        pairsStrategies[6][3] = DecisionEnum.SPLIT;
        pairsStrategies[7][3] = DecisionEnum.SPLIT;
        pairsStrategies[8][3] = DecisionEnum.STAND;
        pairsStrategies[9][3] = DecisionEnum.SPLIT;

        // Dealer 6
        pairsStrategies[0][4] = DecisionEnum.SPLIT;
        pairsStrategies[1][4] = DecisionEnum.SPLIT;
        pairsStrategies[2][4] = DecisionEnum.SPLIT;
        pairsStrategies[3][4] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][4] = DecisionEnum.SPLIT;
        pairsStrategies[5][4] = DecisionEnum.SPLIT;
        pairsStrategies[6][4] = DecisionEnum.SPLIT;
        pairsStrategies[7][4] = DecisionEnum.SPLIT;
        pairsStrategies[8][4] = DecisionEnum.STAND;
        pairsStrategies[9][4] = DecisionEnum.SPLIT;

        // Dealer 7
        pairsStrategies[0][5] = DecisionEnum.SPLIT;
        pairsStrategies[1][5] = DecisionEnum.SPLIT;
        pairsStrategies[2][5] = DecisionEnum.HIT;
        pairsStrategies[3][5] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][5] = DecisionEnum.HIT;
        pairsStrategies[5][5] = DecisionEnum.SPLIT;
        pairsStrategies[6][5] = DecisionEnum.SPLIT;
        pairsStrategies[7][5] = DecisionEnum.STAND;
        pairsStrategies[8][5] = DecisionEnum.STAND;
        pairsStrategies[9][5] = DecisionEnum.SPLIT;

        // Dealer 8
        pairsStrategies[0][6] = DecisionEnum.HIT;
        pairsStrategies[1][6] = DecisionEnum.HIT;
        pairsStrategies[2][6] = DecisionEnum.HIT;
        pairsStrategies[3][6] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][6] = DecisionEnum.HIT;
        pairsStrategies[5][6] = DecisionEnum.HIT;
        pairsStrategies[6][6] = DecisionEnum.SPLIT;
        pairsStrategies[7][6] = DecisionEnum.SPLIT;
        pairsStrategies[8][6] = DecisionEnum.STAND;
        pairsStrategies[9][6] = DecisionEnum.SPLIT;

        // Dealer 9
        pairsStrategies[0][7] = DecisionEnum.HIT;
        pairsStrategies[1][7] = DecisionEnum.HIT;
        pairsStrategies[2][7] = DecisionEnum.HIT;
        pairsStrategies[3][7] = DecisionEnum.DOUBLE_OR_HIT;
        pairsStrategies[4][7] = DecisionEnum.HIT;
        pairsStrategies[5][7] = DecisionEnum.HIT;
        pairsStrategies[6][7] = DecisionEnum.SPLIT;
        pairsStrategies[7][7] = DecisionEnum.SPLIT;
        pairsStrategies[8][7] = DecisionEnum.STAND;
        pairsStrategies[9][7] = DecisionEnum.SPLIT;

        // Dealer 10
        pairsStrategies[0][8] = DecisionEnum.HIT;
        pairsStrategies[1][8] = DecisionEnum.HIT;
        pairsStrategies[2][8] = DecisionEnum.HIT;
        pairsStrategies[3][8] = DecisionEnum.HIT;
        pairsStrategies[4][8] = DecisionEnum.HIT;
        pairsStrategies[5][8] = DecisionEnum.HIT;
        pairsStrategies[6][8] = DecisionEnum.SPLIT;
        pairsStrategies[7][8] = DecisionEnum.STAND;
        pairsStrategies[8][8] = DecisionEnum.STAND;
        pairsStrategies[9][8] = DecisionEnum.SPLIT;

        // Dealer Ace
        pairsStrategies[0][9] = DecisionEnum.HIT;
        pairsStrategies[1][9] = DecisionEnum.HIT;
        pairsStrategies[2][9] = DecisionEnum.HIT;
        pairsStrategies[3][9] = DecisionEnum.HIT;
        pairsStrategies[4][9] = DecisionEnum.HIT;
        pairsStrategies[5][9] = DecisionEnum.HIT;
        pairsStrategies[6][9] = DecisionEnum.SPLIT;
        pairsStrategies[7][9] = DecisionEnum.STAND;
        pairsStrategies[8][9] = DecisionEnum.STAND;
        pairsStrategies[9][9] = DecisionEnum.SPLIT;
    }

    @Override
    public String toString() {
        return "Basic Strategy"; 
    }

}
