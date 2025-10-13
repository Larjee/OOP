package ru.nsu.munkuev;

import java.util.Map;

public abstract class BinaryOperation extends Expression{
    protected final Expression left;
    protected final Expression right;

    public BinaryOperation(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public abstract double evaluate(Map <String, Double> variables);

    protected abstract String getOperator();

    @Override
    public String toString(){
        return "(" + left + getOperator() + right + ")";
    }

}
