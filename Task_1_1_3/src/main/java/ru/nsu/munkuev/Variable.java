package ru.nsu.munkuev;

import java.util.Map;

public class Variable extends Expression {
    private final String name;

    public Variable(String name){
        this.name = name;
    }

    @Override
    public double evaluate(Map <String, Double> variables){
        return variables.getOrDefault(name, 0.0);
    }

    @Override
    public String toString(){
        return name;
    }
}
