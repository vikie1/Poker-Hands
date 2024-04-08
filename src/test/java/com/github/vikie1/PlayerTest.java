package com.github.vikie1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest { // other methods in this class are tested in PlayTest when evaluating for winner in different scenarios
    @Test
    void addCard_testForSort_theCardShouldBeSortedASCUponInsertInList(){
        Card card1 = new Card('A', 'H');
        Card card2 = new Card('T', 'H');
        Card card3 = new Card('K', 'H');
        Card card4 = new Card('2', 'H');
        Card card5 = new Card('8', 'H');
        Player player = new Player("Victor");
        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card4);
        player.addCard(card3);
        player.addCard(card5);

        assertEquals(0, player.getCards().indexOf(card4), "2 should be at index 0");
        assertEquals(4, player.getCards().indexOf(card1), "Ace should be the last card in the list (index 4)");
        assertEquals(2, player.getCards().indexOf(card2), "T should be after 2 and 8 (index 2)");
    }
}
