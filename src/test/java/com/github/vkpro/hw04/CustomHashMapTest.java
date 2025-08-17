package com.github.vkpro.hw04;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CustomHashMapTest {

    static Stream<Arguments> mapProvider() {
        return Stream.of(
                Arguments.of(new HashMap<Integer, String>()),
                Arguments.of(new CustomHashMap<Integer, String>())
        );
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testEmptyMap(Map<Integer, String> map) {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertFalse(map.containsKey(1));
        assertFalse(map.containsValue("value"));
        assertNull(map.get(1));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testSinglePutAndGet(Map<Integer, String> map) {
        assertNull(map.put(1, "one"));
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
        assertEquals("one", map.get(1));
        assertTrue(map.containsKey(1));
        assertTrue(map.containsValue("one"));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testMultiplePuts(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        assertEquals(3, map.size());
        assertEquals("one", map.get(1));
        assertEquals("two", map.get(2));
        assertEquals("three", map.get(3));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testPutOverwrite(Map<Integer, String> map) {
        map.put(1, "one");
        assertEquals("one", map.put(1, "new value"));
        assertEquals("new value", map.get(1));
        assertEquals(1, map.size());
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testRemove(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");

        assertEquals("one", map.remove(1));
        assertEquals(1, map.size());
        assertNull(map.get(1));
        assertNull(map.remove(1)); // Remove non-existent key
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testClear(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");
        map.clear();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get(1));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testNullKey(Map<Integer, String> map) {
        assertNull(map.put(null, "null value"));
        assertEquals("null value", map.get(null));
        assertTrue(map.containsKey(null));
        assertEquals("null value", map.remove(null));
        assertFalse(map.containsKey(null));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testNullValue(Map<Integer, String> map) {
        map.put(1, null);
        assertTrue(map.containsValue(null));
        assertNull(map.get(1));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testCollisions(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");
        map.put(17, "seventeen"); // Likely same bucket if capacity is 16
        map.put(33, "thirty-three"); // Same bucket

        assertEquals(4, map.size());
        assertEquals("one", map.get(1));
        assertEquals("two", map.get(2));
        assertEquals("seventeen", map.get(17));
        assertEquals("thirty-three", map.get(33));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testKeySet(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        Set<Integer> keys = map.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertTrue(keys.contains(3));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testValues(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        Collection<String> values = map.values();
        assertEquals(3, values.size());
        assertTrue(values.contains("one"));
        assertTrue(values.contains("two"));
        assertTrue(values.contains("three"));
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testEntrySet(Map<Integer, String> map) {
        map.put(1, "one");
        map.put(2, "two");

        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        assertEquals(2, entries.size());

        for (Map.Entry<Integer, String> entry : entries) {
            assertTrue(entry.getKey() == 1 || entry.getKey() == 2);
            assertTrue(
                    (entry.getKey() == 1 && "one".equals(entry.getValue())) ||
                            (entry.getKey() == 2 && "two".equals(entry.getValue()))
            );
        }
    }

    @ParameterizedTest
    @MethodSource("mapProvider")
    void testInvalidInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CustomHashMap<>(-1,0.75f));
    }
}