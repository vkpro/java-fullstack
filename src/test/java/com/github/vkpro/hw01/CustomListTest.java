package com.github.vkpro.hw01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("List Implementations Unit Tests")
class CustomListTest {

    // Add any List implementation here (e.g., ArrayList, LinkedList, etc) to test them
    // Provides a stream of List<String> implementations for parameterized tests
    static Stream<List<String>> listProvider() {
        return Stream.of(new CustomList<>());
    }

    // Provides a stream of List<Integer> implementations for parameterized tests
    static Stream<List<Integer>> intListProvider() {
        return Stream.of(new CustomList<>());
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("add() should add elements and update size")
    void add(List<String> list) {
        assertTrue(list.isEmpty());
        assertTrue(list.add("A"));
        assertEquals(1, list.size());
        assertEquals("A", list.get(0));
        list.add("B");
        assertEquals(2, list.size());
        assertEquals("B", list.get(1));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("remove(index) should remove element at index and update size")
    void remove(List<String> list) {
        list.add("A");
        list.add("B");
        list.add("C");
        assertEquals("B", list.remove(1));
        assertEquals(2, list.size());
        assertEquals("C", list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("set(index, element) should replace element and return old value")
    void set(List<String> list) {
        list.add("A");
        list.add("B");
        String old = list.set(1, "C");
        assertEquals("B", old);
        assertEquals("C", list.get(1));
        assertEquals(2, list.size());
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("get(index) should throw IndexOutOfBoundsException for invalid indices")
    void get_outOfBounds(List<String> list) {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("set(index, element) should throw IndexOutOfBoundsException for invalid indices")
    void set_outOfBounds(List<String> list) {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, "A"));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "B"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "B"));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("remove(index) should throw IndexOutOfBoundsException for invalid indices")
    void remove_outOfBounds(List<String> list) {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        list.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("add(null) should add null element")
    void add_null(List<String> list) {
        assertTrue(list.add(null));
        assertNull(list.get(0));
        assertEquals(1, list.size());
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("remove(object) should remove first occurrence of object")
    void remove_object(List<String> list) {
        list.add("A");
        list.add(null);
        list.add("B");
        assertTrue(list.remove(null));
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertFalse(list.remove("C"));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("isEmpty() should reflect list state correctly")
    void emptyList(List<String> list) {
        assertTrue(list.isEmpty());
        list.add("A");
        assertFalse(list.isEmpty());
        list.remove(0);
        assertTrue(list.isEmpty());
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("List should grow beyond initial capacity")
    void listGrowth(List<String> list) {
        int initialCapacity = 10;
        for (int i = 0; i < initialCapacity; i++) {
            list.add("E" + i);
        }
        assertEquals(initialCapacity, list.size());
        list.add("E10");
        assertEquals(initialCapacity + 1, list.size());
        for (int i = 0; i <= initialCapacity; i++) {
            assertEquals("E" + i, list.get(i));
        }
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("clear() should remove all elements and reset size")
    void clearList(List<String> list) {
        list.add("A");
        list.add("B");
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("intListProvider")
    @DisplayName("CustomList<Integer> should support basic operations")
    void integerList_basicOperations(List<Integer> intList) {
        assertTrue(intList.add(1));
        assertTrue(intList.add(2));
        assertEquals(2, intList.size());
        assertEquals(1, intList.get(0));
        assertEquals(2, intList.set(1, 3));
        assertEquals(3, intList.get(1));
        assertEquals(1, intList.remove(0));
        assertEquals(1, intList.size());
        assertEquals(3, intList.get(0));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("Type safety should be enforced at compile time")
    void typeSafety_compileTime(List<String> stringList) {
        stringList.add("hello");
        // stringList.add(123); // Uncommenting this should cause a compile error
        assertEquals("hello", stringList.get(0));
    }

    @ParameterizedTest(name = "{index} => list={0}")
    @MethodSource("listProvider")
    @DisplayName("ensureCapacity() should handle large minCapacity")
    void ensureCapacity_minCapacityRespected(List<String> bigList) {
        for (int i = 0; i < 1000; i++) {
            bigList.add("item" + i);
        }
        assertEquals(1000, bigList.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals("item" + i, bigList.get(i));
        }
    }
}