package com.github.vkpro.hw05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class MultithreadedPerformance {
    private static short[] array;

    public static void main(String[] args) {
        initializeArray();
        warmUpJVM();
        runBenchmarks();
    }

    private static void initializeArray() {
        array = new short[100_000_001];
        Arrays.fill(array, (short) 1);
        System.out.println("Array created with " + array.length + " elements\n");
    }

    private static void warmUpJVM() {
        System.out.println("Warming up JVM...");
        for (int i = 0; i < 5; i++) {
            sumWithParallelStream();
            sumWithParallelThreads(4);
        }
        System.out.println("Warm up completed\n");
    }

    private static void runBenchmarks() {
        int[] threadCounts = {1, 10, 100, 1000};

        for (int threadsCount : threadCounts) {
            System.out.printf("=== Testing with %d threads ===\n", threadsCount);

            // Test parallel stream
            long startTime = System.nanoTime();
            long sum1 = sumWithParallelStream();
            long endTime = System.nanoTime();
            double streamTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

            // Test parallel threads
            startTime = System.nanoTime();
            long sum2 = sumWithParallelThreads(threadsCount);
            endTime = System.nanoTime();
            double threadsTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds

            // Print results
            System.out.printf("Parallel stream sum: %d (%.2f ms)\n", sum1, streamTime);
            System.out.printf("ExecutorService sum: %d (%.2f ms)\n", sum2, threadsTime);
            System.out.printf("Performance ratio: %.2fx %s\n\n",
                    Math.abs(streamTime / threadsTime),
                    streamTime < threadsTime ? "(stream faster)" : "(threads faster)");
        }
    }

    public static long sumWithParallelStream() {
        return IntStream.range(0, array.length)
                .parallel()
                .mapToLong(i -> array[i])
                .sum();
    }

    public static long sumWithParallelThreads(int threadsCount) {
        try (ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount)) {
            List<Future<Long>> futures = new ArrayList<>();

            int chunkSize = array.length / threadsCount;

            for (int t = 0; t < threadsCount; t++) {
                int start = t * chunkSize;
                int end = (t == threadsCount - 1) ? array.length : start + chunkSize;

                futures.add(threadPool.submit(() -> {
                    long sum = 0;
                    for (int i = start; i < end; i++) {
                        sum += array[i];
                    }
                    return sum;
                }));
            }

            return futures.stream()
                    .mapToLong(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .sum();

        } catch (Exception e) {
            throw new RuntimeException("Error during parallel execution", e);
        }
    }
}