package ru.nsu.munkuev;

import java.util.Map;

/**
 * Абстракция математического выражения.
 * Экземпляр можно вывести в строку, вычислить при подстановке значений переменных
 * и получить аналитическую производную по имени переменной.
 */
public abstract class Expression {
    /**
     * Вычисляет значение выражения при заданных значениях переменных.
     *
     * @param variables Отображение: имя переменной -> значение
     *
     * @return Числовое значение выражения
     */
    public abstract double evaluate(Map<String, Double> variables);

    /** @return строковое представление выражения с безопасными скобками */
    @Override public abstract String toString();

    /** Печатает строковое представление выражения в стандартный поток. */
    public abstract void print();

    /**
     * Возвращает строковое представление производной выражения по переменной.
     *
     * @param variable имя переменной, по которой берется производная
     * @return Строка с формулой производной (в инфиксной записи)
     */
    public abstract String derivative(String variable);
}
