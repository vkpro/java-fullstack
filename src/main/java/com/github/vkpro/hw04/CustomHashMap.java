package com.github.vkpro.hw04;

import java.util.*;

public class CustomHashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table; // Buckets array
    private int size = 0;

    private int threshold; // Resize threshold
    private final float loadFactor;

    public CustomHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public CustomHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.table = new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    static class Node<K, V> implements Entry<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = getEntry(key);
        return (node == null) ? null : node.value;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if ((key == null && e.key == null) || (key != null && key.equals(e.key))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        addEntry(key, value, index);
        return null;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> e = removeEntryForKey(key);
        return (e == null) ? null : e.value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K, V> e : table) {
            for (; e != null; e = e.next) {
                if (value == null ? e.value == null : value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Set<K> keySet() {
        Set<K> ks = new HashSet<>();
        for (Node<K, V> e : table) {
            while (e != null) {
                ks.add(e.key);
                e = e.next;
            }
        }
        return ks;
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = new ArrayList<>();
        for (Node<K, V> e : table) {
            while (e != null) {
                vs.add(e.value);
                e = e.next;
            }
        }
        return vs;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es = new HashSet<>();
        for (Node<K, V> e : table) {
            while (e != null) {
                es.add(e);
                e = e.next;
            }
        }
        return es;
    }

    private Node<K, V> getEntry(Object key) {
        if (size == 0) return null;

        int hash = (key == null) ? 0 : key.hashCode();
        int index = indexFor(hash, table.length);

        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if ((key == null && e.key == null) || (key != null && key.equals(e.key))) {
                return e;
            }
        }
        return null;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    // Use % with adjustment for negative hashes
    private int indexFor(int hash, int length) {
        int idx = hash % length;
        return (idx < 0) ? idx + length : idx;
    }

    private void addEntry(K key, V value, int bucketIndex) {
        if (size >= threshold) {
            resize(2 * table.length);
            bucketIndex = indexFor(hash(key), table.length);
        }

        Node<K, V> e = table[bucketIndex];
        table[bucketIndex] = new Node<>(key, value, e);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Node<K, V>[] newTable) {
        int newCapacity = newTable.length;
        for (Node<K, V> e : table) {
            while (e != null) {
                Node<K, V> next = e.next;
                int index = indexFor(hash(e.key), newCapacity);
                e.next = newTable[index];
                newTable[index] = e;
                e = next;
            }
        }
    }

    private Node<K, V> removeEntryForKey(Object key) {
        if (size == 0) return null;

        int hash = (key == null) ? 0 : key.hashCode();
        int index = indexFor(hash, table.length);

        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(key, current.key)) { // handles null safely
                size--;
                if (prev == null) {
                    table[index] = current.next;   // removing head
                } else {
                    prev.next = current.next;      // bypass current
                }
                return current;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

}
