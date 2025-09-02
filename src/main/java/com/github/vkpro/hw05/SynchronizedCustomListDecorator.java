package com.github.vkpro.hw05;

import java.util.*;

/**
 * A thread-safe decorator for CustomList that uses synchronized methods
 */
public class SynchronizedCustomListDecorator<T> implements List<T> {

    private final List<T> customList;

    public SynchronizedCustomListDecorator(List<T> customList) {
        this.customList = Objects.requireNonNull(customList);
    }

    @Override
    public synchronized int size() {
        return customList.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return customList.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object element) {
        return customList.contains(element);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return customList.iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return customList.toArray();
    }

    @Override
    public synchronized <U> U[] toArray(U[] a) {
        return customList.toArray(a);
    }

    @Override
    public synchronized boolean add(T element) {
        return customList.add(element);
    }

    @Override
    public synchronized boolean remove(Object element) {
        return customList.remove(element);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return customList.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        return customList.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends T> c) {
        return customList.addAll(index, c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return customList.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return customList.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        customList.clear();
    }

    @Override
    public synchronized T get(int index) {
        return customList.get(index);
    }

    @Override
    public synchronized T set(int index, T element) {
        return customList.set(index, element);
    }

    @Override
    public synchronized void add(int index, T element) {
        customList.add(index, element);
    }

    @Override
    public synchronized T remove(int index) {
        return customList.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return customList.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return customList.lastIndexOf(o);
    }

    @Override
    public synchronized ListIterator<T> listIterator() {
        return customList.listIterator();
    }

    @Override
    public synchronized ListIterator<T> listIterator(int index) {
        return customList.listIterator(index);
    }

    @Override
    public synchronized List<T> subList(int fromIndex, int toIndex) {
        return customList.subList(fromIndex, toIndex);
    }
}