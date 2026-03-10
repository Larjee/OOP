package ru.nsu.munkuev.util;

/**
 * Вспомогательный класс для поиска непростых чисел
 */
public final class PrimeFinderUtil {

    private PrimeFinderUtil() {}

    /**
     * Метод проверяющий число на его не простоту
     * @param n число
     * @return {@code true} если число непростое, {@code false} иначе
     */
    public static boolean isNotPrime(int n) {
        if(n < 2) {
            return true;
        }

        if(n == 2) {
            return false;
        }

        if(n % 2 == 0) {
            return true;
        }

        int int_sqrt = (int) Math.sqrt(n);

        for(int divisor = 3; divisor <= int_sqrt; divisor+=2) {
            if(n % divisor == 0) {
                return true;
            }
        }
        return false;
    }
}
