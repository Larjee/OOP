package ru.nsu.munkuev;

import java.util.Map;

/**
 * Сумма двух выражений
 */
public class Sum extends BinaryOperation{
    /**
     * @param left - левое выражение
     * @param right - правое выражение
     */
    public Sum(Expression left, Expression right){
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variables){
        return left.evaluate(variables) + right.evaluate(variables);
    }

    @Override
    protected String getOperator(){
        return " + ";
    }

    /** {@inheritDoc} — {@code (f + g)' = (f' + g')} */
    @Override
    public String derivative(String variable){
        return "(" + left.derivative(variable) + " + " + right.derivative(variable) + ")";
    }
}
