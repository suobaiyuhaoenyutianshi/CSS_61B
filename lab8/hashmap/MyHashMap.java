package hashmap;

import java.util.Collection;


import java.util.*;

public class MyHashMap<K, V> implements Map61B<K, V> {

    protected class Node {
        K key;
        V value;
        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Collection<Node>[] buckets;
    private int size;
    private int capacity;
    private double loadFactor;
    private Set<K> keys;          // 用于 keySet() 和 iterator()

    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialSize, double maxLoad) {
        this.capacity = initialSize;
        this.loadFactor = maxLoad;
        this.buckets = (Collection<Node>[]) new Collection[initialSize];
        this.keys = new HashSet<>();
        this.size = 0;
        // 可选：延迟初始化桶，这里先不填充
    }

    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    private int hash(K key) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    @Override
    public void put(K key, V value) {
        int idx = hash(key);
        if (buckets[idx] == null) {
            buckets[idx] = createBucket();
        }
        Collection<Node> bucket = buckets[idx];
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }
        bucket.add(createNode(key, value));
        size++;
        keys.add(key);

        if ((double) size / capacity > loadFactor) {
            resize(capacity * 2);
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Collection<Node>[] oldBuckets = buckets;
        this.buckets = (Collection<Node>[]) new Collection[newCapacity];
        this.capacity = newCapacity;
        this.size = 0;
        this.keys.clear();

        for (Collection<Node> bucket : oldBuckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    int newIdx = Math.floorMod(node.key.hashCode(), newCapacity);
                    if (buckets[newIdx] == null) {
                        buckets[newIdx] = createBucket();
                    }
                    buckets[newIdx].add(node);
                    size++;
                    keys.add(node.key);
                }
            }
        }
    }

    @Override
    public V get(K key) {
        int idx = hash(key);
        Collection<Node> bucket = buckets[idx];
        if (bucket == null) return null;
        for (Node n : bucket) {
            if (n.key.equals(key)) return n.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int idx = hash(key);
        Collection<Node> bucket = buckets[idx];
        if (bucket == null) return false;
        for (Node n : bucket) {
            if (n.key.equals(key)) return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        @SuppressWarnings("unchecked")
        Collection<Node>[] newBuckets = (Collection<Node>[]) new Collection[capacity];
        buckets = newBuckets;
        size = 0;
        keys.clear();
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("remove not supported in Lab 8");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove not supported in Lab 8");
    }
}