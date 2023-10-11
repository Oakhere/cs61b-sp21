package deque;

public class Comparator {
    public int compare(int a, int b) {
        return Integer.compare(a, b);
    }

    public int compare(double a, double b) {
        return Double.compare(a, b);
    }

    public int compare(char a, char b) {
        return Character.compare(a, b);
    }

}
