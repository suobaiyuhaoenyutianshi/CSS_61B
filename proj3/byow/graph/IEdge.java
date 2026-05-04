package byow.graph;

/**
 * 带权边的通用接口（适用于有向图和无向图）
 * @param <T> 顶点类型
 */
public interface IEdge<T> extends Comparable<IEdge<T>> {

    /** 返回边的权重 */
    double weight();

    /** 返回边的其中一个端点（对于有向边，默认返回起点） */
    T either();

    /** 给定边的一个端点，返回另一个端点 */
    T other(T vertex);

    /** 默认比较方式：按权重升序 */
    @Override
    default int compareTo(IEdge<T> that) {
        return Double.compare(this.weight(), that.weight());
    }
}///