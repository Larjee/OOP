package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PrimeFinderUtilTest {
    @Test
    void testNegativeNumbersAreNotPrime() {
        assertTrue(PrimeFinderUtil.isNotPrime(-10));
        assertTrue(PrimeFinderUtil.isNotPrime(-1));
    }

    @Test
    void testZeroAndOneAreNotPrime() {
        assertTrue(PrimeFinderUtil.isNotPrime(0));
        assertTrue(PrimeFinderUtil.isNotPrime(1));
    }

    @Test
    void testSmallPrimes() {
        assertFalse(PrimeFinderUtil.isNotPrime(2));
        assertFalse(PrimeFinderUtil.isNotPrime(3));
        assertFalse(PrimeFinderUtil.isNotPrime(5));
        assertFalse(PrimeFinderUtil.isNotPrime(7));
    }

    @Test
    void testSmallNonPrimes() {
        assertTrue(PrimeFinderUtil.isNotPrime(4));
        assertTrue(PrimeFinderUtil.isNotPrime(6));
        assertTrue(PrimeFinderUtil.isNotPrime(8));
        assertTrue(PrimeFinderUtil.isNotPrime(9));
    }

    @Test
    void testLargePrime() {
        assertFalse(PrimeFinderUtil.isNotPrime(7919));
    }

    @Test
    void testLargeNonPrime() {
        assertTrue(PrimeFinderUtil.isNotPrime(8000));
    }

}

class FindersTest {
    @Test
    void allPrimes_shouldReturnFalse_forAllFinders() {
        int[] arr = {2, 3, 5, 7, 11, 13, 17};

        assertFalse(SequentialFinder.findNonPrime(arr));
        assertFalse(ParallelStreamFinder.findNonPrime(arr));
        assertFalse(new ParallelFinder(arr).findNonPrime(4));
    }

    @Test
    void containsNonPrime_shouldReturnTrue_forAllFinders() {
        int[] arr = {2, 3, 5, 8, 11};

        assertTrue(SequentialFinder.findNonPrime(arr));
        assertTrue(ParallelStreamFinder.findNonPrime(arr));
        assertTrue(new ParallelFinder(arr).findNonPrime(4));
    }

    @Test
    void emptyArray_shouldReturnFalse_forAllFinders() {
        int[] arr = {};

        assertFalse(SequentialFinder.findNonPrime(arr));
        assertFalse(ParallelStreamFinder.findNonPrime(arr));
        assertFalse(new ParallelFinder(arr).findNonPrime(2));
    }

    @Test
    void singlePrime_shouldReturnFalse_forAllFinders() {
        int[] arr = {13};

        assertFalse(SequentialFinder.findNonPrime(arr));
        assertFalse(ParallelStreamFinder.findNonPrime(arr));
        assertFalse(new ParallelFinder(arr).findNonPrime(1));
    }

    @Test
    void singleNonPrime_shouldReturnTrue_forAllFinders() {
        int[] arr = {15};

        assertTrue(SequentialFinder.findNonPrime(arr));
        assertTrue(ParallelStreamFinder.findNonPrime(arr));
        assertTrue(new ParallelFinder(arr).findNonPrime(1));
    }

    @Test
    void arrayNotModified_shouldNotChangeInput() {
        int[] arr = {2, 3, 5, 8, 11};
        int[] copy = Arrays.copyOf(arr, arr.length);

        SequentialFinder.findNonPrime(arr);
        ParallelStreamFinder.findNonPrime(arr);
        new ParallelFinder(arr).findNonPrime(3);

        assertArrayEquals(copy, arr);
    }

    @Test
    void threadsMoreThanArraySize_shouldStillWork() {
        int[] arr = {2, 3, 5, 7, 8};

        assertTrue(new ParallelFinder(arr).findNonPrime(100));
    }

    @Test
    void threadsEqualToArraySize_shouldWork() {
        int[] arr = {2, 3, 5, 7, 9};

        assertTrue(new ParallelFinder(arr).findNonPrime(arr.length));
    }

    @Test
    void threadsOne_shouldBehaveLikeSequential() {
        int[] arr1 = {2, 3, 5, 7, 11};
        int[] arr2 = {2, 3, 4, 7, 11};

        assertEquals(SequentialFinder.findNonPrime(arr1), new ParallelFinder(arr1).findNonPrime(1));
        assertEquals(SequentialFinder.findNonPrime(arr2), new ParallelFinder(arr2).findNonPrime(1));
    }

    @Test
    void negativeZeroOne_shouldBeDetectedAsNonPrime() {
        int[] arr = {2, 3, -5, 7};

        assertTrue(SequentialFinder.findNonPrime(arr));
        assertTrue(ParallelStreamFinder.findNonPrime(arr));
        assertTrue(new ParallelFinder(arr).findNonPrime(2));
    }
}

class DataGeneratorTest {

    @Test
    void smallSizeSmallNumbers() {
        int[] arr = DataGenerator.smallSizeSmallNumbers();
        assertEquals(10_000, arr.length);

        int limit = Math.min(arr.length, 300);
        for (int i = 0; i < limit; i++) {
            assertFalse(PrimeFinderUtil.isNotPrime(arr[i]));
        }
    }

    @Test
    void smallSizeBigNumbers() {
        int[] arr = DataGenerator.smallSizeBigNumbers();
        assertEquals(10_000, arr.length);

        int limit = Math.min(arr.length, 300);
        for (int i = 0; i < limit; i++) {
            assertFalse(PrimeFinderUtil.isNotPrime(arr[i]));
        }
    }

}