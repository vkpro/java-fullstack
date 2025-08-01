package com.github.vkpro.hw03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomQueueTest {

    private CustomQueue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new CustomQueue<>();
    }

    @Test
    void testAddAndSize() {
        queue.add(1);
        queue.add(2);
        queue.add(3);
        assertEquals(3, queue.size());
    }

    @Test
    void testRemove() {
        queue.add(10);
        queue.add(20);
        assertEquals(10, queue.remove());
        assertEquals(1, queue.size());
    }

    @Test
    void testPeek() {
        queue.add(99);
        assertEquals(99, queue.peek());
        assertEquals(1, queue.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.add(5);
        assertFalse(queue.isEmpty());
    }

    @Test
    void testRemoveFromEmptyThrows() {
        assertThrows(NoSuchElementException.class, () -> queue.remove());
    }

    @Test
    void testPeekFromEmptyThrows() {
        assertThrows(NoSuchElementException.class, () -> queue.peek());
    }
}
