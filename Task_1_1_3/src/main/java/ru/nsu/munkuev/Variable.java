package ru.nsu.munkuev;

import java.util.Map;

/**
 * Переменная выражения
 */
public class Variable implements Expression {
    private final String name;

    /** @param name имя переменной (нельзя {@code null} или пустую строку) */
    public Variable(String name){
        this.name = name;
    }

    /**
     * Возвращает значение из {@code variables} по ключу {@code name}.
     */
    @Override
    public double evaluate(Map <String, Double> variables){
        return variables.getOrDefault(name, 0.0);
    }

    @Override
    public String derivative(String variable) {
        if (variable.equals(name)) {
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public String toString(){
        return name;
    }
}
