package ru.nsu.munkuev;

/**
 * Sample class to simulate 1.1 task functionality
 */
public class Sample {
    public static void printHelloWorld() {
        System.out.println("Hello world!");
    }

    public static void main(String[] args) {
        //printHelloWorld();
        int[] arr = {54, 12, 45, 31, 123, 34, 8};
        HeapSort.sort(arr);
        int n = arr.length;
        for(int i = 0; i<n; i++){
            System.out.printf("%d, ", arr[i]);
        }
    }
}
