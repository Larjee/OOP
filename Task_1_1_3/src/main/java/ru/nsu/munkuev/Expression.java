package ru.nsu.munkuev;

import java.util.Map;

public abstract class Expression {
    //Метод вычисляющий значения переменных по переданному словарю
    public abstract double evaluate(Map<String, Double> variables);

    @Override
    public abstract String toString();

}
