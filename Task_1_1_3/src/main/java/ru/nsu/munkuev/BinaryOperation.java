package ru.nsu.munkuev;

import java.util.Map;

/**
 * Базовый класс для бинарных операций (сложение, вычитание, умножение, деление).
 * Обеспечивает хранение левого/правого операнда и общий формат {@link #toString()}.
 */
public abstract class BinaryOperation extends Expression{
    /** Левый операнд. */
    protected final Expression left;
    /** Правый операнд. */
    protected final Expression right;

    /**
     * Создаёт бинарную операцию.
     *
     * @param left левый операнд
     * @param right правый операнд
     */
    public BinaryOperation(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public abstract double evaluate(Map <String, Double> variables);

    /** @return Символ операции, например "+", "-", "*", "/" */
    protected abstract String getOperator();

    @Override
    public String toString(){
        return "(" + left + getOperator() + right + ")";
    }

    @Override
    public void print(){
        System.out.println( "(" + left.toString() + getOperator() + right.toString() + ")");
    }

    @Override
    public abstract String derivative(String variable);
}
