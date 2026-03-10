package ru.nsu.munkuev.threads;

import ru.nsu.munkuev.util.PrimeFinderUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class ParellelFinderTask implements Runnable {

    /**
     * Индекс начала подмассива для поиска непростого числа
     */
    private final int begin;

    /**
     * Индекс конца подмассива для поиска непростого числа
     */
    private final int end;

    /**
     * Массив в котором производится поиск непростого числа
     */
    private final int arr[];

    /**
     * Флаг, указывающий на необходимость прекратить поиск
     */
    private final AtomicBoolean stop;


    /**
     * Конструктор для указания начала и конца подмассива для поиска непростого числа
     * @param begin Индекс начлаа подмассива для поиска непростого числа
     * @param end Индекс конца подмассива для поиска непростого числа(невключительно)
     * @param arr Массив в котором производится поиск непростого числа
     * @param stop Индикатор остановки поиска
     */
    public ParellelFinderTask(int begin, int end, int arr[], AtomicBoolean stop) {
        this.begin = begin;
        this.end = end;
        this.arr = arr;
        this.stop = stop;
    }


    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();

        for(int i = begin; i < end; i++) {
            if(stop.get()) {
                return;
            }

            int temp = arr[i];
            if(PrimeFinderUtil.isNotPrime(temp)) {
                stop.set(true);
                return;
            }
        }

    }
}
