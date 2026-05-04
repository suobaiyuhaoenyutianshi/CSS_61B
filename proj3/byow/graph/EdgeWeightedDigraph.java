package byow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * 带权有向图，基于邻接表实现。
 * 不允许自环，不允许重复的同向边。
 * 使用泛型顶点类型 T，内部维护对象与整数序号的映射。
 *
 * @param <T> 顶点类型
 */
public class EdgeWeightedDigraph<T> implements WeightedGraph<T> {
    private final int capacity;
    private final List<IEdge<T>>[] adj;
    private final TreeMap<T, Integer> map;
    private final List<T> keys;
    private int size;

    /**
     * 构造一个带权有向图。
     *
     * @param capacity 最大顶点数（邻接表数组容量）
     */
    @SuppressWarnings("unchecked")
    public EdgeWeightedDigraph(int capacity) {
        this.capacity = capacity;
        this.adj = (List<IEdge<T>>[]) new ArrayList[capacity];
        for (int i = 0; i < capacity; i++) {
            adj[i] = new ArrayList<>();
        }
        this.map = new TreeMap<>();
        this.keys = new ArrayList<>();
        this.size = 0;
    }

    /**
     * 获取对象对应的内部序号，若对象不存在则返回 -1。
     */
    public int indexOf(T v) {
        return map.getOrDefault(v, -1);
    }

    /**
     * 根据内部序号获取对应的对象。
     */
    public T getKey(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return keys.get(index);
    }

    /**
     * 如果顶点不存在则将其加入图中，并返回其内部序号。
     */
    private int addVertexIfAbsent(T v) {
        int idx = indexOf(v);
        if (idx == -1) {
            if (size >= capacity) {
                throw new IllegalStateException("顶点数量已超过预设容量 " + capacity);
            }
            idx = size;
            map.put(v, idx);
            keys.add(v);
            size++;
        }
        return idx;
    }

    /**
     * 添加一条有向边。
     * 不允许自环，不允许重复的同向边。
     *
     * @param e 有向边（通常为 DirectedEdge<T> 实例）
     */
    public void addEdge(IEdge<T> e) {
        // 有向边的 either() 约定返回起点
        T from = e.either();
        T to = e.other(from);
        if (from.equals(to)) {
            System.out.println("警告：自环不允许添加，已忽略 " + e);
            return;
        }

        int fromIdx = addVertexIfAbsent(from);
        addVertexIfAbsent(to);   // 确保终点也存在（但不使用返回值）

        if (adj[fromIdx].contains(e)) {
            System.out.println("警告：同向边已经存在，已忽略 " + e);
            return;
        }

        adj[fromIdx].add(e);
        // 有向图只存一次，不添加反向边
    }

    /**
     * 返回从顶点 v 出发的所有有向边。
     */
    public Iterable<IEdge<T>> adj(T v) {
        int idx = indexOf(v);
        if (idx == -1) return Collections.emptyList();
        return adj[idx];
    }

    /**
     * 返回图中所有的有向边。
     */
    public Iterable<IEdge<T>> edges() {
        List<IEdge<T>> list = new ArrayList<>();
        for (int v = 0; v < size; v++) {
            list.addAll(adj[v]);
        }
        return list;
    }

    /**
     * 返回当前图中实际的顶点数。
     */
    public int V() {
        return size;
    }

    /**
     * 返回预设的最大顶点容量。
     */
    public int capacity() {
        return capacity;
    }

    /**
     * 返回对象到序号的映射（供算法类使用）。
     */
    public TreeMap<T, Integer> correspondence() {
        return map;
    }
    static void main(String[] args){
        EdgeWeightedDigraph<String> test = new EdgeWeightedDigraph<>(5);
        test.addEdge(new DirectedEdge<>("a","b",4));
        test.addEdge(new DirectedEdge<>("b","e",3));
        test.addEdge(new DirectedEdge<>("d","e",5));
        test.addEdge(new DirectedEdge<>("c","d",4));
        test.addEdge(new DirectedEdge<>("e","c",1));
        test.addEdge(new DirectedEdge<>("a","c",1));
    }
}///