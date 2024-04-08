package com.github.vikie1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {
    @Test
    void compareTo_testForHigherCard_theKCardShouldBeGreaterThanThe8Card(){
        Card cardToCompare = new Card('8', 'H');
        Card givenCard = new Card('K', 'D');

        int expectedValue = 1;
        int actualValue = givenCard.compareTo(cardToCompare);

        assertEquals(expectedValue, actualValue, "The 'K' card should be higher than the '8' card");
    }

    @Test
    void compareTo_testForSameValueCard_theTHCardShouldHaveSameValueWithTheTDCard(){
        Card cardToCompare = new Card('T', 'H');
        Card givenCard = new Card('T', 'D');

        int expectedValue = 0;
        int actualValue = givenCard.compareTo(cardToCompare);

        assertEquals(expectedValue, actualValue, "Same cards with different suits are of the same value");
    }

    @Test
    void compareTo_testLowerCard_the7CardShouldBeLowerThanTheQCard(){
        Card cardToCompare = new Card('Q', 'S');
        Card givenCard = new Card('7', 'C');

        int expectedValue = -1;
        int actualValue = givenCard.compareTo(cardToCompare);

        assertEquals(expectedValue, actualValue, "The 'Q' card should be higher than the '7' card");
    }
}
