package com._17od.blackjack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com._17od.blackjack.Card.Rank;
import com._17od.blackjack.Card.Suit;
import com._17od.blackjack.strategy.BasicStrategyBuilder;
import com._17od.blackjack.strategy.HiLoStrategyBuilder;
import com._17od.blackjack.strategy.Strategy;
import com._17od.blackjack.strategy.StrategyBuilder;

/**
 * This class is used to test various scenarios. It uses a properties file to
 * provide a snapshot of a round. This information is fed into a strategy that
 * gives back the next move the player should take.
 *
 * @author Adrian Smith
 */
public class ScenarioTester {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties scenario = new Properties();
        
        if (args.length < 2) {
            System.err.println("Usage: StrategyTester <\"basic\" or \"hilo\"> <scenario file>");
            System.exit(1);
        }

        // Read in the scenario properties
        String strategyName = args[0];
        scenario.load(new FileInputStream(args[1]));

        // Create the various objects that participate in the game
        Game game = createGame(scenario);
        Player player = new Player();
        Player dummyPlayer = new Player();
        Strategy strategy = createStrategy(strategyName, game, player);

        // Deal the cards already dealt to a dummy player
        String cardsAlreadyDealt = scenario.getProperty("cardsAlreadyDealt");
        if (cardsAlreadyDealt != null) {
            dealCards(game, dummyPlayer, cardsAlreadyDealt);
        }

        // The first step it to determine how much to bet
        System.out.println("Amount To Bet: " + strategy.amountToBet() + "\n");

        // Deal out the cards to the player and the dealer
        String playerHand = scenario.getProperty("playerHand");
        if (playerHand == null) {
            throw new RuntimeException("A 'playerHand' must be provided in the scenario file");
        }
        dealCards(game, player, playerHand);
        
        String handCameFromSplit = scenario.getProperty("handCameFromSplit");
        if (handCameFromSplit != null) {
            player.getHand().setCameFromSplit(Boolean.parseBoolean(handCameFromSplit));
        }
        
        String dealerCard = scenario.getProperty("dealerCard");
        if (dealerCard == null) {
            throw new RuntimeException("A 'dealerCard' must be provided in the scenario file");
        }
        dealCards(game, game.getDealer(), dealerCard);

        // Print out the game rules and hands to give some context
        System.out.println("Strategy: " + player.getStrategy().toString());
        System.out.println("Player Hand: " + player.getHand().toString());
        System.out.println("Dealer Hand: " + game.getDealer().getHand().toString());
        System.out.println("Cards Already Dealt: " + dummyPlayer.getHand().getCards());
        System.out.println("Rules: " + game.getRules().toString());

        // Call the strategy to find out what decision the player should make
        // next
        System.out.println("\nNext Move: " + strategy.whatNext().toString());
    }

    private static Game createGame(Properties properties) {
        Game game = new Game(new GameRules());
        String minimumBet = properties.getProperty("minimumBet");
        if (minimumBet == null) {
            throw new RuntimeException("A 'minimumBet' must be provided in the scenario file");
        }
        game.getRules().setMinimumBet(Integer.parseInt(minimumBet));
        return game;
    }

    private static void dealCards(Game game, Player player, String cards) {
        Card[] cardObjects = parseCards(cards);
        for (Card card : cardObjects) {
            game.deal(card, player);
        }
    }

    /**
     * Create the requested strategy
     */
    private static Strategy createStrategy(String strategyName, Game game, Player player) {
        StrategyBuilder builder = null;

        if (strategyName.equals("basic")) {
            builder = new BasicStrategyBuilder();
        } else if (strategyName.equals("hilo")) {
            builder = new HiLoStrategyBuilder();
        } else {
            throw new RuntimeException("Unknown strategy " + strategyName);
        }

        return builder.create(game, player);
    }

    /**
     * Given a comma separated list of cards create a Card array. Each card is
     * given using a rank and suit, The ranks are 1-10, [K]ing, [Q]ueen, [J]ack.
     * The suits are [D]iamonds, [H]earts, [S]pades, [C]lubs.
     * 
     * @param multipleCardsString A comma separated list of cards
     * @return
     */
    private static Card[] parseCards(String multipleCardsString) {
        String[] cardsString = multipleCardsString.split(",");

        Card[] cardsArray = new Card[cardsString.length];

        // Loop through each card string and parse it to create a Card object
        for (int i=0; i<cardsString.length; i++) {
            Pattern pattern = Pattern.compile("(\\d+|[akqj])([dhcs])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(cardsString[i]);
            if (matcher.find()) {
                String rank = matcher.group(1);
                String suit = matcher.group(2);
                Card card = new Card(determineRank(rank), determineSuit(suit));
                cardsArray[i] = card;
            } else {
                throw new RuntimeException("Invalid card pattern " + cardsString[i]);
            }
        }

        return cardsArray;
    }

    private static Rank determineRank(String rank) {
        Rank rankEnum = null;

        if (rank.equals("A")) {
            rankEnum = Rank.ACE;
        } else if (rank.equals("2")) {
            rankEnum = Rank.TWO;
        } else if (rank.equals("3")) {
            rankEnum = Rank.THREE;
        } else if (rank.equals("4")) {
            rankEnum = Rank.FOUR;
        } else if (rank.equals("5")) {
            rankEnum = Rank.FIVE;
        } else if (rank.equals("6")) {
            rankEnum = Rank.SIX;
        } else if (rank.equals("7")) {
            rankEnum = Rank.SEVEN;
        } else if (rank.equals("8")) {
            rankEnum = Rank.EIGHT;
        } else if (rank.equals("9")) {
            rankEnum = Rank.NINE;
        } else if (rank.equals("10")) {
            rankEnum = Rank.TEN;
        } else if (rank.equals("K")) {
            rankEnum = Rank.KING;
        } else if (rank.equals("Q")) {
            rankEnum = Rank.QUEEN;
        } else if (rank.equals("J")) {
            rankEnum = Rank.JACK;
        } else {
            throw new RuntimeException("Invalid rank " + rank);
        }

        return rankEnum;
    }
    
    private static Suit determineSuit(String suit) {
        Suit suitEnum = null;

        switch (suit.charAt(0)) {
        case 'D':
            suitEnum = Suit.DIAMONDS;
            break;
        case 'H':
            suitEnum = Suit.HEARTS;
            break;
        case 'S':
            suitEnum = Suit.SPADES;
            break;
        case 'C':
            suitEnum = Suit.CLUBS;
            break;
        default:
            throw new RuntimeException("Invalid suit " + suit);
        }

        return suitEnum;
    }

}

