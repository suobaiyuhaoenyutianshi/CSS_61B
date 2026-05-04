package byow.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class DAGSP<T> {
    private EdgeWeightedDigraph<T> G;
    private int s;
    private double[] disTo;
    private IEdge<T>[] edgeTo;

    /**
     * @param G         有向无环图
     * @param start     起点
     * @param topoAlgo  拓扑排序策略（注入 TopologicalOrder 的实现）
     */
    public DAGSP(EdgeWeightedDigraph<T> G, T start, TopologicalOrder<T> topoAlgo) {
        this.G = G;
        this.s = G.indexOf(start);
        disTo = new double[G.V()];
        edgeTo = new IEdge[G.V()];
        Arrays.fill(disTo, Double.POSITIVE_INFINITY);
        disTo[s] = 0;

        // 通过接口获取拓扑顺序（内部已含环检测）
        List<Integer> order = topoAlgo.getOrder(G);

        // 按顺序松弛
        for (int vIdx : order) {
            if (disTo[vIdx] == Double.POSITIVE_INFINITY) continue; // 不可达
            T from = G.getKey(vIdx);
            for (IEdge<T> e : G.adj(from)) {
                relax(e, from);
            }
        }
    }

    private void relax(IEdge<T> e, T from) {
        int u = G.indexOf(from);
        T to = e.other(from);
        int w = G.indexOf(to);
        if (disTo[w] > disTo[u] + e.weight()) {
            disTo[w] = disTo[u] + e.weight();
            edgeTo[w] = e;
        }
    }
///
    //
    /** 返回从起点到顶点 v 的最短距离 */
    public double distTo(T v) {
        int idx = G.indexOf(v);
        return disTo[idx];
    }
    /** 是否存在从起点到顶点 v 的路径 */
    public boolean hasPathTo(T v) {
        int idx = G.indexOf(v);
        return disTo[idx] < Double.POSITIVE_INFINITY;
    }
    /** 返回从起点到顶点 v 的最短路径（边的序列） */
    public List<IEdge<T>> pathTo(T v){
        int index = G.indexOf(v);
        List<IEdge<T>> path = new ArrayList<>();
        if(!hasPathTo(v)) return null;
        for(int correspondNum = index;correspondNum != s;correspondNum = G.indexOf(edgeTo[correspondNum].other(G.getKey(correspondNum)))){
            path.add(edgeTo[correspondNum]);
        }
        Collections.reverse(path);
        return path;
    }
}