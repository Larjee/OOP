package ru.nsu.munkuev;

public class Dealer extends Participant{
    public Dealer(String name){
        this.name = name;
        this.hand = new Hand();
    }
}
