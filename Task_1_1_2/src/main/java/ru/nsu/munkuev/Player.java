package ru.nsu.munkuev;

public class Player extends Participant{
    public Player(String name){
        this.setName(name);
        this.setHand(new Hand());
    }
}
