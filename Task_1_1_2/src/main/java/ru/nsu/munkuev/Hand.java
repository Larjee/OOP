package ru.nsu.munkuev;


import java.util.List;
import java.util.ArrayList;


public class Hand {
    private final List<Card> hand = new ArrayList<>();

    public List<Card> getHand(){
        return hand;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public int getSum(){
        return CardUtil.calcSum(hand);
    }

    public void clear(){
        hand.clear();
    }
}
