package com.github.vikie1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Play {
    static final Player player1 = new Player("Player 1");
    static final Player player2 = new Player("Player 2");

    public static void main(String[] args) throws IOException {
        try(Stream<String> hands = Files.lines(Paths.get("src/main/resources/poker.txt"))) {
            hands.forEach(Play::evalLineWinner);
        }
        System.out.println(player1.getName() + " has " + player1.getWins() + " wins!");
    }

    static void extractCardsFromLine(String line) {
        String[] individualCardValues = line.split(" ");
        for (int i = 0; i < individualCardValues.length; i++) {
            Card currentCard = new Card( // the problem states that all characters are valid so no need for multiple checks
                    individualCardValues[i].charAt(0),
                    individualCardValues[i].charAt(1)
            );

            // first five cards belong to player 1, the rest to player 2
            if (i < 5) player1.addCard(currentCard);
            else player2.addCard(currentCard);
        }
    }

    static void evalLineWinner(String line){
        extractCardsFromLine(line);

        Player.HighestRank player1Rank = player1.evalRank();
        Player.HighestRank player2Rank = player2.evalRank();

        if (player1Rank.getCardRank() < player2Rank.getCardRank()) player1.incrementWins();
        else if (player2Rank.getCardRank() < player1Rank.getCardRank()) player2.incrementWins();
        else if (player1Rank != Player.HighestRank.ROYAL_FLUSH) { // if we are here then it's a tie, you can not break a royal flush tie but other ties can be broken by card values
            Card player1WiningCard = player1.getWinningCard();
            Card player2WiningCard = player2.getWinningCard();
            if (!isPlayersCardComparisonSuccessFull(player1WiningCard, player2WiningCard)) {
                for (int i = player1.getCards().size() - 1; i >= 0; i--) {
                    if (isPlayersCardComparisonSuccessFull(
                            player1.getCards().get(i),
                            player2.getCards().get(i)
                    )) break;
                }
            }
        }

        // clear player cards for next sequence
        player1.clearHand();
        player2.clearHand();
    }

    private static boolean isPlayersCardComparisonSuccessFull(Card card1, Card card2){
        int cardsComparison = card1.compareTo(card2);


        return switch (cardsComparison){
            case 1 -> {
                player1.incrementWins();
                yield true;
            }
            case -1 -> {
                player2.incrementWins();
                yield true;
            }
            default -> false;
        };
    }
}