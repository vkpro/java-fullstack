package com.github.vkpro.hw02.p3;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ArrayOperationsPerformanceTest {
    //TODO: Refactor the class
    private static final int[] SIZES = {1_000, 10_000, 100_000, 1_000_000};
    private static final int[] SHIFTS = {1, 10, 100, 1_000};

    @Test
    void performanceComparison() {
        System.out.println("Performance Comparison Report");
        System.out.println("Array Size | Shift | System.arraycopy (ms) | For-Loop (ms)");
        for (int size : SIZES) {
            System.out.println("----------------------------------------------------------");
            for (int shift : SHIFTS) {
                // Prepare two identical arrays for comparison
                int[] arr1 = createArray(size);
                int[] arr2 = new int[arr1.length];
                System.arraycopy(arr1, 0, arr2, 0, arr1.length);

                long startTime = System.nanoTime();
                ArrayOperations.shiftLeftSystemCopy(arr1, shift);
                long arraycopyTime = System.nanoTime() - startTime;

                startTime = System.nanoTime();
                ArrayOperations.shiftLeftManualLoop(arr2, shift);
                long forLoopTime = System.nanoTime() - startTime;

                System.out.printf("%9d | %5d | %20.3f | %15.3f%n",
                        size, shift, arraycopyTime / 1_000_000.0, forLoopTime / 1_000_000.0);
            }
        }

        // Conclusion
        System.out.println("""
                ----------------------------------------------------------
                The time complexity for both methods: O(n)
                System.arraycopy is typically faster due to internal optimizations.
                """);
    }

    private int[] createArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = i;
        return arr;
    }
}