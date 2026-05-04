package byow.graph;//

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
public class KahnTopologicalSort<T> implements TopologicalOrder<T> {
    @Override
    public List<Integer> getOrder(EdgeWeightedDigraph<T> graph){
        int V = graph.V();
        int[] inDegree = new int[V];
        //为他们算度数
        for(int i = 0;i<V;i++){
            T from = graph.getKey(i);
            for(IEdge<T> edge:graph.adj(from)){
                T e = edge.other(from);
                inDegree[graph.indexOf(e)]++;
            }
        }
        // /初始化队列,将一开始度数为0的0加进去
        Queue<Integer> initpq= new LinkedList<>();
        for(int i = 0;i< V;i++){
            if(inDegree[i] == 0){
                initpq.add(i);
            }
        }
        //检测，加入order,与bfs一样,在
        List<Integer> order = new LinkedList<>();
        while(!initpq.isEmpty()){
            int vertex = initpq.poll();
            order.add(vertex);
            T Tvertex = graph.getKey(vertex);
            for(IEdge<T> item:graph.adj(Tvertex)){
                T o = item.other(Tvertex);
                int onum = graph.indexOf(o);
                inDegree[onum]--;
                if(inDegree[onum] == 0){
                    initpq.add(onum);
                }


            }

        }

        // 环检测
        if (order.size() != V) {
            throw new IllegalArgumentException("图中存在环，无法进行拓扑排序。");
        }
        return order;
    }



//拨掉一个入度为 0 的顶点 → 加入结果 → 其邻居入度减 1 → 若新入度为 0 则进队列，最后检查总数。


}
