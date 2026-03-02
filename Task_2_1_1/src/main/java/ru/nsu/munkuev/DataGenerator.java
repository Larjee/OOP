package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * <p>Класс для генерации тестовых данных содержащих только простые числа.</p>
 * <p>
 * Для генерации данных используются методы:
 * <ul>
 *     <li>{@link #smallSizeSmallNumbers()} для генерации маленького размера и маленьких чисел</li>
 *     <li>{@link #smallSizeBigNumbers()}  для генерации маленького размера и больших чисел</li>
 *     <li>{@link #bigSizeSmallNumbers()} для генерации большого размера и маленьких чисел</li>
 *     <li>{@link #bigSizeBigNumbers()} для генерации большого размера и больших чисел</li>
 * </ul>
 * </p>
 * <p> В данном классе под маленьким размером массива подразумевается 10000 элементов,
 *     под большим размером подразумевается 1000000 элементов
 * </p>
 *
 * Для генерации больших простых чисел используется массив {@link #BIG_PRIMES}. Из его элементов составляется
 * итоговый массив.
 * Для генерации маленьких простых чисел используется решето Эратосфена {@link #sieve(int)}.
 *
 */
public class DataGenerator {

    /**
     * Большие простые числа из которых составляются массивы smallSizeBigNumbers и bigSizeBigNumbers
     */
    private static final int[] BIG_PRIMES = {
            1_000_000_007,
            1_000_000_009,
            1_000_000_021,
            1_000_000_033,
            1_000_000_087
    };

    /**
     * Вызывает внутренний метод {@link #generateSmallPrimes(int)} с ограничением 10000
     * @return маленький массив с маленькими простыми числами
     */
    public static int[] smallSizeSmallNumbers() {
        return generateSmallPrimes(10_000);
    }

    /**
     * Вызывает внутренний метод {@link #generateSmallPrimes(int)} с ограничением 10000
     * @return маленький массив с большими простыми числами
     */
    public static int[] smallSizeBigNumbers() {
        return generateBigPrimes(10_000);
    }

    /**
     * Вызывает внутренний метод {@link #generateSmallPrimes(int)} с ограничением 1000000
     * @return большой массив с маленькими простыми числами
     */
    public static int[] bigSizeSmallNumbers() {
        return generateSmallPrimes(1_000_000);
    }

    /**
     * Вызывает внутренний метод {@link #generateBigPrimes(int)} с ограничением 1000000
     * @return большой массив с большими простыми числами
     */
    public static int[] bigSizeBigNumbers() {
        return generateBigPrimes(1_000_000);
    }


    /**
     * Метод генерирует массив маленьких простых чисел, используя решето Эратосфена
     * @param size размер выходного массива
     * @return массив маленьких простых чисел
     */
    private static int[] generateSmallPrimes(int size) {
        List<Integer> primes = sieve(10_000);
        Random random = new Random();

        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = primes.get(random.nextInt(primes.size()));
        }
        return arr;
    }

    /**
     * Метод генерирует массив больших простых чисел, используя
     * @param size размер выходного массива
     * @return массив больших простых чисел
     */
    private static int[] generateBigPrimes(int size) {
        Random random = new Random();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = BIG_PRIMES[random.nextInt(BIG_PRIMES.length)];
        }
        return arr;
    }

    /**
     * Решето эратосфена.
     * @param limit верхняя граница простых чисел
     * @return массив простых чисел
     */
    private static List<Integer> sieve(int limit) {
        boolean[] isComposite = new boolean[limit + 1];
        List<Integer> primes = new ArrayList<>();

        for (int i = 2; i * i <= limit; i++) {
            if (!isComposite[i]) {
                for (int j = i * i; j <= limit; j += i) {
                    isComposite[j] = true;
                }
            }
        }

        for (int i = 2; i <= limit; i++) {
            if (!isComposite[i]) {
                primes.add(i);
            }
        }

        return primes;
    }
}
