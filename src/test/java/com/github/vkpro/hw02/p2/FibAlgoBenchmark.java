package com.github.vkpro.hw02.p2;

public class FibAlgoBenchmark {

    public static final String[] ALGOS = new String[]{"recursive", "memorized", "iterative"};
    public static final String[] EFF_ALGOS = new String[]{"memorized", "iterative"};
    public static final int[] NUMS = new int[]{10, 20, 30, 35, 40, 45, 50};
    public static final int[] BIG_NUMS = new int[]{20_000, 200_000, 400_000};

    public static void main(String[] args) {
        System.out.printf("%-10s %-10s %-15s %-15s\n", "Algo", "n", "Duration (ms)", "Memory (KB)");
        for (int num : NUMS) {
            System.out.println("-------------------------------------------------");
            for (String algo : ALGOS) {
                runAlgoTest(num, algo);
            }
        }
        for (int num : BIG_NUMS) {
            System.out.println("-------------------------------------------------");
            for (String algo : EFF_ALGOS) {
                runAlgoTest(num, algo);
            }
        }
    }

    private static void runAlgoTest(int num, String algo) {
        System.gc();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();

        calculateFibNumber(num, algo);

        long endTime = System.currentTimeMillis();
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double memoryUsedKB = (afterUsedMem - beforeUsedMem) / 1024.0;
        System.out.printf("%-10s %-10d %-15d %-15.2f\n",
                algo, num, (endTime - startTime), memoryUsedKB);
    }

    private static void calculateFibNumber(int num, String algo) {
        switch (algo) {
            case "recursive":
                FibonacciAlgorithms.fibonacciRecursive(num);
                break;
            case "memorized":
                FibonacciAlgorithms.fibonacciMemoized(num);
                break;
            case "iterative":
                FibonacciAlgorithms.fibonacciIterative(num);
                break;
        }
    }
}
