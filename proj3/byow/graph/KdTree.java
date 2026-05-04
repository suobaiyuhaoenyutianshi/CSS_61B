package byow.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维 KD 树实现，支持插入、范围查询和最近邻搜索。
 * 划分维度在每层深度间轮换：深度 0 按 x 划分，深度 1 按 y 划分，依此类推。
 */
public class KdTree {
    private Node root;
    private int size;

    /** KD 树节点定义 */
    private static class Node {
        final Point2D point;      // 该节点存储的点该节点存储的点
        Node left, right;         // 左/右子树（或下/上子树）
        final int depth;          // 节点在树中的深度，用于决定划分维度

        Node(Point2D point, int depth) {
            this.point = point;
            this.depth = depth;
        }

        /** 返回当前节点的划分维度：深度为偶数时按 x 轴，奇数时按 y 轴 */
        boolean splitByX() {
            return depth % 2 == 0;
        }

        /** 比较目标点与当前节点在划分维度上的大小 */
        int compareInSplitDim(Point2D that) {
            if (splitByX()) {
                return Double.compare(this.point.x, that.x);
            } else {
                return Double.compare(this.point.y, that.y);
            }
        }

        /** 计算当前节点到目标点的欧几里得距离平方（避免开方） */
        double distanceSquaredTo(Point2D that) {
            double dx = this.point.x - that.x;
            double dy = this.point.y - that.y;
            return dx * dx + dy * dy;
        }
    }

    /** 二维点，不可变 */
    public static class Point2D {
        final double x, y;
        public Point2D(double x, double y) { this.x = x; this.y = y; }
        public double x() { return x; }
        public double y() { return y; }
        @Override
        public String toString() { return "(" + x + ", " + y + ")"; }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    // ==================== 插入 ====================
    public void insert(Point2D p) {
        root = insert(root, p, 0);
    }

    private Node insert(Node n, Point2D p, int depth) {
        if (n == null) {
            size++;
            return new Node(p, depth);
        }
        // 在划分维度上比较，决定向左（下）还是向右（上）走
        int cmp = n.compareInSplitDim(p);
        if (cmp < 0) {          // 新点更小，去左子树
            n.left = insert(n.left, p, depth + 1);
        } else if (cmp > 0) {   // 新点更大，去右子树
            n.right = insert(n.right, p, depth + 1);
        } else {
            // 完全相等的情况：可以选择替换或者忽略。这里简单忽略重复点。
            // 实际应用可能需要存储重复或更新值。
        }
        return n;
    }

    // ==================== 范围查询 ====================
    /** 返回落在矩形区域 [xmin, xmax] × [ymin, ymax] 内的所有点 */
    public List<Point2D> range(double xmin, double xmax, double ymin, double ymax) {
        List<Point2D> result = new ArrayList<>();
        range(root, xmin, xmax, ymin, ymax, result);
        return result;
    }

    private void range(Node n, double xmin, double xmax, double ymin, double ymax,
                       List<Point2D> result) {
        if (n == null) return;
        Point2D p = n.point;

        // 1. 检查当前节点是否在矩形内
        if (p.x >= xmin && p.x <= xmax && p.y >= ymin && p.y <= ymax) {
            result.add(p);
        }

        // 2. 根据划分维度判断是否需要递归左右子树
        if (n.splitByX()) {
            // 划分线为 x = n.point.x
            if (xmin < p.x) range(n.left, xmin, xmax, ymin, ymax, result);  // 左子树可能相交
            if (xmax >= p.x) range(n.right, xmin, xmax, ymin, ymax, result); // 右子树可能相交
        } else {
            // 划分线为 y = n.point.y
            if (ymin < p.y) range(n.left, xmin, xmax, ymin, ymax, result);
            if (ymax >= p.y) range(n.right, xmin, xmax, ymin, ymax, result);
        }
    }

    // ==================== 最近邻搜索 ====================
    /** 返回距离查询点最近的点（若树空则返回 null） */
    public Point2D nearest(Point2D goal) {
        if (root == null) return null;
        // 使用数组包装最佳节点和最佳距离平方，便于在递归中修改
        Node[] bestNode = new Node[1];
        double[] bestDistSq = new double[]{Double.POSITIVE_INFINITY};
        nearest(root, goal, bestNode, bestDistSq);
        return bestNode[0].point;
    }

    /**
     * 递归最近邻搜索
     * @param n         当前节点
     * @param goal      查询点
     * @param bestNode  当前找到的最佳节点（包装在数组中以便修改）
     * @param bestDistSq 当前最佳距离的平方
     */
    private void nearest(Node n, Point2D goal, Node[] bestNode, double[] bestDistSq) {
        if (n == null) return;

        // 1. 更新最佳（如果当前节点更近）
        double distSq = n.distanceSquaredTo(goal);
        if (distSq < bestDistSq[0]) {
            bestDistSq[0] = distSq;
            bestNode[0] = n;
        }

        // 2. 确定“好的一侧”（Good Side）和“坏的一侧”（Bad Side）
        //    好的一侧是查询点所在的半空间，应优先搜索
        Node goodSide, badSide;
        int cmp = n.compareInSplitDim(goal);
        if (cmp < 0) {          // goal 在划分维度的较小侧 → 左子树是好的一侧
            goodSide = n.left;
            badSide = n.right;
        } else {                // goal 在划分维度的较大侧（或等于，等于时随便选一侧作为好侧）
            goodSide = n.right;
            badSide = n.left;
        }

        // 3. 优先搜索好的一侧
        nearest(goodSide, goal, bestNode, bestDistSq);

        // 4. 判断是否需要搜索坏的一侧
        //    剪枝条件：如果查询点到划分线的最短垂直距离的平方 >= 当前最佳距离平方，
        //    则坏的一侧不可能存在更近的点，直接跳过。
        if (needToSearchBadSide(n, goal, bestDistSq[0])) {
            nearest(badSide, goal, bestNode, bestDistSq);
        }
    }

    /**
     * 剪枝判断：坏的一侧是否还有可能存在更优点？
     * @param n          当前划分节点
     * @param goal       查询点
     * @param bestDistSq 当前最佳距离平方
     * @return true 表示需要搜索坏的一侧
     */
    private boolean needToSearchBadSide(Node n, Point2D goal, double bestDistSq) {
        // 计算查询点到划分线的最短垂直距离的平方
        double distToSplitSq;
        if (n.splitByX()) {
            double dx = goal.x - n.point.x;
            distToSplitSq = dx * dx;
        } else {
            double dy = goal.y - n.point.y;
            distToSplitSq = dy * dy;
        }
        // 如果垂直距离平方小于最佳距离平方，则坏的一侧可能存在更近的点
        return distToSplitSq < bestDistSq;
    }

    // ==================== 简单测试主方法 ====================
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(2, 3));
        tree.insert(new Point2D(4, 2));
        tree.insert(new Point2D(4, 5));
        tree.insert(new Point2D(3, 3));
        tree.insert(new Point2D(1, 5));
        tree.insert(new Point2D(5, 5));

        // 范围查询
        List<Point2D> range = tree.range(2, 4, 2, 5);
        System.out.println("Range [2,4]x[2,5]: " + range);

        // 最近邻查询
        Point2D query = new Point2D(0, 7);
        Point2D nearest = tree.nearest(query);
        System.out.println("Nearest to " + query + " is " + nearest);
    }
} ///