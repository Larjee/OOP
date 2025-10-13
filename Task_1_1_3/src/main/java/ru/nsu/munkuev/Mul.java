package ru.nsu.munkuev;

import java.util.Map;

public class Mul extends BinaryOperation{
    public Mul(Expression left, Expression right){
        super(left,right);
    }

    @Override
    public double evaluate(Map <String, Double> variables){
        return left.evaluate(variables) * right.evaluate(variables);
    }

    @Override
    public String getOperator(){
        return "*";
    }
}
