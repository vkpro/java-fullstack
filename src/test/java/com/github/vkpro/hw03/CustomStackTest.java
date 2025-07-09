package com.github.vkpro.hw03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CustomStackTest {

    private CustomStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new CustomStack<>();
    }

    @Test
    void testPushAndSize() {
        stack.push("One");
        stack.push("Two");
        assertEquals(2, stack.size());
    }

    @Test
    void testPop() {
        stack.push("A");
        stack.push("B");
        assertEquals("B", stack.pop());
        assertEquals(1, stack.size());
    }

    @Test
    void testPeek() {
        stack.push("X");
        assertEquals("X", stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push("NotEmpty");
        assertFalse(stack.isEmpty());
    }

    @Test
    void testPopFromEmptyStackThrows() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    @Test
    void testPeekFromEmptyStackThrows() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }
}
