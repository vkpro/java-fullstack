package com.github.vkpro.hw02.p2;

import java.util.HashMap;

public class FibonacciAlgorithms {

    private static final HashMap<Integer, Long> cachedNumbers = new HashMap<>();

    /**
     * Recursive implementation of Fibonacci sequence
     * Time Complexity: O(2^n) because each call branches into two more calls
     * Space Complexity: O(n) because the maximum depth of the recursion stack is n
     * <p>
     * Explanation: Each call branches into two recursive calls, creating
     * a binary tree of calls with height n. The same subproblems are
     * solved multiple times, leading to exponential time complexity.
     */
    public static long fibonacciRecursive(int n) {
        /*
          0 1 1 2 3 5 8 13 21
         */
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n <= 1) {
            return n;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n -2 );
    }

    /**
     * Memoized implementation of Fibonacci sequence
     * Time Complexity: O(n) because each value calculated only once and then cached
     * Space Complexity: O(n) becuase cached can store up to n calculated numbers
     * <p>
     * Explanation: By caching intermediate results, we avoid
     * redundant calculations. Each number from 0 to n is
     * calculated exactly once.
     */
    public static long fibonacciMemoized(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n <= 1) {
            return n;
        }
        long result;
        if (cachedNumbers.containsKey(n)) {
            return cachedNumbers.get(n);
        } else {
            result = fibonacciMemoized(n - 1) + fibonacciMemoized(n - 2);
            cachedNumbers.put(n, result);
            return result;
        }
    }

    /**
     * Iterative implementation of Fibonacci sequence
     * Time Complexity: O(n) - single loop from 0 to n
     * Space Complexity: O(1) - constant space usage
     * <p>
     * Explanation: Uses bottom-up approach with only two variables
     * to track previous values, eliminating recursion overhead.
     */
    public static long fibonacciIterative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n <= 1) {
            return n;
        }
        long num1 = 0;
        long num2 = 1;
        long result = 0;
        // First two values are handled by if condition
        for (int i = 2; i <= n; i++) {
            result = num1 + num2;
            num1 = num2;
            num2 = result;
        }
        return result;
    }

    public static void main(String[] args) {
        long fibonacciRecursive = fibonacciRecursive(8);
        System.out.println("fibonacciRecursive = " + fibonacciRecursive);

        long fibonacciMemoized = fibonacciMemoized(8);
        System.out.println("fibonacciMemoized = " + fibonacciMemoized);
    }
}

