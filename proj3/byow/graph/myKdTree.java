package byow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class myKdTree {
    private Node root;//开头
    private int size;//一共多少节点
    private final int dimens; //多少维度
    private int insertSinceRebuild;          // 自上次重建以来插入的新点数
    private static final double REBUILD_RATIO = 0.5; // 阈值比例（新点占当前大小的比例）
    private static final int    MIN_REBUILD  = 10;    // 最少重建间隔，避免小树频繁重建
    /**一个节点要有很多功能：深度->得应该比较的维度，// 左/右子树（或下/上子树）该节点存储的点 double[]
     比较目标点与当前节点在划分维度上的大小
     计算当前节点到目标点的欧几里得距离平方（避免开方）
     */
    private class Node {
        final double[] ponit;//维度坐标
        private Node left,right;
        private int depth;// 节点在树中的深度，用于决定划分维度
        public Node(double[] ponit,int depth){
            this.ponit = ponit;
            this.depth = depth;
        }
        //返回当前节点的划分维度 注：1维0，2维1。。。因为我是用double存的坐标
        public int spiltDim(){
            return depth % dimens;
        }
        //返回与目标的坐标的该维度比较大小，大于0说明应是是本节点的右侧，right
        public int compareSplitDim(double[] that){
            int spilitDem = spiltDim();
            return Double.compare(that[spilitDem], this.ponit[spilitDem]);
        }
        /** 当前节点到目标点的欧氏距离平方 */
        public double distanceSquaredTo(double[] thatPoint){
            double sum =0;
            for(int i = 0;i < dimens;i++){
                double correspondDimData = ponit[i] - thatPoint[i];
                sum += correspondDimData * correspondDimData;
            }
            return sum;
        }

    }


    public myKdTree(int dimens){
        this.root = null;
        this.dimens =dimens;
        this.size = 0;
        this.insertSinceRebuild = 0;
    }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    //插入
    public void insert(double[] point){
        root = insertHelp(point,root,0);
        //自动检查是否需要重建
        if (insertSinceRebuild >= MIN_REBUILD &&
                insertSinceRebuild >= size * REBUILD_RATIO) {
                rebuild();   // 触发全量打乱重建
        }
    }
    //传的是当前节点,父节点，父节点的父节点
    private Node insertHelp(double[] point,Node curr,int depth){
        if(curr == null){
            size++;
            insertSinceRebuild++;
            return new Node(point,depth);

        }
        int comp = curr.compareSplitDim(point);
        if(comp >= 0){
            curr.right = insertHelp(point,curr.right,depth+1);
        } else if (comp < 0) {
            curr.left = insertHelp(point,curr.left,depth+1);
        }else {

        }
        return curr;

    }


    /** 收集当前树中所有点，打乱，清空，重新插入 */
    private void rebuild(){
        List<double[]> allPoints = new ArrayList<>();
        collectAll(root,allPoints);
        //打乱
        Collections.shuffle(allPoints);
        //清空
        this.size =0;
        this.insertSinceRebuild = 0;
        root = null;
        //重新插入，因为insertSinceRebuild为0，不会触发
        for (double[] p : allPoints) {
            root = insertHelp(p, root, 0);
        }
    }

    private void collectAll(Node node,List<double[]> container ){
        if(node == null) return;
        container.add(node.ponit);
        collectAll(node.left,container);
        collectAll(node.right,container);
    }


    /** 返回距离查询点最近的点（若树空则返回 null） */
    public double[] nearest(double[] goal){
        //方便与再递归与传递中修改，数组引用！
        double[] bestDist = new double[]{Double.POSITIVE_INFINITY};//最近距离
        Node[] BestNode = new Node[1];//最近节点
        nearestHelp(root,goal,bestDist,BestNode);

        return  BestNode[0].ponit;
    }

    private boolean nearestHelp(Node curr,double[] goal,double[] bestDist,Node[] BestNode) {
        if (curr == null) return false;
        //有没有什么方式使若currTogoal==0，直接全部跳出
        double currTogoal = curr.distanceSquaredTo(goal);
        if (bestDist[0] > currTogoal) {
            bestDist[0] = currTogoal;
            BestNode[0] = curr;
            if (bestDist[0] == 0.0) {
                return true;
            }
        } else if (dimeShotDist(goal, curr) > bestDist[0]) {
            return false;
        }
        //分倾向那个方向👉right
        int comp = curr.compareSplitDim(goal);
        Node BestSide;
        Node badSide;
        if (comp > 0) {
            BestSide = curr.right;
            badSide = curr.left;
        } else if (comp < 0) {
            BestSide = curr.left;
            badSide = curr.right;
        } else {//相等为0，则随机
            boolean flag = new Random().nextBoolean();
            if (flag) {
                BestSide = curr.right;
                badSide = curr.left;
            } else {
                BestSide = curr.left;
                badSide = curr.right;
            }
        }//

        if(nearestHelp(BestSide,goal,bestDist,BestNode)){
            return true;
        }
        //再返回后，探索bad,因为dimeShotDist,，不符和会返回的，不用写判断,写判断只是为了节省开支
        if (badSide != null &&bestDist[0] > dimeShotDist(goal, badSide)) {
            if( nearestHelp(badSide,goal,bestDist,BestNode)){
                return false;
            }
        }

    return false;
    }



    private double dimeShotDist(double[] goal,Node curr){
        int spilitNum = curr.spiltDim();
        double Dimeshort = curr.ponit[spilitNum] - goal[spilitNum];
        return Dimeshort * Dimeshort ;
    }


    static void main(String[] args){
        myKdTree test = new myKdTree(2);
        test.insert(new double[]{3,1});
        test.insert(new double[]{6,1});
        test.insert(new double[]{6,2});
        test.insert(new double[]{5,2});
        test.insert(new double[]{3,2});
        test.insert(new double[]{3,3});
        double[]a =test.nearest(new double[]{3,2});
    }




}
///