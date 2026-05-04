package byow.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
//
public class digraph<T> implements Graph<T>{
    private final int SUM_V;
    private final List<T>[] adj;
    //要创建个映射
    public TreeMap<T,Integer> correspondence;
    private int tail;
    private List<T> keys;   // 序号 → 对象
    @SuppressWarnings("unchecked")
    public digraph(int sumV){
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

    //添加一条有向边,不能添加重复字符，可以a:a环，但不能a:b,b
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
            keys.add(v);
            wNum = tail;
            tail += 1;
        }

        if(adj[vNum].contains(w)){
            System.out.println("此边已经添加");
            return;
        }
        adj[vNum].add(w);

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
    //返回序号对应的T
    public T getVertex(int index) {
        return keys.get(index);
    }

    public static void main(String[] args){
        digraph<String> test = new digraph<>(6);
        String a = "a";
        String b ="b";
        String c ="c";String d ="d";String e = "e";String f = "f";

        test.addEdge(a,b);
        test.addEdge(a,e);
        test.addEdge(e,b);
        test.addEdge(e,f);
        test.addEdge(f,d);
        test.addEdge(b,d);
        test.addEdge(d,c);
        for(String dox :test.adj("a")){
            System.out.println(dox);
        }
         test.adj(d);
    }
    //返回对应的序号
    public TreeMap<T,Integer> correspond(){
        return this.correspondence;
    }
}
//