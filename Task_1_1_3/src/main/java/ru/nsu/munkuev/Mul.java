package ru.nsu.munkuev;

import java.util.Map;

/**
 * Произведение двух выражений
 */
public class Mul extends BinaryOperation {
    /**
     * @param left - левое выражение
     * @param right - правое выражение
     */
    public Mul(Expression left, Expression right){
        super(left,right);
    }

    @Override
    public double evaluate(Map <String, Double> variables){
        return left.evaluate(variables) * right.evaluate(variables);
    }

    @Override
    public String getOperatorSign(){
        return "*";
    }

    /**
     {@inheritDoc} — {@code (f * g)' = (f' * g + f * g')}
     */
    @Override
    public String derivative(String variable){
        return "(" + left.derivative(variable) + "*" + right  + " + " + left + "*" + right.derivative(variable) + ")";
    }
}
