package ru.nsu.munkuev;

public class Player extends Participant{
    public Player(String name){
        this.name = name;
        this.hand = new Hand();
    }
}
