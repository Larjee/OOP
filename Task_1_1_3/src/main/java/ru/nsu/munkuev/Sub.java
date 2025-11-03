package ru.nsu.munkuev;

import java.util.Map;

/**
 * Разность двух выражений
 */
public class Sub extends BinaryOperation {
    /**
     * @param left - левое выражение
     * @param right - правое выражение
     */
    public Sub(Expression left, Expression right){
        super(left,right);
    }

    @Override
    public double evaluate(Map <String, Double> variables){
        return left.evaluate(variables) - right.evaluate(variables);
    }

    @Override
    public String getOperatorSign(){
        return " - ";
    }

    /**
     {@inheritDoc} — {@code (f - g)' = (f' - g')}
     */
    @Override
    public String derivative(String variable){
        return "(" + left.derivative(variable) + " - " + right.derivative(variable) + ")";
    }
}
