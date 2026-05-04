package byow.graph;

import java.util.*;

/**
 * 广度优先搜索 (BFS)：找出从起点到所有顶点的最短路径（仅限无权图或等权图）。
 * 核心数据结构：队列（显式）。
 * 时间复杂度：O(V + E)，空间复杂度：O(V)。
 * 无权图
 * 该实现可直接用于无权无向图或有向图的单源最短路径查询、连通性判断、层序遍历等。
 * 若用于带权图（边权非均等），
 * 则不能保证最短路径的正确性，需改用 Dijkstra 或 Bellman‑Ford 算法
 */
public class BreadthFirstPaths<T> {
    private int[] disTo;
    private int[] edgeTo;
    private final int s;//起点对应的序号
    private Graph<T> G; //图
    public BreadthFirstPaths(Graph<T> G,T v){
        this.G = G;
        disTo = new int[G.V()];
        Arrays.fill(disTo,Integer.MAX_VALUE);
        this.s = G.findCorrspondence(v);
        edgeTo = new int[G.V()];
        disTo[s] = 0;
        bfs(G,v);///
    }
    private void bfs(Graph<T> G ,T v){
        Queue<T> q = new LinkedList<>();
        //权重相等，找到即最短
        q.add(v);
        while(!q.isEmpty()){
            T w = q.poll();
            for(T vertex:G.adj(w)){

                if(disTo[G.findCorrspondence(vertex)] != Integer.MAX_VALUE) continue;
                edgeTo[G.findCorrspondence(vertex)] = G.findCorrspondence(w);
                disTo[G.findCorrspondence(vertex)] = disTo[G.findCorrspondence(w)] + 1;
                q.add(vertex);
            }

        }
    }
    public boolean hasPathTo(T v) {
        return disTo[G.findCorrspondence(v)] != Integer.MAX_VALUE;
    }
    public Iterable<T> pathTo(T v){
        if(!hasPathTo(v)) return null;
        List<T> path = new ArrayList<>();
        for(int correspondNum = G.findCorrspondence(v);correspondNum != s ;correspondNum = edgeTo[correspondNum]){
            path.add(G.getVertex(correspondNum));
        }
        path.add(G.getVertex(s));
        Collections.reverse(path);
        return path;
    }
    public static void main(String[] args){
        Graph<String> test = new adjacencyList<>(6);
        String a = "a";
        String b ="b";
        String c ="c";String d ="d";String e = "e";String f = "f";
//有向
        test.addEdge(a,b);
        test.addEdge(a,e);
        test.addEdge(e,b);
        test.addEdge(e,f);
        test.addEdge(f,d);
        test.addEdge(b,d);
        test.addEdge(d,c);
        BreadthFirstPaths<String> test2 = new BreadthFirstPaths<>(test,"a");
        test2.hasPathTo(c);
        for(String n:test2.pathTo(c)){
            System.out.print(n + "->");
        }



    }
}
