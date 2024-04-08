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
            hands.forEach(Play::extractCardsFromLine);
        }
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
}