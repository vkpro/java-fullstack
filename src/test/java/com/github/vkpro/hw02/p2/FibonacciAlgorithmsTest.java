package com.github.vkpro.hw02.p2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @ParameterizedTest(name = "testFibonacciMemoized({0}) = {1}")
    @MethodSource("fibonacciNumbers")
    void testFibonacciMemoized(int input, long result) {
        assertEquals(result, FibonacciAlgorithms.fibonacciMemoized(input));
    }

    @ParameterizedTest(name = "fibonacciIterative({0}) = {1}")
    @MethodSource("fibonacciNumbers")
    void testFibonacciIterative(int input, long result) {
        assertEquals(result, FibonacciAlgorithms.fibonacciIterative(input));
    }

    @Test
    void testFibonacciRecursive_NegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithms.fibonacciRecursive(-1));
    }

    @Test
    void testFibonacciMemoized_NegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithms.fibonacciMemoized(-1));
    }

    @Test
    void testFibonacciIterative_NegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> FibonacciAlgorithms.fibonacciMemoized(-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35})
    void testAllImplementationsProduceIdentiaclResults(int num) {
        long fibRec = FibonacciAlgorithms.fibonacciRecursive(num);
        long fibMem = FibonacciAlgorithms.fibonacciMemoized(num);
        long fibIter = FibonacciAlgorithms.fibonacciIterative(num);
        assertEquals(fibRec, fibMem);
        assertEquals(fibRec, fibIter);
    }
}