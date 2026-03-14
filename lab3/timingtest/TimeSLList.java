package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, ArrayList<Integer> opCounts) {
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


        // TODO: YOUR CODE HERE
        // 已有的 printTimingTable 方法（这里省略，保持原样）

        public static void main (String[]args){
            timeAListConstruction();
        }

        public static void timeAListConstruction () {
            // 定义要测试的列表大小 N
            int[] testSizes = {1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};

            // 准备存储数据的 ArrayList
            AList<Integer> Ns = new AList<>();
            AList<Double> times = new AList<>();
            ArrayList<Integer> opCounts = new ArrayList<>();

            // 对每一个 N 进行测试
            for (int N : testSizes) {
                // 创建一个新的 AList
                AList<Integer> list = new AList<>();

                // 开始计时
                Stopwatch sw = new Stopwatch();

                // 执行 N 次 addLast 操作
                for (int i = 0; i < N; i++) {
                    list.addLast(i); // 添加任意整数
                }

                // 结束计时，获取耗时（秒）
                double timeInSeconds = sw.elapsedTime();

                // 记录数据
                Ns.add(N);
                times.add(timeInSeconds);
                opCounts.add(N); // 操作次数就是 N
            }

            // 打印表格
            printTimingTable(Ns, times, opCounts);
        }

}
