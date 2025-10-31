package ru.nsu.munkuev;

import java.util.Map;

/**
 * Числовая константа.
 */
public class Number extends Expression{
    private final double value;

    /** @param value значение константы */
    public Number(double value){
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

    @Override
    public String toString(){
        return String.valueOf(value);
    }

    @Override
    public void print(){
        System.out.println(this.toString());
    }

    @Override
    public String derivative(String variable) {
        return "0";
    }
}
