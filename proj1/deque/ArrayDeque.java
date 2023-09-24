package deque;

public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        // The starting length of the array is 8.
        T[] items = (T[]) new Object[8];
        nextFirst = 8 - 1;
        nextLast = 0;
    }

    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst--;
        size++;
    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast++;
        size++;
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
        nextFirst++;
        size--;
        return items[nextFirst + 1];
    }

    public T removeLast() {
        nextLast--;
        size--;
        return items[nextLast - 1];
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        return items[nextFirst - items.length];
    }


}
