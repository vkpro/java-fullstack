package com.github.vkpro.hw01;

import java.util.*;

public class CustomList<T> implements List<T> {

    public static final int INITIAL_CAPACITY = 10;
    private Object[] list;
    private int size;

    public CustomList() {
        this.list = new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object element) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator iterator() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Object element) {
        // Ensure there is enough capacity, then add the element and increment size
        ensureCapacity();
        list[size] = element;
        size++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object element) {
        // Iterate through the list to find the first occurrence of the element and remove it
        for (int index = 0; index < size + 1; index++) {
            if (element == null && list[index] == null ||
                    (element != null && element.equals(list[index]))) {
                remove(index);
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        list = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(int index) {
        isIndexValid(index);
        return (T) list[index];
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public T set(int index, Object element) {
        // Validate the index, replace the element at the given index, and return the old value
        isIndexValid(index);
        Object old = list[index];
        list[index] = element;
        return (T) old;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, Object element) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T remove(int index) {
        // Validate the index, store the old value, shift elements left, decrease size, and return the removed element
        isIndexValid(index);
        Object old = list[index];
        size--;
        System.arraycopy(list, index + 1, list, index, size);
        return (T) old;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator listIterator() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List subList(int fromIndex, int toIndex) {
        return List.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    private void isIndexValid(int index) {
        // Checks if the index is within the valid range for the list
        if (index >= 0 && index < size) {
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    private void ensureCapacity() {
        // If the current array is full, double its capacity and copy elements to the new array
        if (list.length <= size) {
            int newCapacity = list.length * 2;
            Object[] tempList = new Object[newCapacity];
            System.arraycopy(list, 0, tempList, 0, size);
            list = tempList;
        }
    }
}