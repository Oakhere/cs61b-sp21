package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private Node<T> sentinel;
    private Node<T> first;
    private Node<T> last;
    private int size;

    private static class Node<T> {
        private Node<T> pre;
        private T item;
        private Node<T> next;
        private Node(Node<T> p, T i, Node<T> n) {
            item = i;
            pre = p;
            next = n;
        }
    }
    public LinkedListDeque() {
        sentinel = new Node<>(sentinel, null, sentinel);
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
        Node<T> p = new Node<>(sentinel, item, first);
        first.pre = p;
        sentinel.next = p;
        first = p;
        if (size == 0) {
            last = p;
        }
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        // Once the deque has at least one item, make sure sentinel.item is not null.
        if (sentinel.item == null) {
            sentinel.item = item;
        }
        Node<T> p = new Node<>(last, item, sentinel);
        last.next = p;
        sentinel.pre = p;
        last = p;
        if (size == 0) {
            first = p;
        }
        size += 1;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line. */
    public void printDeque() {
        Node<T> p = first;
        while (!p.equals(last)) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println(last.item);
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. */
    public T removeFirst() {
        if (sentinel.item == null || size == 0) {
            return null;
        }
        T itemToReturn = first.item;
        sentinel.next = first.next;
        first = first.next;
        first.pre = sentinel;
        size--;
        if (size == 0) {
            last = sentinel;
        }
        return  itemToReturn;
    }

    /** Removes and returns the item at the back of the deque.
    If no such item exists, returns null.*/
    public T removeLast() {
        if (sentinel.item == null || size == 0) {
            return null;
        }
        T itemToReturn = last.item;
        sentinel.pre = last.pre;
        last = last.pre;
        last.next = sentinel;
        size--;
        if (size == 0) {
            first = sentinel;
        }
        return  itemToReturn;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item,
     * and so forth. If no such item exists, returns null.
     * Must not alter the deque! */
    public T get(int index) {
        if (index + 1 > size) {
            return null;
        }
        Node<T> currentNode = first;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }
        return currentNode.item;
    }
    /** Same as the get method above, but use recursion. */
    public T getRecursive(int index) {
        if (index + 1 > size) {
            return null;
        }
        return getRecursiveHelper(index, first);
    }

    private T getRecursiveHelper(int index, Node<T> n) {
        if (index == 0) {
            return n.item;
        }
        return getRecursiveHelper(index - 1, n.next);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        int wizPos;

        LinkedListDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            T itemToReturn = get(wizPos);
            wizPos += 1;
            return itemToReturn;
        }

    }


    /** Returns whether the parameter o is equal to the Deque.
     * o is considered equal if it is a Deque and if it contains the same contents
     * (as governed by the generic T’s equals method) in the same order. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> oDeque = (Deque<T>) o;
        if (size != oDeque.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (get(i).equals(oDeque.get(i))) {
                return false;
            }
        }
        return true;
    }
}
