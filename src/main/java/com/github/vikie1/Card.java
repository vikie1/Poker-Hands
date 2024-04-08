package com.github.vikie1;

import java.util.Objects;

public record Card(Character name, Character type) implements Comparable<Card>{ // Use record as variables should be immutable
    boolean isAPair(Card other){ // A pair is basically cards with same name
        return this.name().equals(other.name());
    }

    public boolean isRoyalCard() {
        return name().equals('T') || name().equals('J') || name().equals('K') || name().equals('Q') || name().equals('A');
    }

    /**
     * @param that the card to be compared with for ranking
     * @return int - 0 if they are equal, negative number if current is lower, positive if current is higher
     */
    @Override
    public int compareTo(Card that) {
        if (isAPair(that)) return 0;

        // if both are numbers then use natural order i.e 2 is greater than 3
        if (Character.isDigit(name()) && Character.isDigit(that.name())) {
            if (name() > that.name()) return 1;
            else return -1;
        }

        // digit cards are all smaller in value that character cards
        if (Character.isDigit(name()) && !Character.isDigit(that.name())) return -1;
        else if (!Character.isDigit(name()) && Character.isDigit(that.name())) return 1;

        return switch (name()) {
            case 'A' -> {
                if (that.name().equals('A')) yield 0;
                else yield 1;
            }
            case 'K' -> {
                if (that.name().equals('A')) yield -1;
                else if (that.name().equals('K')) yield 0;
                else yield 1;
            }
            case 'Q' -> {
                if (that.name().equals('A') || that.name().equals('K')) yield -1;
                else if (that.name().equals('Q')) yield 0;
                else yield 1;
            }
            case 'J' -> {
                if (that.name().equals('A') || that.name().equals('Q') || that.name().equals('K')) yield -1;
                else if (that.name().equals('J')) yield 0;
                else yield 1;
            }
            case 'T' -> { // 'X' was used to represent the 10 card
                if (
                        that.name().equals('A')
                                || that.name().equals('Q')
                                || that.name().equals('K') ||
                                that.name().equals('J')) yield -1;
                else if (that.name().equals('T')) yield 0;
                else yield 1;
            }
            default -> 0;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Card other)) return false;
        return name().equals(other.name()) && type().equals(other.type());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), type());
    }
}