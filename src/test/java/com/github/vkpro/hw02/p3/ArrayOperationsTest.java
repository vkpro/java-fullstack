package com.github.vkpro.hw02.p3;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayOperationsTest {

    @Nested
    class ShiftLeftSystemCopyTests {
        @Test
        void testNormalShift() {
            int[] arr = {1, 2, 3, 4, 5};
            ArrayOperations.shiftLeftSystemCopy(arr, 2);
            assertArrayEquals(new int[]{3, 4, 5, 1, 2}, arr);
        }

        @Test
        void testZeroPositions() {
            int[] arr = {1, 2, 3};
            ArrayOperations.shiftLeftSystemCopy(arr, 0);
            assertArrayEquals(new int[]{1, 2, 3}, arr);
        }

        @Test
        void testPositionsEqualsLength() {
            int[] arr = {1, 2, 3};
            ArrayOperations.shiftLeftSystemCopy(arr, 3);
            assertArrayEquals(new int[]{1, 2, 3}, arr);
        }

        @Test
        void testPositionsGreaterThanLength() {
            int[] arr = {1, 2, 3};
            ArrayOperations.shiftLeftSystemCopy(arr, 5);
            assertArrayEquals(new int[]{3, 1, 2}, arr);
        }

        @Test
        void testNullArray() {
            // No exception expected for null
            ArrayOperations.shiftLeftSystemCopy(null, 2);
        }

        @Test
        void testEmptyArray() {
            int[] arr = {};
            ArrayOperations.shiftLeftSystemCopy(arr, 1);
            assertArrayEquals(new int[]{}, arr);
        }

        @Test
        void testNegativePositions() {
            int[] arr = {1, 2, 3, 4};
            ArrayOperations.shiftLeftSystemCopy(arr, -2);
            assertArrayEquals(new int[]{1, 2, 3, 4}, arr);
        }

        @Test
        void testNegativePositionsNullArray() {
            // No exception expected for null
            ArrayOperations.shiftLeftSystemCopy(null, -3);
        }
    }

    @Nested
    class ShiftLeftManualLoopTests {
        @Test
        void testNormalShift() {
            int[] arr = {1, 2, 3, 4, 5};
            ArrayOperations.shiftLeftManualLoop(arr, 2);
            assertArrayEquals(new int[]{3, 4, 5, 1, 2}, arr);
        }

        @Test
        void testZeroPositions() {
            int[] arr = {1, 2, 3};
            ArrayOperations.shiftLeftManualLoop(arr, 0);
            assertArrayEquals(new int[]{1, 2, 3}, arr);
        }

        @Test
        void testPositionsEqualsLength() {
            int[] arr = {1, 2, 3};
            ArrayOperations.shiftLeftManualLoop(arr, 3);
            assertArrayEquals(new int[]{1, 2, 3}, arr);
        }

        @Test
        void testPositionsGreaterThanLength() {
            int[] arr = {1, 2, 3};
            ArrayOperations.shiftLeftManualLoop(arr, 5);
            assertArrayEquals(new int[]{3, 1, 2}, arr);
        }

        @Test
        void testNullArray() {
            // No exception expected for null
            ArrayOperations.shiftLeftManualLoop(null, 2);
        }

        @Test
        void testEmptyArray() {
            int[] arr = {};
            ArrayOperations.shiftLeftManualLoop(arr, 1);
            assertArrayEquals(new int[]{}, arr);
        }

        @Test
        void testNegativePositions() {
            int[] arr = {1, 2, 3, 4};
            ArrayOperations.shiftLeftManualLoop(arr, -2);
            assertArrayEquals(new int[]{1, 2, 3, 4}, arr);
        }

        @Test
        void testNegativePositionsNullArray() {
            // No exception expected for null
            ArrayOperations.shiftLeftManualLoop(null, -3);
        }
    }
}