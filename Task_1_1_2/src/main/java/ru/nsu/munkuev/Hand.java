package ru.nsu.munkuev;


import java.util.List;
import java.util.ArrayList;


public class Hand {
    public final List<Card> hand = new ArrayList<>();

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
