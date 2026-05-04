package byow.graph;
import java.util.List;

/**
 * 拓扑排序接口：给定一个有向图，返回一个合法的拓扑序列（顶点索引列表）。
 * 若图中存在环，实现类应抛出 IllegalArgumentException 或返回空列表（根据需要约定）。
 */
/**
 * 计算拓扑顺序
 *  有向图（EdgeWeightedDigraph）
 * @return 顶点索引列表，按拓扑顺序排列
 * @throws IllegalArgumentException 如果图中包含环，无法进行拓扑排序
 */
public interface TopologicalOrder<T> {
    List<Integer> getOrder(EdgeWeightedDigraph<T> graph);
}
///