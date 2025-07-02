package com.github.vkpro.hw02.p2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class FibAlgoBenchmark {

    public static void main(String[] args) {
        int[] nums = {10, 20, 30, 35};
        for (int num : nums) {
            System.gc();
            long beforeUsedMem = getMemory();
            long startTime = System.currentTimeMillis();
            FibonacciAlgorithms.fibonacciRecursive(num);
            long endTime = System.currentTimeMillis();
            long afterUsedMem = getMemory();
            double durationMs = endTime - startTime;
            double memoryUsed = (afterUsedMem - beforeUsedMem) / (1024.0);
            System.out.printf("FibNumber = %d, Duration = %.2f ms, Memory Used = %.2f kB\n", num, durationMs, memoryUsed);
        }
    }



    @ParameterizedTest
    @MethodSource("fibAlgoCombinations")
    void test(int num) {
        System.gc();
        long beforeUsedMem = getMemory();
        long startTime = System.currentTimeMillis();
        FibonacciAlgorithms.fibonacciRecursive(num);
        long endTime = System.currentTimeMillis();
        long afterUsedMem = getMemory();

        double durationMs = endTime - startTime;
        double memoryUsed = (afterUsedMem - beforeUsedMem) / (1024.0 * 1024.0);
        System.out.printf("Duration = %.2f ms, Memory Used = %.2f MB\n", durationMs, memoryUsed);
    }

    private static long getMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    static Stream<Arguments> fibAlgoCombinations() {
        int[] nums = {10, 20, 30, 35};
        String[] algos = {"recursive", "iterative", "dynamic"};
        return Stream.of(nums)
                .flatMap(n -> Stream.of(algos).map(a -> Arguments.of(n, a)));
    }
}
