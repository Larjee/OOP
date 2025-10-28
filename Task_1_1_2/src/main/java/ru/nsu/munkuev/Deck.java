package ru.nsu.munkuev;


import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Deck {
    private static final String[] SUITS = {"♥", "♦", "♣", "♠"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    private List<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck(){
        for (String suit: SUITS){
            for(String rank: RANKS){
                Card newCard = new Card(suit, rank);
                deck.add(newCard);
            }
        }
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Card getCard(){
        if (deck.isEmpty()){
            System.out.printf("%s", "\nDeck is empty!\nCreating new deck...\n");
            initializeDeck();
            shuffle();
        }

        return deck.remove(0);
    }
}
