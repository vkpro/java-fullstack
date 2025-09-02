package com.github.vkpro.hw05;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * Tests 8000 virtual threads vs platform threads sleeping 200ms each
 */
public class VirtualThreadsTest {

    private static final int THREAD_COUNT = 8000;
    private static final int SLEEP_DURATION_MS = 200;

    public static void main(String[] args) {
        VirtualThreadsTest test = new VirtualThreadsTest();

        test.runTest("VIRTUAL THREADS TEST", () -> test.executeWithExecutor(Executors.newVirtualThreadPerTaskExecutor()));
        sleep(2000);
        test.runTest("PLATFORM THREADS TEST", () -> test.executeWithExecutor(Executors.newFixedThreadPool(THREAD_COUNT)));
    }

    private void runTest(String testName, Runnable taskExecutor) {
        printSeparator(testName);
        System.out.println("Starting " + THREAD_COUNT + " " + testName.toLowerCase() + "...");

        Instant start = Instant.now();
        taskExecutor.run();
        Duration elapsed = Duration.between(start, Instant.now());

        System.out.println("\n=== RESULTS ===");
        System.out.println("Threads completed: " + THREAD_COUNT + "/" + THREAD_COUNT);
        System.out.println("Total time: " + elapsed.toMillis() + " ms");
        printMemory("Final");
    }

    private void executeWithExecutor(ExecutorService executor) {
        try (executor) {
            List<Future<String>> futures = submitTasks(executor);
            waitForCompletion(futures);
        }
    }

    private List<Future<String>> submitTasks(ExecutorService executor) {
        return IntStream.range(0, THREAD_COUNT)
                .mapToObj(i -> executor.submit(this::sleepTask))
                .toList();
    }

    private String sleepTask() {
        try {
            Thread.sleep(SLEEP_DURATION_MS);
            return "Done";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Interrupted";
        }
    }

    private void waitForCompletion(List<Future<String>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void printSeparator(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(title);
        System.out.println("=".repeat(50));
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void printMemory(String stage) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.printf("%s memory - Used: %d MB, Free: %d MB%n",
                stage,
                bytesToMB(usedMemory),
                bytesToMB(freeMemory));
    }

    private static long bytesToMB(long bytes) {
        return bytes / (1024 * 1024);
    }
}