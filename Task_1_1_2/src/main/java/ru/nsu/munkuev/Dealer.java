package ru.nsu.munkuev;

public class Dealer extends Participant{
    public Dealer(String name){
        this.setName(name);
        this.setHand(new Hand());
    }
}
