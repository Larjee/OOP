package ru.nsu.munkuev.primefinder;

import java.util.Arrays;

/**
 * Класс реализующий поиск непростого числа используя ParallelStream.
 */
public class ParallelStreamFinder {
    /**
     * Метод выполняющий поиск непростого числа через ParallelStream
     * @param arr массив, в котором производится поиск
     * @return {@code true} если найдено непростое число, {@code false} иначе
     */
    public static boolean findNonPrime(int[] arr) {
        return Arrays.stream(arr).parallel().anyMatch(x -> PrimeFinderUtil.isNotPrime(x));
    }
}
