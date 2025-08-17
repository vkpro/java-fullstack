package com.github.vkpro.hw04;

import java.util.*;
import java.util.function.Supplier;

/**
 * Performance testing framework for HashMap implementations
 */
public class HashMapPerformanceTest {

    private static final int[] TEST_SIZES = {1000, 100_000, 1_000_000};
    private static final int WARMUP_ITERATIONS = 3;
    private static final int TEST_ITERATIONS = 5;

    public static void main(String[] args) {
        System.out.println("HashMap Performance Comparison Test");
        System.out.println("===================================");

        for (var size : TEST_SIZES) {
            System.out.println("\nTesting with " + size + " elements:");
            System.out.println("-".repeat(40));
            runPerformanceTest(size);
        }
    }

    private static void runPerformanceTest(int size) {
        var keys = generateKeys(size);
        var values = generateValues(size);

        // Warmup
        warmup(() -> testMapPerformance(HashMap::new, keys, values));
        warmup(() -> testMapPerformance(CustomHashMap::new, keys, values));

        // Actual tests
        long totalJdkTime = 0;
        long totalCustomTime = 0;

        for (int i = 0; i < TEST_ITERATIONS; i++) {
            totalJdkTime += testMapPerformance(HashMap::new, keys, values);
            totalCustomTime += testMapPerformance(CustomHashMap::new, keys, values);
        }

        double avgJdkTime = totalJdkTime / (double) TEST_ITERATIONS;
        double avgCustomTime = totalCustomTime / (double) TEST_ITERATIONS;
        double ratio = avgCustomTime / avgJdkTime;

        System.out.printf("JDK HashMap:    %8.2f ms%n", avgJdkTime / 1_000_000.0);
        System.out.printf("Custom HashMap: %8.2f ms%n", avgCustomTime / 1_000_000.0);
        System.out.printf("Ratio (Custom/JDK): %.2fx %s%n",
                ratio, ratio > 1 ? "(slower)" : "(faster)");
    }

    private static void warmup(Runnable task) {
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            task.run();
        }
    }

    private static long testMapPerformance(Supplier<Map<Integer, String>> mapSupplier,
                                           List<Integer> keys, List<String> values) {
        var start = System.nanoTime();
        var map = mapSupplier.get();

        // Insert
        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }
        // Get
        for (var key : keys) {
            map.get(key);
        }
        // ContainsKey
        for (var key : keys) {
            map.containsKey(key);
        }
        // Remove half
        for (int i = 0; i < keys.size() / 2; i++) {
            map.remove(keys.get(i));
        }

        return System.nanoTime() - start;
    }

    private static List<Integer> generateKeys(int count) {
        var keySet = new HashSet<Integer>();
        var random = new Random();
        while (keySet.size() < count) {
            keySet.add(random.nextInt(Integer.MAX_VALUE));
        }
        return new ArrayList<>(keySet);
    }

    private static List<String> generateValues(int count) {
        var values = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            values.add("Value_" + i + "_" + System.nanoTime());
        }
        return values;
    }
}
