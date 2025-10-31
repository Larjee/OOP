package ru.nsu.munkuev;

import java.util.Map;

/**
 * Частное двух выражений
 */
public class Div extends BinaryOperation{
    /**
     * @param left - левое выражение
     * @param right - правое выражение
     */
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

    /** {@inheritDoc} — {@code (f / g)' = (f' * g - f * g') / g^2} */
    @Override
    public String derivative(String variable){
        return "(" + left.derivative(variable) + "*" + right  + " - " + left + "*" + right.derivative(variable) + ")" +
                "/" + "(" + right.toString() + "*" + right.toString() + ")";
    }
}
