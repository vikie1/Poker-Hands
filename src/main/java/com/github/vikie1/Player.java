package com.github.vikie1;

import java.util.*;

public class Player {
    private final List<Card> cards;
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
}