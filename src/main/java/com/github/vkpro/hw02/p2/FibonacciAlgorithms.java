package com.github.vkpro.hw02.p2;

public class FibonacciAlgorithms {
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
        // todo: Your implementation here
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

    public static void main(String[] args) {
        long l = fibonacciRecursive(8);
        System.out.println("l = " + l);
    }
}

