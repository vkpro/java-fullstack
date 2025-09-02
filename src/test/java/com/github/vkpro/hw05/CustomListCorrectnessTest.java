package com.github.vkpro.hw05;

import com.github.vkpro.hw01.CustomList;

import java.util.List;

public class CustomListCorrectnessTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Testing UNSYNCHRONIZED CustomList:");
        for (int run = 0; run < 100; run++) {
            CustomList<Integer> list = new CustomList<>();
            runTest(list, run + 1);
        }

        System.out.println("\nTesting SYNCHRONIZED CustomList:");
        for (int run = 0; run < 100; run++) {
            CustomList<Integer> customList = new CustomList<>();
            List<Integer> list = new SynchronizedCustomListDecorator<>(customList);
            runTest(list, run + 1);
        }

        System.out.println("\nTesting READ-WRITE LOCK CustomList:");
        for (int run = 0; run < 100; run++) {
            CustomList<Integer> customList = new CustomList<>();
            List<Integer> list = new ReadWriteLockCustomListDecorator<>(customList);
            runTest(list, run + 1);
        }
    }

    static void runTest(List<Integer> list, int runNumber) throws InterruptedException {
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

        System.out.println("Run " + runNumber + ": " + list.size() + " elements");
    }
}