package timingtest;

import edu.princeton.cs.algs4.Stopwatch;

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
        int[] testSizes = {1000, 2000, 4000, 800, 19000};
        int M = 10000; // 每个 N 执行 getLast 的次数

        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        for (int N : testSizes) {
            // 1. 创建 SLList 并添加 N 个元素
            SLList<Integer> list = new SLList<>();
            for (int i = 0; i < N; i++) {
                list.addLast(i);
            }

            // 2. 开始计时
            Stopwatch sw = new Stopwatch();

            // 3. 执行 M 次 getLast
            for (int j = 0; j < M; j++) {
                list.getLast();
            }

            // 4. 记录时间
            double timeInSeconds = sw.elapsedTime();

            // 5. 存储数据
            Ns.addLast(N);
            times.addLast(timeInSeconds);
            opCounts.addLast(M);
        }

        printTimingTable(Ns, times, opCounts);
    }
}