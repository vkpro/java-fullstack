package com.github.vkpro.hw06.p1;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Comprehensive test suite for CustomExecutorService
 */
public class CustomExecutorServiceTest {

    private static final int PERFORMANCE_TASK_COUNT = 10_000;
    private static final int PERFORMANCE_SLEEP_MS = 10;
    private static final int CONCURRENT_TASK_COUNT = 1_000;
    private static final int[] POOL_SIZES = {10, 50, 100, 500};

    public static void main(String[] args) {
        System.out.println("=== CustomExecutorService Test Suite ===\n");

        // Test 1: Performance comparison
        testPerformanceComparison();

        // Test 2: Concurrent task execution
        testConcurrentExecution();

        // Test 3: Shutdown behavior
        testShutdownBehavior();
    }

    /**
     * Test 1: Performance comparison with different pool sizes
     * Tests 10,000 tasks that sleep for 10ms each
     */
    private static void testPerformanceComparison() {
        System.out.println("--- Performance Test: 10,000 tasks sleeping 10ms ---");
        System.out.println("Pool Size | Platform Time (ms) | Virtual Time (ms)  | Platform Memory (MB)  | Virtual Memory (MB)  | Speedup");
        System.out.println("----------|--------------------|--------------------|-----------------------|----------------------|--------");

        for (int poolSize : POOL_SIZES) {
            // Platform threads
            long[] platformResult = measurePerformance(
                    () -> CustomExecutorService.withPlatformThreadPool(poolSize),
                    "Platform-" + poolSize
            );

            // Virtual threads
            long[] virtualResult = measurePerformance(
                    () -> CustomExecutorService.withVirtualThreadPerTask(),
                    "Virtual"
            );

            double speedup = (double) platformResult[0] / virtualResult[0];

            System.out.printf("%9d | %18d | %18d | %21.2f | %20.2f | %.2fx%n",
                    poolSize,
                    platformResult[0],
                    virtualResult[0],
                    platformResult[1] / 1024.0 / 1024.0,  // platform memory
                    virtualResult[1] / 1024.0 / 1024.0,   // virtual memory
                    speedup
            );

        }
        System.out.println();
    }

    /**
     * Test 2: Concurrent execution with shared AtomicInteger
     * Tests 1000 tasks incrementing a counter
     */
    private static void testConcurrentExecution() {
        System.out.println("--- Concurrent Execution Test: 1000 tasks incrementing counter ---");

        for (int poolSize : POOL_SIZES) {
            // Platform threads
            testConcurrentExecutionImpl(
                    "Platform-" + poolSize,
                    CustomExecutorService.withPlatformThreadPool(poolSize)
            );
        }

        // Virtual threads
        testConcurrentExecutionImpl(
                "Virtual",
                CustomExecutorService.withVirtualThreadPerTask()
        );

        System.out.println();
    }

    /**
     * Test 3: Shutdown behavior testing
     */
    private static void testShutdownBehavior() {
        System.out.println("--- Shutdown Behavior Test ---");

        testShutdownBehaviorImpl("Platform Threads",
                CustomExecutorService.withPlatformThreadPool(4));

        testShutdownBehaviorImpl("Virtual Threads",
                CustomExecutorService.withVirtualThreadPerTask());

        System.out.println();
    }

    private static long[] measurePerformance(Supplier<CustomExecutorService> supplier, String name) {
        // Force garbage collection before test
        long memoryBefore = getUsedMemory();
        long startTime = System.currentTimeMillis();

        try (var executor = supplier.get()) {
            List<Future<?>> futures = new ArrayList<>();

            // Submit all tasks
            for (int i = 0; i < PERFORMANCE_TASK_COUNT; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        Thread.sleep(PERFORMANCE_SLEEP_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }));
            }

            // Wait for all tasks to complete
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Task failed in " + name + ": " + e.getMessage());
                }
            }

            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        long memoryAfter = getUsedMemory();

        return new long[]{endTime - startTime, Math.abs(memoryAfter - memoryBefore)};
    }

    /**
     * Tests concurrent execution with atomic counter
     */
    private static void testConcurrentExecutionImpl(String name, CustomExecutorService executor) {
        AtomicInteger counter = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        try {
            for (int i = 0; i < CONCURRENT_TASK_COUNT; i++) {
                futures.add(executor.submit(() -> {
                    counter.incrementAndGet();
                }));
            }

            for (Future<?> future : futures) {
                try {
                    future.get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    System.err.println("Task failed in " + name + ": " + e.getMessage());
                }
            }

            executor.shutdown();
            boolean terminated = executor.awaitTermination(10, TimeUnit.SECONDS);

            long endTime = System.currentTimeMillis();
            int finalCount = counter.get();

            System.out.printf("%s: Counter = %d/%d (%s), Time = %d ms, Terminated = %s%n",
                    name,
                    finalCount,
                    CONCURRENT_TASK_COUNT,
                    finalCount == CONCURRENT_TASK_COUNT ? "PASS" : "FAIL",
                    endTime - startTime,
                    terminated ? "YES" : "NO"
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Concurrent test interrupted for " + name + ": " + e.getMessage());
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
    }

    /**
     * Tests basic shutdown behavior
     */
    private static void testShutdownBehaviorImpl(String name, CustomExecutorService executor) {
        System.out.printf("Testing %s shutdown...%n", name);

        try {
            executor.submit(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
            });
            executor.submit(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
            });

            // Test shutdown
            executor.shutdown();
            System.out.printf("  Shutdown called - isShutdown: %s%n", executor.isShutdown());

            // Test that new tasks are rejected
            try {
                executor.submit(() -> { });
                System.out.println("  ERROR: Should have rejected new task");
            } catch (RejectedExecutionException e) {
                System.out.println("  SUCCESS: New task rejected");
            }

            // Wait for termination
            boolean terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            System.out.printf("  Terminated: %s%n", terminated ? "YES" : "NO");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();
    }

    /**
     * Get current memory usage in bytes
     */
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}