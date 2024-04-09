package com.github.vikie1;

import java.util.*;

public class Player {
    private final List<Card> cards;
    private Card winningCard = null;
    private int wins;
    private final String name;

    public Player(String name) {
        this.name = name;

        /*
         * Initialise the list and override the add method to sort app data added to it by default
         * sorting cards is meant to make it easier to retrieve card ranks
         * and allow pairs to be stored adjacent to each other
         **/
        cards = new ArrayList<>(){
            @Override
            public boolean add(Card card) {
                int index = Collections.binarySearch(this, card); // using binary search to determine the index
                if (index < 0) index = ~index;
                super.add(index, card);
                return true;
            }

            @Override
            public boolean addAll(Collection<? extends Card> c) {
                boolean index = super.addAll(c);
                this.sort(Comparator.naturalOrder());
                return index;
            }
        };
    }

    void incrementWins() {
        wins ++;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getWins() {
        return wins;
    }

    public String getName() {
        return name;
    }

    /**
     * @return Card - highest value card of the winning sequence
     */
    public Card getWinningCard() {
        if (winningCard == null) return cards.get(cards.size() - 1);
        return winningCard;
    }

    public void clearHand(){
        cards.clear();
        winningCard = null;
    }

    private boolean hasRoyalFlush() {
        if (!cards.get(0).name().equals('T') || !cards.get(cards.size() - 1).name().equals('A')) return false; // A royal flush should begin with 10 and end with A
        else {
            List<Card> flushCards = cards
                    .parallelStream()
                    .filter(Card::isRoyalCard)
                    .filter(card -> card.type().equals(cards.get(0).type())) // check for type similarity e.g All should be Clubs
                    .toList();
            return cards.size() == flushCards.size(); //if the size of both strings is the same then it meas nothing was filtered hence a royal flush
        }
    }

    private boolean hasFlush(){
        List<Card> flushCards = cards.parallelStream().filter(card -> card.type().equals(cards.get(0).type())).toList();
        return cards.size() == flushCards.size();
    }

    private boolean hasStraight(){
        for (int i = 0; i < cards.size(); i++) {
            Character currentCard = cards.get(i).name();
            char expectedNextCard;

            if (currentCard.equals('A') && i < cards.size() - 1) break; // if 'A' is not in the last index then it's not a flush

            if (Character.isDigit(currentCard) && !currentCard.equals('9')) expectedNextCard = (char) (currentCard + 1);
            else {
                expectedNextCard = switch (currentCard){
                    case '9' -> 'T';
                    case 'T' -> 'J';
                    case 'J' -> 'Q';
                    case 'Q' -> 'K';
                    case 'K' -> 'A';
                    default -> '0';
                };
            }
            if (i < cards.size() - 1 && !cards.get(i + 1).name().equals(expectedNextCard)) return false;
        }
        return true;
    }


    private Map<Character, Integer> xOfAKind(){
        int count = 1; // since a hand only have 5 cards, we can never get more than to different repeat kinds cards
        char repeatedCard = cards.get(0).name();
        Map<Character, Integer> similarCards = new HashMap<>();

        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).name().equals(repeatedCard)) { // increment count if card is repeated
                count ++;
                similarCards.put(cards.get(i).name(), count);
            }
            else {
                repeatedCard = cards.get(i).name();
                count = similarCards.getOrDefault(repeatedCard, 1);
            }
        }

        return similarCards;
    }

    HighestRank evalRank(){
        if (hasRoyalFlush()) return HighestRank.ROYAL_FLUSH;

        boolean straight = hasStraight();
        boolean flush = hasFlush();
        if (straight && flush) return HighestRank.STRAIGHT_FLUSH;

        Map<Character, Integer> xOfAKind = xOfAKind();
        if (xOfAKind.containsValue(4)) {
            winningCard = cards.get(3); // if there's 4 of a kind then any card at index 1 - 4 is of same kind
            return HighestRank.FOUR_OF_A_KIND;
        }
        if (xOfAKind.containsValue(3) && xOfAKind().containsValue(2)) {
            for (char highChar: xOfAKind.keySet()) {
                if (xOfAKind.get(highChar) == 3) {
                    winningCard = new Card(highChar, 'S');
                    break;
                }
            }
            return HighestRank.FULL_HOUSE;
        }

        if (flush) return HighestRank.FLUSH;
        if (straight) return HighestRank.STRAIGHT;

        if (xOfAKind.containsValue(3)) {
            winningCard = new Card(new ArrayList<>(xOfAKind.keySet()).get(0), 'S');
            return HighestRank.THREE_OF_A_KIND;
        }
        if (xOfAKind.size() == 2 && xOfAKind.values().containsAll(List.of(2, 2))) {
            Card firstPair = new Card(new ArrayList<>(xOfAKind.keySet()).get(0), 'S');
            Card secondPair = new Card(new ArrayList<>(xOfAKind.keySet()).get(1), 'S');
            int higherCard = firstPair.compareTo(secondPair);

            switch (higherCard){
                case 1 -> winningCard = firstPair;
                case -1 -> winningCard = secondPair;
            }
            return HighestRank.TWO_PAIRS;
        }
        if (xOfAKind.containsValue(2)) {
            winningCard = new Card(new ArrayList<>(xOfAKind.keySet()).get(0), 'S');
            return HighestRank.A_PAIR;
        }

        else return HighestRank.HIGHEST_CARD;
    }

    enum HighestRank{
        ROYAL_FLUSH(1),
        STRAIGHT_FLUSH(2),
        FOUR_OF_A_KIND(3),
        FULL_HOUSE(4),
        FLUSH(5),
        STRAIGHT(6),
        THREE_OF_A_KIND(7),
        TWO_PAIRS(8),
        A_PAIR(9),
        HIGHEST_CARD(10);

        private final int cardRank;
        HighestRank(int cardRank) {
           this.cardRank = cardRank;
        }

        public int getCardRank() {
            return cardRank;
        }
    }
}