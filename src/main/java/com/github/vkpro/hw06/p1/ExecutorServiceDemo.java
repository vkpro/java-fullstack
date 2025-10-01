package com.github.vkpro.hw06.p1;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== CustomExecutorService Demo ===\n");

        // Demo 1: Platform Thread Pool
        demoPlatformThreads();

        System.out.println();

        // Demo 2: Virtual Threads
        demoVirtualThreads();
    }

    private static void demoPlatformThreads() throws Exception {
        System.out.println("1. Platform Thread Pool Demo (3 threads)");

        CustomExecutorService executor = CustomExecutorService.withPlatformThreadPool(3);

        // Submit some tasks
        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.printf("[Platform] Task %d running on %s%n",
                        taskId, Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.printf("[Platform] Task %d completed%n", taskId);
            });
        }

        // Submit a Callable task
        Future<String> future = executor.submit(() -> {
            Thread.sleep(500);
            return "Platform thread result: " + Thread.currentThread().getName();
        });

        System.out.println("Future result: " + future.get());

        // Shutdown and wait
        executor.shutdown();
        if (executor.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Platform thread pool terminated successfully");
        }
    }

    private static void demoVirtualThreads() throws Exception {
        System.out.println("2. Virtual Threads Demo");

        CustomExecutorService executor = CustomExecutorService.withVirtualThreadPerTask();

        for (int i = 1; i <= 8; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.printf("[Virtual] Task %d running on %s%n",
                        taskId, Thread.currentThread().getName());
                try {
                    Thread.sleep(800); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.printf("[Virtual] Task %d completed%n", taskId);
            });
        }

        // Submit a Callable task
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(300);
            return 42;
        });

        System.out.println("Future result: " + future.get());

        // Shutdown and wait
        executor.shutdown();
        if (executor.awaitTermination(3, TimeUnit.SECONDS)) {
            System.out.println("Virtual thread executor terminated successfully");
        }
    }
}