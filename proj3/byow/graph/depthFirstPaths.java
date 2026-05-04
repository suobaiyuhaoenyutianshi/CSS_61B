package byow.graph;

import java.util.*;

//基于深度优先搜索（DFS）找出无向图中从给定起点到所有连通顶点的可达路径,也适用与有向
//T节点对应的类型
//记住把T转化为序号先一定
public class depthFirstPaths<T> {
    //距离，代替marked
    private int[] distTo;
    private final int s;//起点通过对应关系找到
    private final TreeMap<T,Integer> corrspondence;//对应关系
    private int[] edgeTo;
    private Graph<T> graph;// edgeTo[w] = 到达 w 的前一个顶点（用于回溯路径）
    public depthFirstPaths(Graph<T> G,T v){
        this.graph = G;
        corrspondence = G.correspond();
        distTo =new int[G.V()];
        Arrays.fill(distTo, Integer.MAX_VALUE);//都设为最大值
        edgeTo = new int[G.V()];
        this.s =correspondNum(v) ;//起点对应的
        distTo[s] = 0;
        dfs(G,v);
    }///
    //通过对象T找到对应的序号，尤其是为edgeTo
    private int correspondNum(T v){
        return corrspondence.get(v);
    }
    public void dfs(Graph<T> G,T s){

        for(T w:G.adj(s)){

           int wNum = correspondNum(w);//对应序号
            if(distTo[wNum] != Integer.MAX_VALUE) continue;
            int v =correspondNum(s);
            edgeTo[wNum] = v;//来时路
            distTo[wNum] = distTo[v] + 1;
            dfs(G,w);
        }

    }

    /** 是否存在从起点到 v 的路径 */
    public boolean hasPathTo(T v) {
        return distTo[graph.findCorrspondence(v)] != Integer.MAX_VALUE;
    }

    /** 返回从起点到 v 的路径（顶点序列） */
    public Iterable<T> pathTo(T v) {
        if(!hasPathTo(v)) return null;
        List<T> path = new ArrayList<>();//加了在改
        for(int n = graph.findCorrspondence(v);n != s; n = edgeTo[n]){
            path.add(graph.getVertex(n));
        }
        path.add(graph.getVertex(s));
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
        depthFirstPaths<String> test2 = new depthFirstPaths<>(test,"a");
        test2.hasPathTo(c);
        for(String n:test2.pathTo(c)){
            System.out.print(n + "->");
        }



    }
}

