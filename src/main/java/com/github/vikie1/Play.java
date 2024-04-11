package com.github.vikie1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
        else if (player1Rank != Player.HighestRank.ROYAL_FLUSH) { // if we are here then it's a tie, you cannot break a royal flush tie, but other ties can be broken by card values
            switch (player1Rank) {
                case FOUR_OF_A_KIND -> {
                    Card player1Quadruple = player1.getXCardsOfAKind().get(4).get(0);
                    Card player2Quadruple = player2.getXCardsOfAKind().get(4).get(0);

                    if (!isPlayerCardComparisonSuccessFull(player1Quadruple, player2Quadruple)){
                        Card lastOfPLayer1 = player1.getXCardsOfAKind().get(1).get(0);
                        Card lastOfPlayer2 = player2.getXCardsOfAKind().get(1).get(0);
                        isPlayerCardComparisonSuccessFull(lastOfPLayer1, lastOfPlayer2); // if it's not successful then it is a draw, ignore it
                    }
                }
                case FULL_HOUSE -> {
                    Card player1Triple = player1.getXCardsOfAKind().get(3).get(0);
                    Card player2Triple = player2.getXCardsOfAKind().get(3).get(0);

                    if (!isPlayerCardComparisonSuccessFull(player1Triple, player2Triple)){
                        Card player1Double = player1.getXCardsOfAKind().get(2).get(0);
                        Card player2Double = player2.getXCardsOfAKind().get(2).get(0);
                        isPlayerCardComparisonSuccessFull(player1Double, player2Double); // if it's not successful then it is a draw, ignore it
                    }
                }
                case TWO_PAIRS -> {
                    Card player1HigherPair = player1.getXCardsOfAKind().get(2).get(1); // there can't be more than 2 pairs, so no need for a loop just get index 0&1 in the list
                    Card player2HigherPair = player2.getXCardsOfAKind().get(2).get(1);

                    if (!isPlayerCardComparisonSuccessFull(player1HigherPair, player2HigherPair)){
                        Card player1OtherPair = player1.getXCardsOfAKind().get(2).get(0);
                        Card player2OtherPair = player2.getXCardsOfAKind().get(2).get(0);
                        if (!isPlayerCardComparisonSuccessFull(player1OtherPair, player2OtherPair)){
                            isPlayerCardComparisonSuccessFull(
                                    player1.getXCardsOfAKind().get(1).get(0),
                                    player2.getXCardsOfAKind().get(1).get(0)
                            );
                        }
                    }
                }
                case THREE_OF_A_KIND -> {
                    Card player1Triple = player1.getXCardsOfAKind().get(3).get(0);
                    Card player2Triple = player2.getXCardsOfAKind().get(3).get(0);

                    if (!isPlayerCardComparisonSuccessFull(player1Triple, player2Triple)){ // if triple is same cards
                        List<Card> remainingPlayer1Cards = player1.getXCardsOfAKind().get(1);
                        List<Card> remainingPlayer2Cards = player2.getXCardsOfAKind().get(1);

                        for (int i = remainingPlayer1Cards.size() - 1; i >= 0; i--) {
                            if (isPlayerCardComparisonSuccessFull(
                                    remainingPlayer1Cards.get(i),
                                    remainingPlayer2Cards.get(i)
                            )) break;
                        }
                    }
                }
                case A_PAIR -> {
                    Card player1Pair = player1.getXCardsOfAKind().get(2).get(0);
                    Card player2Pair = player2.getXCardsOfAKind().get(2).get(0);

                    if (!isPlayerCardComparisonSuccessFull(player1Pair, player2Pair)){ // if triple is same cards
                        List<Card> remainingPlayer1Cards = player1.getXCardsOfAKind().get(1);
                        List<Card> remainingPlayer2Cards = player2.getXCardsOfAKind().get(1);

                        for (int i = remainingPlayer1Cards.size() - 1; i >= 0; i--) {
                            if (isPlayerCardComparisonSuccessFull(
                                    remainingPlayer1Cards.get(i),
                                    remainingPlayer2Cards.get(i)
                            )) break;
                        }
                    }
                }
                default -> {
                    for (int i = player1.getCards().size() - 1; i >= 0; i--) {
                        if (isPlayerCardComparisonSuccessFull(
                                player1.getCards().get(i),
                                player2.getCards().get(i)
                        )) break;
                    }
                }
            }
        }

        // clear player cards for the next sequence
        player1.clearHand();
        player2.clearHand();
    }

    private static boolean isPlayerCardComparisonSuccessFull(Card card1, Card card2){
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