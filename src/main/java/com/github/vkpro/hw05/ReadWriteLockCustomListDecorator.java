package com.github.vkpro.hw05;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread-safe decorator for CustomList that uses ReentrantReadWriteLock
 */
public class ReadWriteLockCustomListDecorator<T> implements List<T> {

    private final List<T> customList;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ReadWriteLockCustomListDecorator(List<T> customList) {
        this.customList = customList;
    }

    // READ OPERATIONS
    @Override
    public int size() {
        lock.readLock().lock();
        try { return customList.size(); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try { return customList.isEmpty(); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public boolean contains(Object element) {
        lock.readLock().lock();
        try { return customList.contains(element); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public T get(int index) {
        lock.readLock().lock();
        try { return customList.get(index); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public int indexOf(Object o) {
        lock.readLock().lock();
        try { return customList.indexOf(o); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.readLock().lock();
        try { return customList.lastIndexOf(o); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public Iterator<T> iterator() {
        lock.readLock().lock();
        try { return customList.iterator(); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public Object[] toArray() {
        lock.readLock().lock();
        try { return customList.toArray(); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public <U> U[] toArray(U[] a) {
        lock.readLock().lock();
        try { return customList.toArray(a); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        lock.readLock().lock();
        try { return customList.containsAll(c); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public ListIterator<T> listIterator() {
        lock.readLock().lock();
        try { return customList.listIterator(); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        lock.readLock().lock();
        try { return customList.listIterator(index); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        lock.readLock().lock();
        try { return customList.subList(fromIndex, toIndex); }
        finally { lock.readLock().unlock(); }
    }

    // WRITE OPERATIONS
    @Override
    public boolean add(T element) {
        lock.writeLock().lock();
        try { return customList.add(element); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public void add(int index, T element) {
        lock.writeLock().lock();
        try { customList.add(index, element); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public boolean remove(Object element) {
        lock.writeLock().lock();
        try { return customList.remove(element); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public T remove(int index) {
        lock.writeLock().lock();
        try { return customList.remove(index); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public T set(int index, T element) {
        lock.writeLock().lock();
        try { return customList.set(index, element); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        lock.writeLock().lock();
        try { return customList.addAll(c); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        lock.writeLock().lock();
        try { return customList.addAll(index, c); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        lock.writeLock().lock();
        try { return customList.removeAll(c); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.writeLock().lock();
        try { return customList.retainAll(c); }
        finally { lock.writeLock().unlock(); }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try { customList.clear(); }
        finally { lock.writeLock().unlock(); }
    }
}