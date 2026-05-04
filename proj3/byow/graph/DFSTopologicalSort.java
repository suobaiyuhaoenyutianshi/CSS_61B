package byow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DFSTopologicalSort<T> implements TopologicalOrder<T> {
//
    private boolean[] marked;
    private boolean[] onStack;
    private List<Integer> postOrder;
    private EdgeWeightedDigraph<T> graph;

    @Override
    public List<Integer> getOrder(EdgeWeightedDigraph<T> graph) {
        this.graph = graph;
        int V = graph.V();
        marked = new boolean[V];
        onStack = new boolean[V];
        postOrder = new ArrayList<>();

        // 遍历所有顶点，保证非连通图也能得到完整顺序每条边
        //  u→v 都满足 v 先于 u 完成（后序中 v 排在 u 前面）
        //这个性质无论在哪个连通分量内部，都是一样成立的。翻转后，u 就排在了 v 前面。
        //2.
        //
        //DFS 后序逆序算法最精妙的地方——它天然支持非连通图，且对起点不敏感，当然
        for (int i = 0; i < V; i++) {
            if (!marked[i]) {
                dfs(graph.getKey(i));
            }
        }

        Collections.reverse(postOrder);
        return postOrder;
    }

    private void dfs(T v) {
        int vIdx = graph.indexOf(v);
        marked[vIdx] = true;
        onStack[vIdx] = true;//入栈

        for (IEdge<T> e : graph.adj(v)) {
            T w = e.other(v);
            int wIdx = graph.indexOf(w);
            if (!marked[wIdx]) {
                dfs(w);
            } else if (onStack[wIdx]) {//找到回边
                throw new IllegalArgumentException("图中存在环，无法进行拓扑排序。");
            }
        }

        onStack[vIdx] = false;// 出栈
        postOrder.add(vIdx);// 后序
    }
}/** A  onStack就是为检验这种情况的:拓扑排序的目标是给所有顶点一个线性顺序，保证每条边 u → v 中，u 排在 v 前面。
 也就是：所有依赖都向前走，决不回头
 ↓
 B ←──┐
 ↓    │
 C    │
 ↓    │
 D ───┘*///