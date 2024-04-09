package com.github.vikie1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PlayTest {
    @Test
    void main_checkForFileIOException_appStartsSuccessfully() throws IOException {
        Play.main(new String[]{"no args"});
    }

    @Test
    void extractCardsFromLine_playerValuesPopulates_playersShouldHave5CardsEach() {
        String line = "5H KS 9C 7D 9H 8D 3S 5D 5C AH";
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        player1.addCard(new Card('5', 'H'));
        player1.addCard(new Card('K', 'S'));
        player1.addCard(new Card('9', 'C'));
        player1.addCard(new Card('7', 'D'));
        player1.addCard(new Card('9', 'H'));
        player2.addCard(new Card('8', 'D'));
        player2.addCard(new Card('3', 'S'));
        player2.addCard(new Card('5', 'D'));
        player2.addCard(new Card('5', 'C'));
        player2.addCard(new Card('A', 'H'));

        Play.extractCardsFromLine(line);
        List<Card> expectedPlayer1Cards = player1.getCards();
        List<Card> expectedPlayer2Cards = player2.getCards();
        List<Card> actualPlayer1Cards = Play.player1.getCards();
        List<Card> actualPlayer2Cards = Play.player2.getCards();

        assertTrue(expectedPlayer1Cards.size() == 5 && expectedPlayer2Cards.size() == 5, "Both players should have 5 cards");
        assertTrue(actualPlayer1Cards.containsAll(expectedPlayer1Cards), "Both lists should have the same elements");
        assertTrue(actualPlayer2Cards.containsAll(expectedPlayer2Cards), "Both lists should have the same elements");
    }

    @Test
    void evalLineWinner_testForHighestCard_playerWithAceShouldWin(){
        String line = "4D 6S AH 9H QC 3D 6D 7H TD QS";
        Play.evalLineWinner(line);
        int expectedPlayer1result = 1;
        int actualPlayer1result = Play.player1.getWins();

        assertEquals(expectedPlayer1result, actualPlayer1result);
    }

    @Test
    void evalLineWinner_testForRoyalFlush_playerWithRoyalFlushShouldWin(){
        String line = "JD KD TD AD QD JD QD AH TD QS";
        Play.evalLineWinner(line);
        int expectedPlayer1result = 1;
        int actualPlayer1result = Play.player1.getWins();

        assertEquals(expectedPlayer1result, actualPlayer1result, "Player with a royal flush wins");
    }

    @Test
    void evalLineWinner_testStraightFlush_playerWithStraightFlushShouldWin(){
        String line = "4H 7H 9H 6H 5H 7D 9S TH QS 8C";
        Play.evalLineWinner(line);
        int expectedPlayer1result = 1;
        int actualPlayer1result = Play.player1.getWins();

        assertEquals(expectedPlayer1result, actualPlayer1result, "Player with a straight flush wins");
    }

    @Test
    void evalLineWinner_test4OfAKind_playersWith4OfAKindWins(){
        String line = "3D 5H 3S 3C 3H AH TS KC 2S 9D";
        Play.evalLineWinner(line);
        int expectedPlayer1result = 1;
        int actualPlayer1result = Play.player1.getWins();

        assertEquals(expectedPlayer1result, actualPlayer1result, "Player with a 4 of a kind wins");
    }

    @Test
    void evalLineWinner_testForFlush_playerWithFlushShouldWin(){
        String line = "3D 6D 7D TD QD 5H 5C 6S 7S KD ";
        Play.evalLineWinner(line);
        int expectedPlayer1result = 1;
        int actualPlayer1result = Play.player1.getWins();

        assertEquals(expectedPlayer1result, actualPlayer1result, "Player with a flush wins");
    }

    @Test
    void evalLineWinner_testForPairs_playerWithHighestValuePairShouldWin(){
        String line = "5H 5C 6S 7S KD 2C 3S 8S 8D TD";
        Play.evalLineWinner(line);
        int expectedPlayer2result = 1;
        int actualPlayer2result = Play.player2.getWins();

        assertEquals(expectedPlayer2result, actualPlayer2result, "Player with a higher value pair wins");
    }
}