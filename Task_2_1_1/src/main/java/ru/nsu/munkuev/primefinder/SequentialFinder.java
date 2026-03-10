package ru.nsu.munkuev.primefinder;


/**
 * Класс, реализующий поиск непростого числа последовательным способом
 */
public class SequentialFinder {
    /**
     * Метод, выполняющий поиск непростого числа
     * @param arr массив, в котором производится поиск
     * @return {@code true} если найдено непростое число, {@code false} иначе
     */
    public static boolean findNonPrime(int arr[]) {
        int arr_size = arr.length;

        for(int i = 0; i < arr_size; i++) {
            int temp = arr[i];

            if(PrimeFinderUtil.isNotPrime(temp)) {
                return true;
            }
        }

        return false;
    }

}
