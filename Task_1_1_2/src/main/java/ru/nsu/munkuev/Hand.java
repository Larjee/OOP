package ru.nsu.munkuev;


import java.util.List;
import java.util.ArrayList;


public class Hand {
    public List<Card> hand = new ArrayList<>();

    public void addCard(Card card){
        hand.add(card);
    }

    public int getSumm(){

        int summ = 0;

        // Проверим есть ли туз в колоде
        boolean aceFlag = false;
        for(Card card: hand){
            int temp_value;
            if(card.rank == "A"){
                aceFlag = true;
            }
            summ += getIntValue(card);
        }

        if(aceFlag && summ>21){
            summ -=10;
        }

        return summ;
    }

    String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    public int getIntValue(Card card){
        int value = 0;

        if(card.rank == "J" || card.rank == "Q" || card.rank == "K"){
            value = 10;
        }
        else if(card.rank == "A"){
            value = 11;
        }
        else{
            for(int i = 2; i<=10; i++){
                if(card.rank == ranks[i-2]){
                    value = i;
                }
            }
        }

        return value;

    }

    public void clear(){
        hand.clear();
    }
}
