package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        T maxItem = get(0);
        for (int i = 0; i < size() - 1; i++) {
            if (comparator.compare(get(i), get(i + 1)) < 0) {
                maxItem = get(i + 1);
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        T maxItem = get(0);
        for (int i = 0; i < size() - 1; i++) {
            if (c.compare(get(i), get(i + 1)) < 0) {
                maxItem = get(i + 1);
            }
        }
        return maxItem;
    }

}
