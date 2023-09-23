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
        if (size == 0) {
            return true;
        }
        return  false;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line. */
    public void printDeque() {
        Node p = first;
        while (!p.equals(last)) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println(last.item);
    }
    
}
