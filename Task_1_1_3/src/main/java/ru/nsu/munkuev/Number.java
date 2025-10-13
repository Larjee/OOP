package ru.nsu.munkuev;

import java.util.Map;

public class Number extends Expression{
    private final double value;

    public Number(double value){
        this.value = value;
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        return 0;
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }
}
