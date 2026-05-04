package byow.graph;

/**
 * 带权无向边。
 * 无向边的两个方向被视为同一条边，即 (v, w) 与 (w, v) 相等。
 *
 * @param <T> 顶点类型
 */
public class UndirectedEdge<T> implements IEdge<T> {
    private final T v;
    private final T w;
    private final double weight;

    /**
     * 构造一条无向边。
     *
     * @param v      一个端点
     * @param w      另一个端点
     * @param weight 边的权重
     */
    public UndirectedEdge(T v, T w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public T either() {
        return v;
    }

    @Override
    public T other(T vertex) {
        if (vertex.equals(v)) return w;
        if (vertex.equals(w)) return v;
        throw new IllegalArgumentException("顶点 " + vertex + " 不是这条边的端点");
    }

    /**
     * 无向边的相等性判断：方向无关，权重必须相同。
     * 即 (v,w) 与 (v,w) 或 (w,v) 均视为相等。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UndirectedEdge<?> that = (UndirectedEdge<?>) o;
        // 方向无关：两种顺序之一匹配即可
        boolean direct = v.equals(that.v) && w.equals(that.w);
        boolean reverse = v.equals(that.w) && w.equals(that.v);
        return (direct || reverse) && Double.compare(weight, that.weight) == 0;
    }

    /**
     * 对称的哈希码，确保 (v,w) 和 (w,v) 产生相同的哈希值。
     * 使用异或 (XOR) 使顺序不影响结果。
     */
    @Override
    public int hashCode() {
        // 使用异或使顺序无关
        int result = v.hashCode() ^ w.hashCode();
        long temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s-%s(%.2f)", v, w, weight);
    }
}///