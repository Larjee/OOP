package ru.nsu.munkuev;

import java.util.Map;

/**
 * Интерфейс для математического выражения. Позволяет вычислить выражение по
 * заданному набору переменных, выислить производную выражения и выдать ее в виде строки.
 * Так же позволяет напечатать выражение в виде строки используя toString().
 * Чтобы создать выражение нужно использовать наследников этого класса.
 * Add, Sub, Mul, Div, Number, Variable.
 * Например multiplication = new Mul(new Number("5"), new Add(new Variable("x"), new Variable("y"))) // multiplication = 5 * (x + y);
 * Чтобы добавить свой экземпляр выражения необходимо реализовать методы
 * evaluate, deriavative и toString
 * В случае ошибки класс должен выбросить {@link ArithmeticException}
 */
public interface Expression {
    /**
     * Вычисляет значение выражения при заданных значениях переменных.
     *
     * @param variables Отображение: имя переменной -> значение
     *
     * @return Числовое значение выражения
     */
    double evaluate(Map<String, Double> variables);

    /**
     * Возвращает строковое представление производной выражения по переменной.
     *
     * @param variable имя переменной, по которой берется производная
     * @return Строка с формулой производной (в инфиксной записи)
     */
    String derivative(String variable);
}
