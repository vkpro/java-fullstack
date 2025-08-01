package com.github.vkpro.hw01;

import com.github.vkpro.hw03.CustomLinkedList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Performance comparison for ArrayList, LinkedList, CustomList and CustomLinkedList implementations.
 */
public class CustomListBenchmark {
    // Number of elements for insertion test
    private static final int INSERTION_COUNT = 1000_000;
    // Number of elements for add/remove cycle test
    private static final int CYCLE_COUNT = 100_000;

    public static void main(String[] args) {
        // Insertion performance test
        System.out.println("--- Insertion Performance (" + INSERTION_COUNT + " items) ---");
        measureInsertion("ArrayList", new ArrayList<>());
        measureInsertion("CustomList", new CustomList<>());
        measureInsertion("LinkedList", new LinkedList<>());
        measureInsertion("CustomLinkedList", new CustomLinkedList<>());

        // Add/remove cycle performance test
        System.out.println("\n--- Add/Remove Cycle (" + CYCLE_COUNT + " cycles) ---");
        measureAddRemoveCycle("ArrayList", new ArrayList<>());
        measureAddRemoveCycle("CustomList", new CustomList<>());
        measureAddRemoveCycle("LinkedList", new LinkedList<>());
        measureAddRemoveCycle("CustomLinkedList", new CustomLinkedList<>());
    }

    /**
     * Measures time and memory for inserting INSERTION_COUNT items.
     */
    private static void measureInsertion(String type, List<Integer> collection) {
        System.gc();
        long memoryBefore = getMemoryUsage();
        long startTime = System.nanoTime();
        for (int i = 0; i < INSERTION_COUNT; i++) {
            collection.add(i);
        }
        long endTime = System.nanoTime();
        long memoryAfter = getMemoryUsage();
        System.out.printf("%s: Duration = %.2f ms, Memory Used = %.2f MB\n",
                type, (endTime - startTime) / 1_000_000.0, (memoryAfter - memoryBefore) / (1024.0 * 1024));
    }

    /**
     * Measures time and memory for CYCLE_COUNT add/remove operations.
     */
    private static void measureAddRemoveCycle(String type, List<Integer> collection) {
        System.gc();
        long memoryBefore = getMemoryUsage();
        long startTime = System.nanoTime();
        for (int i = 0; i < CYCLE_COUNT; i++) {
            collection.add(i);
        }
        for (int i = 0; i < CYCLE_COUNT; i++) {
            collection.remove(0);
        }
        long endTime = System.nanoTime();
        long memoryAfter = getMemoryUsage();
        System.out.printf("%s: Duration = %.2f ms, Memory Used = %.2f MB\n",
                type, (endTime - startTime) / 1_000_000.0, (memoryAfter - memoryBefore) / (1024.0 * 1024));
    }

    /**
     * Returns the current used memory in bytes.
     */
    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}