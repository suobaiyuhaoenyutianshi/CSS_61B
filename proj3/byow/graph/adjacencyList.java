package byow.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
//
//无向的，改成有向只需要在addEg哪里删掉都填加
public class adjacencyList<T> implements Graph<T>{
    private final int SUM_V;
    private final List<T>[] adj;
    //要创建个映射
    public TreeMap<T,Integer> correspondence;
    private int tail;
    private List<T> keys;
    @SuppressWarnings("unchecked")
    public adjacencyList(int sumV){
        this.SUM_V = sumV;
        adj = (List<T>[]) new ArrayList[SUM_V];
        correspondence = new TreeMap<>();
        keys = new ArrayList<>();
        for(int v = 0;v < SUM_V;v++){
            adj[v] = new ArrayList<>();
        }
        tail = 0;
    }
    //写个通过T找到对应序号
    public int findCorrspondence(T v){
         Integer corrependingNum = correspondence.get(v);//找不到返回 -1
        if(corrependingNum == null){
            return  -1;
        }
        return corrependingNum;
    }

    //添加一条无向边,不能添加重复字符，可以a:a环，但不能a:b,b
    public void addEdge(T v ,T w){
        int vNum = findCorrspondence(v);
        int wNum = findCorrspondence(w);
        if(vNum == -1){
            correspondence.put(v,tail);
            keys.add(v);
            vNum = tail;
            tail += 1;
        }
        if (wNum == -1){
            correspondence.put(w,tail);
            keys.add(w);
            wNum = tail;
            tail += 1;
        }
        //我这个是无向图一定两边都有，只查一个就行
        if(adj[vNum].contains(w)){
            System.out.println("此边已经添加");
            return;
        }
        adj[vNum].add(w);
        adj[wNum].add(v);

    }
    //返回图的顶点数
    public int V(){
       return tail;
    }
    /** 返回顶点 v 的所有邻居（可迭代） */
    public Iterable<T> adj(T a){
        int correspondingNum = findCorrspondence(a);
        return adj[correspondingNum];
    }
    //返回对应的序号
    public TreeMap<T,Integer> correspond(){
        return this.correspondence;
    }
    //返回序号对应的T
    public T getVertex(int index) {
        return keys.get(index);
    }

}
//错了，顶点数知道，不能改，回去修改//