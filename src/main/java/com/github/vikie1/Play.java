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
            hands.forEach(System.out::println);
        }
    }
}