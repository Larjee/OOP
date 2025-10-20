package ru.nsu.munkuev;

import java.util.List;
import java.util.Objects;

public class CardUtil {
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final String JAKE = "J";
    private static final String QUEEN = "Q";
    private static final String KING = "K";
    private static final String ACE = "A";

    public static int calcSum(List<Card> hand) {
        int summ = 0;

        // Проверим есть ли туз в колоде
        boolean aceFlag = false;
        for(Card card: hand){
            int temp_value;
            if(Objects.equals(card.getRank(), ACE)){
                aceFlag = true;
            }
            summ += getIntValue(card);
        }

        if(aceFlag && summ>21){
            summ -=10;
        }

        return summ;
    }

    private static int getIntValue(Card card){
        int value = 0;
        String rank = card.getRank();
        if(Objects.equals(rank, JAKE) || Objects.equals(rank, QUEEN) || Objects.equals(rank, KING)){
            value = 10;
        }
        else if(Objects.equals(rank, ACE)){
            value = 11;
        }
        else{
            for(int i = 2; i<=10; i++){
                if(Objects.equals(rank, RANKS[i-2])){
                    value = i;
                }
            }
        }

        return value;
    }
}
