package ru.nsu.munkuev;

import java.util.Map;

public class Div extends BinaryOperation{
    public Div(Expression left, Expression right){
        super(left,right);
    }

    @Override
    public double evaluate(Map <String, Double> variables){
        double divisor = right.evaluate(variables);
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left.evaluate(variables) / divisor;
    }

    @Override
    public String getOperator(){
        return "/";
    }

}
