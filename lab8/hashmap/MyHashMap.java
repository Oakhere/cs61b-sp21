package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Oak
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    final private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        this.buckets = createTable(16);
        this.loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        this.buckets = createTable(initialSize);
        this.loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table =  new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }
    @Override
    public void clear() {
        buckets = createTable(this.size);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int keyIndex = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> bucket = buckets[keyIndex];
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int keyIndex = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> bucket = buckets[keyIndex];
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        //If the same key has been inserted, update the value.
        int keyIndex = Math.floorMod(key.hashCode(), buckets.length);
        Collection<Node> bucket = buckets[keyIndex];
        for (Node n : bucket) {
            if (n.key.equals(key)) {
                n.value = value;
                return;
            }
        }
        buckets[keyIndex].add(createNode(key, value));
        size += 1;
        if ((double) size / buckets.length > loadFactor) {
            resize();
        }
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keySet = new HashSet<>();
        for (Collection<Node> b : buckets) {
            for (Node n : b) {
                keySet.add(n.key);
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    /** Return an Iterator that iterates over the stored keys. */
    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public void resize() {
        Collection<Node>[] newBuckets = createTable(buckets.length * 2);
        for (Collection<Node> b : buckets) {
            for (Node n : b) {
                int keyIndex = Math.floorMod(n.key.hashCode(), newBuckets.length);
                newBuckets[keyIndex].add(n);
            }
        }
        buckets = newBuckets;
    }

}
