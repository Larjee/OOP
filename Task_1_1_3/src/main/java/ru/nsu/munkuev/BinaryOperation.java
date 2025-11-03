package ru.nsu.munkuev;

/**
 * Базовый класс для бинарных операций (сложение, вычитание, умножение, деление).
 * Обеспечивает хранение левого/правого операнда и общий формат {@link #toString()}.
 */
public abstract class BinaryOperation implements Expression{
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
    public String toString(){
        return "(" + left + getOperatorSign() + right + ")";
    }

    /**
     * @return Символ операции, например "+", "-", "*", "/"
     */
    protected abstract String getOperatorSign();
}
