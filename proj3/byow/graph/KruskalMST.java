package byow.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 一个连通图的最小生成树（MST）必然恰好有 V - 1 条边（V 为顶点数）。
 * Kruskal 算法：计算带权无向图的最小生成树 (MST)。
 * 核心思想：按边权升序考虑每条边，若该边连接了两个不同的连通分量，则加入 MST。
 * 使用并查集 (Union-Find) 高效判断连通性。
 * 时间复杂度：O(E log E) （主要来自排序）
 */
public class KruskalMST<T> {
    private final EdgeWeightedGraph<T> G;
    private List<IEdge<T>> MSTedges;
    List<IEdge<T>> edges = new ArrayList<>();
    private WeightedQuickUnionPC QuickUnionPc;
    public KruskalMST(EdgeWeightedGraph<T> G){
        this.G =G;
        MSTedges = new ArrayList<>();
       QuickUnionPc = new WeightedQuickUnionPC(G.V());
        //把所有边按权重加入edges
        for(IEdge<T> edge:G.edges()){
            edges.add(edge);
        }
        edges.sort(Comparator.comparingDouble(IEdge::weight));

        for(int i = 0;MSTedges.size() < G.V();i++){
            if(MSTedges.size() >= G.V() - 1){
                break;
            }
            IEdge<T> edge = edges.get(i);
            T e = edge.either();
            T o = edge.other(e);
            //对应序号
            int ether = G.indexOf(e);
            int other = G.indexOf(o);
            //不连通是两部分，可以运用切割最优反证，可行
            if(!QuickUnionPc.connected(ether,other)){
                QuickUnionPc.union(ether,other);
                MSTedges.add(edge);
            }

        }


        if (MSTedges.size() < G.V() - 1) {
            throw new IllegalStateException("此图不连通");
        }///
    }

    public Iterable<IEdge<T>> edges(){
        return MSTedges;
    }
    public double Weight(){
        double total = 0;
        for (IEdge<T> edge:MSTedges){
            total += edge.weight();
        }
        return total;
    }


}
