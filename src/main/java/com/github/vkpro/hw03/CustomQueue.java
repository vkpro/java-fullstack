package com.github.vkpro.hw03;

import java.util.NoSuchElementException;

public class CustomQueue<T> {

    private Node<T> first = null;
    private Node<T> last = null;
    private int size = 0;

    // Add item to end
    public void add(T data) {
        var newNode = new Node<>(data);
        if (last != null) {
            last.next = newNode;
        } else {
            first = newNode; // first item
        }
        last = newNode;
        size++;
    }

    // Remove item from front
    public T remove() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");

        T data = first.data;
        first = first.next;
        if (first == null) {
            last = null; // queue is empty
        }
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return first.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
