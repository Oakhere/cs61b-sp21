package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K, V> {
    private BSTNode label;
    private BSTMap<K, V> left;
    private BSTMap<K, V> right;
    private int size = 0;

    private class BSTNode {
        private K key;
        private V value;
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public BSTMap() {
    }

    private BSTMap(BSTNode n) {
        this.label = n;
    }

    @Override
    public void clear() {
        this.label = null;
        this.left = null;
        this.right = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelper(this, key);
    }

    private boolean containsKeyHelper(BSTMap<K, V> t, K key) {
        if (t == null || t.label == null) {
            return false;
        }
        if (t.label.key.equals(key)) {
            return true;
        }
        if (key.compareTo(t.label.key) < 0) {
            return containsKeyHelper(t.left, key);
        }
        return containsKeyHelper(t.right, key);
    }

    @Override
    public V get(K key) {
        return getHelper(this, key);
    }

    private V getHelper(BSTMap<K, V> t, K key) {
        if (t == null || t.label == null) {
            return null;
        }
        if (t.label.key.equals(key)) {
            return t.label.value;
        }
        if (key.compareTo(t.label.key) < 0) {
            return getHelper(t.left, key);
        }
        return getHelper(t.right, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        BSTNode n = new BSTNode(key, value);
        putHelper(this, n);
        size += 1;
    }

    private BSTMap<K, V> putHelper(BSTMap<K, V> t, BSTNode n) {

        if (t == null) {
            return new BSTMap<>(n);
        }
        else if (t.label == null) {
            t.label = n;
        }
        if (n.key.compareTo(t.label.key) < 0) {
            t.left = putHelper(t.left, n);
        }
        else if (n.key.compareTo(t.label.key) > 0) {
            t.right = putHelper(t.right, n);
        }
        return t;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    public void printInOrder() {
        int indentation = 0;
        printInOrderHelper(this, indentation);
    }

    private void printInOrderHelper(BSTMap<K, V> t, int ind) {
        if (t.left == null && t.right == null) {
            for (int i = 0; i < ind; i += 1) {
                System.out.print(" ");
            }
            System.out.println(t.label.key);
            return;
        }
        for (int i = 0; i < ind; i += 1) {
            System.out.print(" ");
        }
        System.out.println(t.label.key);

        if (t.left != null) {
            printInOrderHelper(t.left, ind + 4);
        }
        if (t.right != null) {
            printInOrderHelper(t.right, ind + 4);
        }
    }
}
