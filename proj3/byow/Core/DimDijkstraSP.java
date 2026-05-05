package byow.Core;
import byow.block.*;

import java.util.*;

/**输入: block[][] world, int startX, int startY, int targetX, int targetY
 输出: List<int[]> 路径坐标列表（从起点到终点，每一步的 (x, y)
 // 通行规则：要么是可变为空地，要么是可变门的墙，这里返回的不过是List<int[]> 路径,之后还要在engine还有他写规则,这个规则是指以这个路线为核心扩张，，堵塞方块添加必定在最短路径之前
 房间为负数的才可覆盖，为什么是负数而不是-1，因为我之后要加阻塞方快：不可通行，为-2，prince为3，避免无路可走         </>*/

import byow.block.*;
import java.util.*;

public class DimDijkstraSP {
    private int[][] disTo;
    private int[][][] edgeTo;
    private PriorityQueue<Node> pq;
    private final int WIDTH, HEIGHT;
    private boolean findREsult;
    int startX, startY, targetX, targetY;
    int startIdx, targetIdx;

    public DimDijkstraSP(block[][] blocks, int startX, int startY, int targetX, int targetY,
                         int startIdx, int targetIdx) {
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.startIdx = startIdx;
        this.targetIdx = targetIdx;
        this.WIDTH = blocks.length;
        this.HEIGHT = blocks[0].length;
        this.disTo = new int[WIDTH][HEIGHT];
        this.edgeTo = new int[WIDTH][HEIGHT][2];
        for (int[] row : disTo) Arrays.fill(row, Integer.MAX_VALUE);

        boolean[][] closed = new boolean[WIDTH][HEIGHT];
        pq = new PriorityQueue<>(Comparator.comparingDouble(v -> v.f));

        disTo[startX][startY] = 0;
        int startH = heuristic(startX, startY);
        pq.add(new Node(startX, startY, 0, startH));

        int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            int cx = cur.x, cy = cur.y;
            if (closed[cx][cy]) continue;
            if (cur.dist > disTo[cx][cy]) continue;   // 惰性删除
            closed[cx][cy] = true;                    // 移入关闭列表

            if (cx == targetX && cy == targetY) {
                findREsult = true;
                break;
            }

            for (int[] d : dirs) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) continue;
                if (closed[nx][ny]) continue;         // 已关闭，不再松弛

                block nb = blocks[nx][ny];
                // 房间检查
                if (!(nb.room == startIdx || nb.room == targetIdx || nb.room < 0)) continue;
                // 通行检查
                boolean passable = (nb.BecameDoor && (nb.room == startIdx || nb.room == targetIdx))
                        || nb.isanopenspace;
                if (!passable) continue;

                int newG = disTo[cx][cy] + nb.price;
                if (newG < disTo[nx][ny]) {           // 仅严格小才更新，杜绝等代价交换
                    disTo[nx][ny] = newG;
                    edgeTo[nx][ny][0] = cx;
                    edgeTo[nx][ny][1] = cy;
                    int h = heuristic(nx, ny);
                    pq.add(new Node(nx, ny, newG, newG + h));
                }
            }
        }
        if (!findREsult) System.out.println("堵塞出问题了");
    }

    private int heuristic(int x, int y) {
        // 欧几里得距离作为启发式
        return (int) Math.sqrt((x - targetX) * (x - targetX) + (y - targetY) * (y - targetY));
    }

    public Iterable<int[]> pathTo() {
        if (!findREsult) return null;
        List<int[]> path = new ArrayList<>();
        int x = targetX, y = targetY;
        while (x != startX || y != startY) {
            path.add(new int[]{x, y});
            int px = edgeTo[x][y][0];
            int py = edgeTo[x][y][1];
            x = px;
            y = py;
        }
        path.add(new int[]{startX, startY});
        Collections.reverse(path);
        return path;
    }

    private static class Node {
        int x, y;
        int dist;   // g 值
        int f;      // g + h
        Node(int x, int y, int dist, int f) {
            this.x = x; this.y = y; this.dist = dist; this.f = f;
        }
    }
}
