package ru.nsu.munkuev;

/**
 *  Класс, реализующий пирамидальную сортировку
*/
public class HeapSort {
    /**
     * Метод реализующий саму сортировку с помощью метода heapify
     *
     * @param arr исходный массив
     */
    public static void sort(int[] arr){
        int n = arr.length;

        //строим max-heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        for (int i = n - 1; i > 0; i--) {

            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0);
        }
    }

    /**
     * Метод восстанавливает основное свойство кучи для дерева
     * с корнем в i-ой вершине при условии, что оба поддерева
     * ему удовлетворяют.
     * @param arr исходный массив
     * @param n размер массива
     * @param i индекс элемента в массиве для которого проверяем свойство дерева
     */
    private static void heapify(int[] arr, int n, int i) {
        int root = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[root]) {
            root = left;
        }

        if (right < n && arr[right] > arr[root]) {
            root = right;
        }

        if (root != i) {
            int temp = arr[i];
            arr[i] = arr[root];
            arr[root] = temp;

            heapify(arr, n, root);
        }
    }


}


