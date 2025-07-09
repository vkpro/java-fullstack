package com.github.vkpro.hw03;

import java.util.NoSuchElementException;

public class CustomStack<T> {

    private Node<T> top = null;
    private int size = 0;

    public void push(T data) {
        var newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack is empty");

        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack is empty");
        return top.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
