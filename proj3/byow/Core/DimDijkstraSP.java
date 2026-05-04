package byow.Core;
import byow.block.*;

import java.util.Comparator;
import java.util.PriorityQueue;

/**输入: block[][] world, int startX, int startY, int targetX, int targetY
 输出: List<int[]> 路径坐标列表（从起点到终点，每一步的 (x, y)
 // 通行规则：要么是可变为空地，要么是可变门的墙，这里返回的不过是List<int[]> 路径,之后还要在engine还有他写规则，  房间为负数的才可覆盖，为什么是负数而不是-1，因为我之后要加阻塞方快：不可通行，为-2，prince为3，避免无路可走         </>*/
public class DimDijkstraSP {
    private int[][] disTo;
    private int[][][] edgeTo;//注释：例[2][3][0] = 1  [2][3][0] = 2即（2，3）-》（1，2）
    private PriorityQueue<Node> pq;
    private final int WIDTH;//X
    private  final int HEIGHT;

    public DimDijkstraSP(block[][] blocks,int startX,int startY,int targetX,int targetY){
        this.disTo = new int[blocks[0].length][blocks.length];
        this.edgeTo = new int[blocks[0].length][blocks.length][2];
        this.WIDTH = blocks[0].length;this.HEIGHT =blocks.length ;
        pq = new PriorityQueue<>(Comparator.comparingDouble(vd -> vd.dist));
        pq.add(new Node(startX,startY,0));
        while (!pq.isEmpty()){
            Node T = pq.poll();
            int currTX = T.x;int currTy= T.y;
            if(currTX==targetX&&currTy == targetY ){
                break;
            }
            //上下左右
            int[][] directs = new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
            for(int[] directDiam:directs){
                int directX = currTX+directDiam[0];//
                int directY = currTy + directDiam[1];
                if(directX <0||directX>=this.WIDTH||directY <0||directY >=this.HEIGHT)continue;
                block newDirectBlock = blocks[directX][directY];
                // 通行规则：要么是可变为空地，要么是可变门的墙
                if(newDirectBlock.BecameDoor||newDirectBlock.isanopenspace){
                    //松弛
                    relex(newDirectBlock,)
                }
            }



        }

    }

    private static class Node{
        private int x;
        private int y;
        private int dist;
        public Node(int x,int y,int dis){
            this.x = x;
            this.y = y;
            this.dist = dis;
        }
    }


}
