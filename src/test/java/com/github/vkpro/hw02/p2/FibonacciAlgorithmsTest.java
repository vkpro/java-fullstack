package com.github.vkpro.hw02.p2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciAlgorithmsTest {

    @Test
    void fibonacciRecursive() {
        // Base cases
        assertEquals(0, FibonacciAlgorithms.fibonacciRecursive(0));
        assertEquals(1, FibonacciAlgorithms.fibonacciRecursive(1));
        // Small values
        assertEquals(1, FibonacciAlgorithms.fibonacciRecursive(2));
        assertEquals(2, FibonacciAlgorithms.fibonacciRecursive(3));
        assertEquals(3, FibonacciAlgorithms.fibonacciRecursive(4));
        assertEquals(5, FibonacciAlgorithms.fibonacciRecursive(5));
        // Negative input (optional, depending on implementation)
        assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithms.fibonacciRecursive(-1));
    }
}