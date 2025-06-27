package com.github.vkpro.hw02.p2;

public class FibonacciAlgorithms {
    /**
     * Recursive implementation of Fibonacci sequence
     * Time Complexity: {todo: specify. explain}
     * Space Complexity: {todo: specify.explain }
     * <p>
     * Explanation: Each call branches into two recursive calls, creating
     * a binary tree of calls with height n. The same subproblems are
     * solved multiple times, leading to exponential time complexity.
     */
    public static long fibonacciRecursive(int n) {
        // todo: Your implementation here
        /**
         * 0 1 1 2 3 5 8 13 21
         *
         */
        if (n == 0 || n == 1) {
            return n;
        }
        long num = fibonacciRecursive(n - 1) + (n);
        return num;
    }

    public static void main(String[] args) {
        long l = fibonacciRecursive(3);
        System.out.println("l = " + l);
    }
}

