package byow.graph;

/**
 * 带权有向边。
 * 方向敏感，边 (v -> w) 与 (w -> v) 是两条不同的边。
 *
 * @param <T> 顶点类型
 */
public class DirectedEdge<T> implements IEdge<T> {
    private final T from;   // 起点
    private final T to;     // 终点
    private final double weight;

    /**
     * 构造一条有向边。
     *
     * @param from   起点
     * @param to     终点
     * @param weight 权重
     */
    public DirectedEdge(T from, T to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public double weight() {
        return weight;
    }

    /**
     * 对于有向边，约定返回起点。
     */
    @Override
    public T either() {
        return from;
    }

    @Override
    public T other(T vertex) {
        if (vertex.equals(from)) return to;
        if (vertex.equals(to)) return from;
        throw new IllegalArgumentException("顶点 " + vertex + " 不是这条边的端点");
    }

    /** 明确返回起点 */
    public T from() {
        return from;
    }

    /** 明确返回终点 */
    public T to() {
        return to;
    }

    /**
     * 有向边的相等性判断：必须起点、终点、权重完全相同。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedEdge<?> that = (DirectedEdge<?>) o;
        return from.equals(that.from) && to.equals(that.to) &&
                Double.compare(weight, that.weight) == 0;
    }

    /**
     * 有向边哈希码：依赖起点和终点的顺序。
     */
    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        long temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s->%s(%.2f)", from, to, weight);
    }
}///