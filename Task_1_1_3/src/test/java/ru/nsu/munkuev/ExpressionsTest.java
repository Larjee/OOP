package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionsTest {
    //====================== ЧИСЛА ======================
    @Test
    void numberEvaluateAndStringAndPrintAndDerivative(){
        Number n = new Number(3.5);
        assertEquals(3.5, n.evaluate(Map.of("x", 10.0)));
        assertEquals("3.5", n.toString());

        String out = captureStdout(n::print);
        assertEquals("3.5" + System.lineSeparator(), out);

        assertEquals("0", n.derivative("x"));
        assertEquals("0", n.derivative("y"));
    }


    //===================== ПЕРЕМЕННЫЕ ======================
    @Test
    void variableEvaluateWithValue(){
        Variable x = new Variable("x");
        assertEquals(42.0, x.evaluate(Map.of("x", 42.0)));
        assertEquals("x", x.toString());

        String out = captureStdout(x::print);
        assertEquals("x" + System.lineSeparator(), out);
    }

    @Test
    void variableEvaluateWithoutValueDefaultsToZero(){
        Variable y = new Variable("y");
        assertEquals(0.0, y.evaluate(Map.of()));
    }

    @Test
    void variableDerivative(){
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        assertEquals("1", x.derivative("x"));
        assertEquals("0", x.derivative("y"));
        assertEquals("0", y.derivative("x"));
    }


    //====================== СУММА ======================
    @Test
    void sumEvaluateToStringPrintDerivative(){
        Expression e = new Sum(
                new Number(2),
                new Variable("x")
        );
        assertEquals("(2.0 + x)", e.toString());

        String out = captureStdout(e::print);
        assertEquals("(2.0 + x)" + System.lineSeparator(), out);

        assertEquals(2.0 + 3.0, e.evaluate(Map.of("x", 3.0)));
        assertEquals("(0 + 1)", e.derivative("x"));
        assertEquals("(0 + 0)", e.derivative("y"));
    }


    //====================== РАЗНОСТЬ ======================
    @Test
    void subEvaluateToStringDerivative(){
        Expression e = new Sub(new Variable("x"), new Number(5));
        assertEquals("(x - 5.0)", e.toString());
        assertEquals(7.0 - 5.0, e.evaluate(Map.of("x", 7.0)));
        assertEquals("(1 - 0)", e.derivative("x"));
        assertEquals("(0 - 0)", e.derivative("z"));
    }


    //====================== УМНОЖЕНИЕ ======================
    @Test
    void mulEvaluateToStringDerivative(){
        Expression e = new Mul(
                new Variable("x"),
                new Sum(
                        new Number(2),
                        new Variable("y")
                )
        );
        assertEquals("(x*(2.0 + y))", e.toString());

        double v = e.evaluate(Map.of("x", 3.0, "y", 4.0));
        assertEquals(18.0, v);

        assertEquals("(1*(2.0 + y) + x*(0 + 0))", e.derivative("x"));

        assertEquals("(0*(2.0 + y) + x*(0 + 1))", e.derivative("y"));
        assertEquals("(0*(2.0 + y) + x*(0 + 0))", e.derivative("z"));
    }


    //====================== ДЕЛЕНИЕ ======================
    @Test
    void divEvaluateToStringDerivative(){
        Expression e = new Div(
                new Variable("x"),
                new Sum(
                        new Number(2),
                        new Variable("x")
                )
        );
        assertEquals("(x/(2.0 + x))", e.toString());

        double v = e.evaluate(Map.of("x", 2.0));
        assertEquals(0.5, v);

        assertEquals("(1*(2.0 + x) - x*(0 + 1))/((2.0 + x)*(2.0 + x))", e.derivative("x"));
        assertEquals("(0*(2.0 + x) - x*(0 + 0))/((2.0 + x)*(2.0 + x))", e.derivative("y"));
    }


    //====================== ДЕЛЕНИЕ НА НОЛЬ ======================
    @Test
    void divByZero() {
        Expression e = new Div(
                new Number(1),
                new Sub(
                        new Variable("x"),
                        new Variable("x")
                )
        );
        ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> e.evaluate(Map.of("x", 10.0))
        );

        assertEquals("Division by zero", ex.getMessage());
    }


    //====================== ДЛЯ ЗАХВАТА ПОТОКА ВЫВОДА ======================
    private static String captureStdout(Runnable action) {
        PrintStream old = System.out;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bout));
        try {
            action.run();
        } finally {
            System.setOut(old);
        }
        return bout.toString();
    }
}