package timingtest;
import edu.princeton.cs.algs4.Stopwatch;
import org.checkerframework.checker.units.qual.A;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        SLList<Integer> items = new SLList<>();
        int M = 10000;
        for (int N = 1000; N <= 128000; N *= 2) {
            Ns.addLast(N);
        }
        for (int i = 0; i < Ns.size(); i++) {
            int N = Ns.get(i);
            for (int j = 0; j < N; j++) {
                items.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < M; j++) {
                items.getLast();
            }
            times.addLast(sw.elapsedTime());
            opCounts.addLast(M);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
