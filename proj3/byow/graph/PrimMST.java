package byow.graph;
import java.util.*;

/**
 * 树全局，不依赖顶点，最小树
 * 无向
 * 运用的是两个部分分割的，反证的思想
 * Prim 算法：计算带权无向图的最小生成树 (MST)。
 * 核心思想：从任意起点开始，每次选择连接“树内”与“树外”的最小权重边。
 * 实现与 Dijkstra 几乎相同，仅松弛条件不同（比较单边权重而非累加距离）。
 * 时间复杂度：O(E log V) （二叉堆 + 惰性删除）
 * 不过他返回的时它收集的边，毕竟时树
 * 树是全局的属性，无向任意顶点都可，选0方便记
 * 这里distTo不是深度
 */
public class PrimMST<T> {
    private static class VertexDist<T>{
        private T Vertex;
        private double dist;
        public VertexDist(T vertex,double d){
            this.dist =d;
            this.Vertex = vertex;
        }
    }
    private boolean[] marked; // 1. 添加 marked 数组
    private double[] disTo;
    private UndirectedEdge<T>[] edgeTo;
    private PriorityQueue<VertexDist<T>> pq;
    private final EdgeWeightedGraph<T> G;
    public PrimMST(EdgeWeightedGraph<T> G){
        this.G =G;
        marked = new boolean[G.V()]; // 2. 初始化 marked 数组

        disTo = new double[G.V()];
        Arrays.fill(disTo,Double.POSITIVE_INFINITY);
        edgeTo = new UndirectedEdge[G.V()];
        pq = new PriorityQueue<>(Comparator.comparingDouble(Vd->Vd.dist));
        //顺便设个起点,设为序号对应的0
        int s = 0;
        pq.add(new VertexDist<>(G.getKey(s),s));
        while(!pq.isEmpty()){
            VertexDist<T> vd = pq.poll();
            T TVertex = vd.Vertex;
            if(vd.dist > disTo[G.indexOf(TVertex)]) continue;
            if(marked[G.indexOf(TVertex)]) continue; // 关键：跳过已经在树中的顶点
            marked[G.indexOf(TVertex)] = true; // 4. 将当前顶点标记为已访问

            for( IEdge<T>item: G.adj(TVertex)){
                relex(item,TVertex);
            }
        }

    }///
    /** 松弛：检查边 e（从 v 出发）是否提供到达 w 的更小横跨权重 */
    private void relex(IEdge<T> item ,T Tvertex){
        T o = item.other(Tvertex);
        int oNum = G.indexOf(o);
        if(marked[oNum]) return; // 5. 如果邻接点已在树中，则不处理
        if(disTo[oNum] > item.weight()){
            disTo[oNum] = item.weight();
            edgeTo[oNum] = (UndirectedEdge<T>) item;
            pq.add(new VertexDist<>(o,disTo[oNum]));
        }
    }

    public Iterable<IEdge<T>> edges(){
        List<IEdge<T>> mst =new ArrayList<>();
        //从一开始，我没为0的edgeTo加边 ,每个顶点一定到达
        for(int i =1;i <G.V();i++){
            mst.add(edgeTo[i]);
        }
        return mst;
    }

//返回该树总权重
    public double weight() {
      double total = 0.0;
      for (IEdge e : edges()) total += e.weight();
      return total;
    }

    static void main(String[] args){
        EdgeWeightedGraph<String> G = new EdgeWeightedGraph<>(5);
        G.addEdge(new UndirectedEdge<>("a","b",4) );
        G.addEdge(new UndirectedEdge<>("a","c",1) );
        G.addEdge(new UndirectedEdge<>("b","e",3) );
        G.addEdge(new UndirectedEdge<>("c","d",4) );
        G.addEdge(new UndirectedEdge<>("e","c",1) );
        G.addEdge(new UndirectedEdge<>("e","d",4) );
        G.addEdge(new UndirectedEdge<>("c","b",4) );

        PrimMST<String> test =new PrimMST<>(G);
        for(IEdge<String> item:test.edges()){
            System.out.println(item);
        }
    }













/**vd.dist > distTo[vIdx] 是惰性删除：
 * 当从优先队列中取出的距离大于当前记录的 distTo 时，
 * 说明这条记录已过时（因为之后发现了更短的边来连接这个顶点），直接跳过。

 marked[vIdx] 是防止重复处理：
 即使队列中某条记录的距离刚好等于 distTo[vIdx]（即未被惰性删除淘汰），
 但它对应的顶点可能已经被正式加入了生成树。如果不检查 marked，
 就会把该顶点再次当作“树外”顶点处理，导致重复松弛、错误更新 edgeTo、甚至破坏 MST 性质。
 所以，加上 marked 检查是保障算法鲁棒性的标准做法。它明确表达了“每次只处理还未加入树的顶点”，
 从根本上杜绝了已加入顶点被再次考虑的风险——无论 relax 条件如何变化，这都是最安全的逻辑。*/






















}
