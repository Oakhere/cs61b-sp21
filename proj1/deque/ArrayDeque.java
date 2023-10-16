package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private T[] items = (T[]) new Object[8]; // The starting length of the array is 8.
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        nextFirst = 8 - 1;
        nextLast = 0;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        size++;
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1) % items.length;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        size++;
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = nextFirst + 1; i < items.length; i++) {
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i < nextLast; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        int first = (nextFirst + 1) % items.length;
        T itemToReturn = items[first];
        nextFirst = first;
        size--;
        // Check usage ratio
        if (items.length >= 16 && (double) size / items.length < 0.25) {
            resize(items.length / 4);
        }
        return itemToReturn;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        int last = (nextLast - 1) % items.length;
        // The % operator in Java works differently than in Python
        // e.g. Make sure -1 % 8 is 7, not -1
        if (last < 0) {
            last += items.length;
        }
        T itemToReturn = items[last];
        nextLast = last;
        size--;
        if (items.length >= 16 && (double) size / items.length < 0.25) {
            resize(items.length / 4);
        }
        return itemToReturn;
    }

    @Override
    /** Gets the item at the given index, where 0 is the front. */
    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        return items[(nextFirst + index + 1) % items.length];
    }

    private void resize(int toSize) {
        T[] a = (T[]) new Object[toSize];
        // List all the elements in order in the new array.
        for (int i = 0; i < size; i++) {
            a[i] = get(i);
        }
        nextFirst = a.length - 1;
        nextLast = size;
        items = a;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        ArrayDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
            T itemToReturn = items[wizPos];
            wizPos += 1;
            return itemToReturn;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<T> oDeque = (ArrayDeque<T>) o;
        if (size != oDeque.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (items[i] != oDeque.items[i]) {
                return false;
            }
        }
        return true;
    }

}
