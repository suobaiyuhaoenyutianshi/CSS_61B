package byow.graph;
import java.util.*;
//其实也是树，不过依赖与顶点的最短路径树
/**
 * Dijkstra 算法：求解带权有向图（或无向图）单源最短路径。
 * 前提：所有边权重非负。
 * 核心数据结构：优先队列（按当前最短距离排序）+ 惰性删除。
 * 时间复杂度：O(E log V) （二叉堆实现）
 */
public class DijkstraSP<T> {
    private static class VertexDist<T>{
        final T vertex;
        final double dist;
        public VertexDist(T vertex,double d){
            this.vertex = vertex;
            this.dist = d;
        }

    }//
    @SuppressWarnings("uncheck")
    private double[] disTo;//距离
    private IEdge<T>[] edgeTo;//最短路径对应的边
    private PriorityQueue<VertexDist<T>> pq;//优先队列
    private final int s;//对应起点序号
    private final WeightedGraph<T> G;
    public DijkstraSP(WeightedGraph<T> G,T v) {
        this.s = G.indexOf(v);
        this.G = G;
        disTo = new double[G.V()];
        edgeTo = new IEdge[G.V()];
        Arrays.fill(disTo, Double.POSITIVE_INFINITY);
        disTo[s] = 0;
        pq = new PriorityQueue<>(Comparator.comparingDouble(vd -> vd.dist));
        pq.add(new VertexDist<>(v, 0));
        while (!pq.isEmpty()) {
            VertexDist vd = pq.poll();
            T TVertex = (T) vd.vertex;
            if (vd.dist > disTo[G.indexOf((TVertex))]) continue;
            for (IEdge<T> item : G.adj(TVertex)) {
                relex(item, TVertex);
            }
        }
    }
    private void relex(IEdge<T> item,T TVertex){
        T other = item.other(TVertex);
        if(disTo[G.indexOf(other)]  > disTo[G.indexOf(TVertex)] + item.weight()){
            double d = disTo[G.indexOf(other)] = disTo[G.indexOf(TVertex)] + item.weight();
            pq.add(new VertexDist<>(other,d));
            edgeTo[G.indexOf(other)] = item;
        }
    }


    /**
     * 返回从起点到顶点 v 的最短距离。
     */
    public double distTo(T v){
        int idx = G.indexOf(v);
        if(idx == -1) throw new IllegalArgumentException("该顶点不存在");
        return disTo[G.indexOf(v)];
    }

    /**
     * 是否存在从起点到顶点 v 的路径。
     */
    public boolean hasPathTo(T v){
        int idx = G.indexOf(v);
        return idx != -1 && disTo[idx] < Double.POSITIVE_INFINITY;
    }


    /**
     * 返回从起点到顶点 v 的最短路径上的所有边（顺序为从起点到终点）。
     */
    public Iterable<IEdge<T>> pathTo(T v){
        if (!hasPathTo(v)) return null;
        List<IEdge<T>> path = new ArrayList<>();
        for(int correspondNum = G.indexOf(v);correspondNum != s; correspondNum = G.indexOf(edgeTo[correspondNum].other(G.getKey(correspondNum)))){
            path.add(edgeTo[correspondNum]);
        }
        Collections.reverse(path);
        return path;
    }







}
//