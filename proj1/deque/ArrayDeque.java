package deque;

public class ArrayDeque<T> {
    private int size;
    private T[] items = (T[]) new Object[8];
    // The starting length of the array is 8.
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        nextFirst = 8 - 1;
        nextLast = 0;
    }

    public void addFirst(T item) {
        items[nextFirst] = item;
        nextFirst--;
        size++;
        resizeUp();
    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast++;
        size++;
        resizeUp();
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
        T itemToReturn = items[(nextFirst + 1) % items.length];
        if (nextFirst < items.length - 1) {
            nextFirst++;
        }
        size--;
        resizeDown();
        return itemToReturn;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextLast--;
        size--;
        resizeDown();
        return items[nextLast];
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        return items[nextFirst - items.length];
    }

    /** For arrays of length 16 or more, if the usage ratio is less than 25%,
     * resize the size of the array down.*/
    public void resizeDown() {
        double usageRatio = (double) size / items.length;
        int RFACTOR = 3;
        if (items.length >= 16 && usageRatio < 0.25) {
            T[] a = (T[]) new Object[items.length / RFACTOR];
            System.arraycopy(items, 0, a, 0, size);
            items = a;
        }
    }
    /** Resize the size up by RFACTOR. */
    public void resizeUp() {
        int RFACTOR = 3;
        T[] a = (T[]) new Object[items.length * RFACTOR];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }


}
