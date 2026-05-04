package byow.graph;

import java.util.TreeMap;

/**
 * 带权图的公共接口（可适用于有向或无向图）。
 * 提供顶点数、边迭代、邻接边迭代以及添加边的操作。
 *
 * @param <T> 顶点类型
 */
public interface WeightedGraph<T> {
    /**
     * 返回图中当前的顶点数。
     */
    int V();

    /**
     * 添加一条带权边。
     * 具体行为（有向/无向）由实现类决定。
     *
     * @param e 边对象（实现 IEdge<T> 接口）
     */
    void addEdge(IEdge<T> e);

    /**
     * 返回与顶点 v 关联的所有边。
     * 对于有向图，返回从 v 出发的边；对于无向图，返回与 v 相连的所有边。
     *
     * @param v 顶点
     * @return 可迭代的边集合
     */
    Iterable<IEdge<T>> adj(T v);

    /**
     * 返回图中所有的边。
     * 对于无向图，每条边只出现一次；对于有向图，返回所有有向边。
     *
     * @return 可迭代的边集合
     */
    Iterable<IEdge<T>> edges();

    /**
     * 返回对象到内部序号的映射。
     * 主要用于算法实现中需要快速获取顶点序号的情况。
     *
     * @return 从顶点对象到整数序号的 TreeMap
     */
    TreeMap<T, Integer> correspondence();

    /**
     * 根据内部序号获取对应的顶点对象。
     * 可选方法，但为了方便路径回溯通常提供。
     *
     * @param index 内部序号
     * @return 顶点对象
     */
    T getKey(int index);

    /**
     * 获取顶点对象对应的内部序号，若不存在则返回 -1。
     *
     * @param v 顶点对象
     * @return 内部序号，-1 表示不存在
     */
    int indexOf(T v);
}////