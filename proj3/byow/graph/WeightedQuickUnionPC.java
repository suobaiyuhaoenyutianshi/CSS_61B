package byow.graph;
//
/**
 * 加权快速合并 + 路径压缩 并查集。
 * 用于高效管理动态连通分量，均摊时间复杂度 O(α(N)) ≈ 常数。
 */
public class WeightedQuickUnionPC {
    private int[] parent;
    private int[] size;//该树对应大小
    public WeightedQuickUnionPC(int totalVertexs){
        this.parent = new int[totalVertexs];
        this.size = new int[totalVertexs];
        for(int i = 0;i < totalVertexs;i++){
            parent[i] = i;
            size[i] = 1;
        }
    }
    //别忘，输进去是对像对应的序号
    private int findFather(int i){
        if(i != parent[i]){
            parent[i] = findFather(parent[i]);
        }
        //相等找到，往回挂
        return parent[i];
    }

    public boolean connected(int x,int y){
        return findFather(x) == findFather(y);
    }
    //不联通才进入这里，所以不用写判断
    /** 合并 x 和 y 所在的分量，总是将小树挂到大树上 */
    //找到最终的父节点一定与对应序号相同，设计这样
    public void union(int x,int y){
        //找对应的父节点
        int xParent = findFather(x);
        int yParent = findFather(y);
        //写判断也行
        if(xParent == yParent) return;
        //对应大小
        int xTreeSize = size[xParent];
        int yTreeSize = size[yParent];
        if(xTreeSize < yTreeSize){
            //x的节点小接到y上
            parent[xParent] = yParent;
            size[yParent] += xTreeSize;
        }else{
            parent[yParent] = xParent;
            size[xParent] += yTreeSize;
        }
    }

}
//