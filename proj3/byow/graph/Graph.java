package byow.graph;

import java.util.TreeMap;

public interface Graph<T> {
    int V();
    void addEdge(T v,T w);
    //返回一个顶点的邻居（可迭代）
    Iterable<T> adj(T v);
    //Ts
    TreeMap<T,Integer> correspond();
    T getVertex(int index);
    public int findCorrspondence(T v);
}
///