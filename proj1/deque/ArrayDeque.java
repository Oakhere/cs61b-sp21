package deque;

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
        items[nextFirst] = item;
        nextFirst--;
        size++;
        if (size == items.length) {
            //resizeUp();
        }
    }

    public void addLast(T item) {
        items[nextLast] = item;
        nextLast++;
        size++;
        if (size == items.length) {
            //resizeUp();
        }
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
        int first = firstHelper(nextFirst);
        T itemToReturn = items[first];
        size--;
        nextFirst = first;
        //resizeDown();
        return itemToReturn;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        int last = lastHelper(nextLast);
        T itemToReturn = items[last];
        size--;
        nextLast = last;
        //resizeDown();
        return itemToReturn;
    }

    /** Gets the item at the given index, where 0 is the front. */
    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        return items[(nextFirst + index + 1) % items.length];
    }

    /** For arrays of length 16 or more, if the usage ratio is less than 25%,
     * resize the size of the array down.*/
    private void resizeDown() {
        double usageRatio = (double) size / items.length;
        int RFACTOR = 2;
        if (items.length >= 16 && usageRatio < 0.25) {
            T[] a = (T[]) new Object[items.length / RFACTOR];
            System.arraycopy(items, 0, a, 0, size);
            items = a;
        }
    }
    /** Resize the size up by RFACTOR. */
    private void resizeUp() {
        int RFACTOR = 2;
        T[] a = (T[]) new Object[items.length * RFACTOR];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    /** Given nextLast, returns the last index of the deque. */
    private int lastHelper(int nextLast) {
        if (nextLast > 0) {
            return nextLast - 1;
        }
        return items.length - 1;
    }
    /** Given nextFirst, returns the first index of the deque. */
    private int firstHelper(int nextFirst) {
        if (nextFirst < 7) {
            return nextFirst + 1;
        }
        return 0;
    }


}
