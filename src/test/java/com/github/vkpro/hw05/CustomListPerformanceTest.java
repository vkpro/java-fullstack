package com.github.vkpro.hw05;

import com.github.vkpro.hw01.CustomList;

import java.util.List;

public class CustomListPerformanceTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Performance Test: 2 threads adding 1 million elements each\n");

        // Test unsynchronized list
        CustomList<Integer> unsyncList = new CustomList<>();
        long unsyncTime = measureTime(unsyncList, "UNSYNCHRONIZED");

        // Test synchronized list
        CustomList<Integer> customList1 = new CustomList<>();
        List<Integer> syncList = new SynchronizedCustomListDecorator<>(customList1);
        long syncTime = measureTime(syncList, "SYNCHRONIZED");

        // Test read-write lock list
        CustomList<Integer> customList2 = new CustomList<>();
        List<Integer> rwLockList = new ReadWriteLockCustomListDecorator<>(customList2);
        long rwLockTime = measureTime(rwLockList, "READ-WRITE LOCK");

        // Compare performance
        System.out.println("Performance Comparison:");
        System.out.println("Unsynchronized:   " + unsyncTime + " ms");
        System.out.println("Synchronized:     " + syncTime + " ms");
        System.out.println("Read-Write Lock:  " + rwLockTime + " ms");
        System.out.printf("Synchronized overhead:   %.2fx slower\n", (double) syncTime / unsyncTime);
        System.out.printf("Read-Write overhead:     %.2fx slower\n", (double) rwLockTime / unsyncTime);
    }

    static long measureTime(List<Integer> list, String type) throws InterruptedException {
        System.out.println("Testing " + type + " list...");

        long startTime = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                list.add(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1_000_000; i < 2_000_000; i++) {
                list.add(i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println(type + " completed in " + duration + " ms");
        System.out.println("Final size: " + list.size() + " elements\n");

        return duration;
    }
}