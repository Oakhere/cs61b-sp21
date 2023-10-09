package deque;

import afu.org.checkerframework.checker.oigj.qual.O;

public class ArrayDeque<T> {
    private int size;
    private T[] items = (T[]) new Object[8]; // The starting length of the array is 8.
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        nextFirst = 8 - 1;
        nextLast = 0;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        size++;
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1) % items.length;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        size++;
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = nextFirst + 1; i < items.length; i++) {
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i < nextLast; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }
    
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        int first = (nextFirst + 1) % items.length;
        T itemToReturn = items[first];
        nextFirst = first;
        size--;
        // Check usage ratio
        if (items.length >= 16 && (double)size / items.length < 0.25) {
            resize(items.length / 4);
        }
        return itemToReturn;
    }

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
        if (items.length >= 16 && (double)size / items.length < 0.25) {
            resize(items.length / 4);
        }
        return itemToReturn;
    }

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
        nextLast = size - 1;
        items = a;
    }




}
