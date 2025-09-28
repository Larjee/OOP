package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    @Test
    void checkEmptyArr(){
        int[] arr = {};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void checkSingleElement(){
        int[] arr = {612};
        HeapSort.sort(arr);
        assertArrayEquals(new int[]{612}, arr);
    }

    @Test
    void checkOrdinaryArr(){
        int[] arr = {54, 12, 45, 31, 123, 34, 8};
        HeapSort.sort(arr);
        assertArrayEquals(new int[] {8, 12, 31, 34, 45, 54, 123}, arr);
    }

    @Test
    void checkSortedArr(){
        int[] arr = {1,2,3,4,5,6,7,8};
        HeapSort.sort(arr);
        assertArrayEquals(new int[] {1,2,3,4,5,6,7,8}, arr);
    }

    @Test
    void checkAllSameArr(){
        int[] arr = {6,6,6,6,6,6,6};
        HeapSort.sort(arr);
        assertArrayEquals(new int[] {6,6,6,6,6,6,6}, arr);
    }

    @Test
    void checkReverseArr(){
        int[] arr = {8,7,6,5,4,3,2,1};
        HeapSort.sort(arr);
        assertArrayEquals(new int[] {1,2,3,4,5,6,7,8}, arr);
    }
}