package ru.nsu.munkuev;

import java.util.Map;

/**
 * Sample class to simulate 1.1 task functionality
 */
public class Sample {
    public static void main(String[] args){
        Expression e = new Sum(
                new Number(3),
                new Mul(
                        new Number(2),
                        new Variable("x")
                )
        );

        System.out.println(e.toString());

        Map<String, Double> variables = Map.of("x",1.0);

        double result = e.evaluate(variables);

        System.out.println(result);
    }
}
