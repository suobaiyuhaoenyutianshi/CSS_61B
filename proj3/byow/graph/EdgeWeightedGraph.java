package byow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * 带权无向图，基于邻接表实现。
 * 不允许自环，不允许重复边。
 * 使用泛型顶点类型 T，内部维护对象与整数序号的映射以提高效率。
 *
 * @param <T> 顶点类型
 */
public class EdgeWeightedGraph<T> implements WeightedGraph<T>{
    private final int capacity;                 // 预设最大顶点数（数组容量）
    private final List<IEdge<T>>[] adj;          // 邻接表，adj[i] 存储与顶点 i 相连的所有边
    private final TreeMap<T, Integer> map;       // 对象 -> 内部序号
    private final List<T> keys;                  // 内部序号 -> 对象
    private int size;                            // 当前实际顶点数

    /**
     * 构造一个带权无向图。
     *
     * @param capacity 最大顶点数（邻接表数组容量）
     */
    @SuppressWarnings("unchecked")
    public EdgeWeightedGraph(int capacity) {
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
     * 添加一条无向边。
     * 不允许自环，不允许重复边（基于 equals 判断）。
     *
     * @param e 无向边（通常为 UndirectedEdge<T> 实例）
     */
    public void addEdge(IEdge<T> e) {
        T v = e.either();
        T w = e.other(v);
        if (v.equals(w)) {
            System.out.println("警告：自环不允许添加，已忽略 " + e);
            return;
        }

        int vIdx = addVertexIfAbsent(v);
        int wIdx = addVertexIfAbsent(w);

        // 因为 UndirectedEdge.equals 是对称的，只需检查一侧即可
        if (adj[vIdx].contains(e)) {
            System.out.println("警告：边已经存在，已忽略 " + e);
            return;
        }

        adj[vIdx].add(e);
        adj[wIdx].add(e);
    }

    /**
     * 返回与顶点 v 相连的所有边。
     */
    public Iterable<IEdge<T>> adj(T v) {
        int idx = indexOf(v);
        if (idx == -1) return Collections.emptyList();
        return adj[idx];
    }

    /**
     * 返回图中所有的边（每条边仅出现一次）。
     */
    public Iterable<IEdge<T>> edges() {
        List<IEdge<T>> list = new ArrayList<>();
        for (int v = 0; v < size; v++) {
            for (IEdge<T> e : adj[v]) {
                int vIdx = indexOf(e.either());
                int wIdx = indexOf(e.other(e.either()));
                // 只添加一次：当起点序号小于终点序号时才加入
                if (vIdx < wIdx) {
                    list.add(e);
                }
            }
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
        EdgeWeightedGraph<String> test = new EdgeWeightedGraph<>(5);
        test.addEdge(new UndirectedEdge<>("a","b",4));
        test.addEdge(new UndirectedEdge<>("b","e",3));
        test.addEdge(new UndirectedEdge<>("d","e",5));
        test.addEdge(new UndirectedEdge<>("c","d",4));
        test.addEdge(new UndirectedEdge<>("e","c",1));
        test.addEdge(new UndirectedEdge<>("a","c",1));
    }
}///