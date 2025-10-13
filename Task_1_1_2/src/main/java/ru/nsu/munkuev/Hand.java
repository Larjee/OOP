package ru.nsu.munkuev;


import java.util.List;
import java.util.ArrayList;


public class Hand {
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

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
            if(card.getRank() == "A"){
                aceFlag = true;
            }
            summ += getIntValue(card);
        }

        if(aceFlag && summ>21){
            summ -=10;
        }

        return summ;
    }


    public int getIntValue(Card card){
        int value = 0;
        String rank = card.getRank();
        if(rank == "J" || rank == "Q" || rank == "K"){
            value = 10;
        }
        else if(rank == "A"){
            value = 11;
        }
        else{
            for(int i = 2; i<=10; i++){
                if(rank == RANKS[i-2]){
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
