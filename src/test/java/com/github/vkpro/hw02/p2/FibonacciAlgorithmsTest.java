package com.github.vkpro.hw02.p2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciAlgorithmsTest {

    static Stream<Arguments> fibonacciNumbers() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(1, 1),
                Arguments.of(2, 1),
                Arguments.of(3, 2),
                Arguments.of(4, 3),
                Arguments.of(5, 5),
                Arguments.of(6, 8),
                Arguments.of(8, 21)
        );
    }

    @ParameterizedTest(name = "fibonacciRecursive({0}) = {1}")
    @MethodSource("fibonacciNumbers")
    void testFibonacciRecursive(int input, long result) {
        assertEquals(result, FibonacciAlgorithms.fibonacciRecursive(input));
    }

    @ParameterizedTest(name = "fibonacciRecursive({0}) = {1}")
    @MethodSource("fibonacciNumbers")
    void testFibonacciMemoized(int input, long result) {
        assertEquals(result, FibonacciAlgorithms.fibonacciMemoized(input));
    }

    @Test
    void testFibonacciRecursive_NegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithms.fibonacciRecursive(-1));
    }

    @Test
    void testFibonacciMemoized_NegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithms.fibonacciMemoized(-1));
    }
}