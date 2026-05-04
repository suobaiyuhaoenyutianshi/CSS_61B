package byow.graph;
/**
 * 带权有向边（也可用于无向图，只需添加时两个方向各加一次）
 * 带权边与带权图
 *///
public class Edge<T> implements Comparable<Edge>{
    private final T v;
    private final T w;
    private final double weight;
    public Edge(T v, T w,double weight){
        this.v = v;
        this.w = w;
        this.weight = weight;
    }
    //这两个感觉没什么用尤其是在图的adj的时候
    public T either(){
        return v;
    }
    //返回对应的顶点，v就w,w就v
    public T other(T vertex){
        if(vertex.equals(w))return v;
        else if (vertex.equals(v)) {
            return w;
        }
        else throw new IllegalArgumentException("Illegal endpoint");
    }
    public T getV(){
        return v;
    }
    public T getW(){
        return w;
    }
    public Double getWeight(){
        return weight;
    }
//权重比较
    @Override
    public int compareTo(Edge that){
        return Double.compare(this.weight,that.weight);
    }
    @Override
    public boolean equals(Object o){
        if(this == o)return true;
        if(o == null || o!=this.getClass())return false;
        Edge<?> other = (Edge<?>) o;
        if(other.getV().equals(this.getV()) && other.getW().equals(this.getW()) && other.getWeight().equals(this.getWeight())) return  true;
        if(other.getV().equals(this.getW()) && other.getW().equals(this.getV()) && other.getWeight().equals(this.getWeight())) return  true;
        return false;
    }
    //天然的无向边，但实际上有向还是无向是由图的adj决定，实际这只是说它天然适合无向，但也可以有向，但就麻烦了

}
//