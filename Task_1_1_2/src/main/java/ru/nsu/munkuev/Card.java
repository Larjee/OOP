package ru.nsu.munkuev;

public class Card {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit(){
        return this.suit;
    }

    public String getRank(){
        return this.rank;
    }
}
