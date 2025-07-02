package com.github.vkpro.hw02.p2;

public class FibAlgoBenchmark {

    public static final String[] ALGOS = new String[]{"recursive", "memorized", "iterative"};
    public static final int[] NUMS = new int[]{10, 20, 30, 35};

    public static void main(String[] args) {
        for (int num : NUMS) {
            System.out.printf("%n=== NUM=%d ===%n", num);
            for (String algo : ALGOS) {
                System.gc();
                long beforeUsedMem = getMemory();
                long startTime = System.currentTimeMillis();
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
                long endTime = System.currentTimeMillis();
                long afterUsedMem = getMemory();
                double memoryUsed = (afterUsedMem - beforeUsedMem);
                System.out.printf("%s, FibNumber = %d, Duration = %d ms, Memory Used = %.2f B\n",
                        algo, num, (endTime - startTime), memoryUsed);
            }

        }
    }

    private static long getMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
