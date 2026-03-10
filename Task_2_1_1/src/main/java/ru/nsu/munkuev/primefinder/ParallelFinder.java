package ru.nsu.munkuev.primefinder;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Класс, реализующий поиск непростого числа используя Threads
 */
public class ParallelFinder {
    AtomicBoolean stop = new AtomicBoolean(false);
    int[] arr;

    public ParallelFinder(int[] arr) {
        this.arr = arr;
    }

    /**
     * Метод, выполняющий поиск непростого числа используя Threads.
     * Разделение массива на потоки происходит по чанкам. Один чанк равен {@code n div threads}
     * @param threads количество потоков, на которые нужно распараллелить задачу
     * @return {@code true} если найдено непростое число, {@code false} иначе
     */
    public boolean findNonPrime(int threads) {
        stop.set(false);

        int n = arr.length;

        Thread[] finders = new Thread[threads];

        int chunk = n/threads;
        for(int t = 0; t < threads; t++) {
            int begin = t*chunk;
            int end = (t == threads-1) ? n : begin+chunk;

            Runnable task = new ParellelFinderTask(begin, end, arr, stop);
            finders[t] = new Thread(task);
            finders[t].start();
        }

        for (int i = 0; i < threads; i++) {
            try {
                finders[i].join();
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        return stop.get();
    }



}
