package com.github.vkpro.hw02.p3;

public class ArrayOperations {
    /**
     * Shift array elements using System.arraycopy
     */
    public static void shiftLeftSystemCopy(int[] array, int positions) {
        if (array == null || array.length == 0 || positions <= 0) return;
        // Avoid unnecessary operations
        positions = positions % array.length;
        int[] tempArray = new int[positions];
        // Save elements to the temporary array
        System.arraycopy(array, 0, tempArray, 0, positions);
        // Left shift
        System.arraycopy(array, positions, array, 0, array.length - positions);
        // Copy saved elements to the end of array
        System.arraycopy(tempArray, 0, array, array.length - positions, positions);
    }

    /**
     * Shift array elements using manual for loop
     */
    public static void shiftLeftManualLoop(int[] array, int positions) {
        // todo: Implementation using for loop
        if (array == null || array.length == 0 || positions <= 0) return;
        // Avoid unnecessary operations
        positions = positions % array.length;
        int[] tempArray = new int[positions];
        // Save elements to the temporary array
        for (int i = 0; i < positions; i++) {
            tempArray[i] = array[i];
        }
        // Left shift
        for (int i = 0; i < array.length - positions; i++) {
            array[i] = array[positions + i];
        }
        // Copy saved elements to the end of array
        for (int i = 0; i < tempArray.length; i++) {
            array[array.length - positions + i] = tempArray[i];
        }
    }
}
