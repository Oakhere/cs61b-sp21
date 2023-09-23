package deque;

import jh61b.junit.In;

public class LinkedListDeque<T> {
    private Node sentinel;
    private Node first;
    private Node last;
    private int size;

    public static class Node<T> {
        public Node pre;
        public T item;
        public Node next;
        public Node(Node p, T i, Node n) {
            item = i;
            pre = p;
            next = n;
        }
    }
    public LinkedListDeque() {
        sentinel = new Node(sentinel, null, sentinel);
        size = 0;
        first = sentinel;
        last = sentinel;
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        // Once the deque has at least one item, make sure sentinel.item is not null.
        if (sentinel.item == null) {
            sentinel.item = item;
        }
        size += 1;
        Node p = new Node(sentinel, item, first);
        first.pre = p;
        sentinel.next = p;
        first = p;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        // Once the deque has at least one item, make sure sentinel.item is not null.
        if (sentinel.item == null) {
            sentinel.item = item;
        }
        size += 1;
        Node p = new Node(last, item, sentinel);
        last.next = p;
        sentinel.pre = p;
        last = p;
    }

    /** Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty() {

    }
}
