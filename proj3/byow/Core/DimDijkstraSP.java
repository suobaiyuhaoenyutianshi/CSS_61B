package byow.Core;
import byow.block.*;

import java.util.*;

/**输入: block[][] world, int startX, int startY, int targetX, int targetY
 输出: List<int[]> 路径坐标列表（从起点到终点，每一步的 (x, y)
 // 通行规则：要么是可变为空地，要么是可变门的墙，这里返回的不过是List<int[]> 路径,之后还要在engine还有他写规则,这个规则是指以这个路线为核心扩张，，堵塞方块添加必定在最短路径之前
 房间为负数的才可覆盖，为什么是负数而不是-1，因为我之后要加阻塞方快：不可通行，为-2，prince为3，避免无路可走         </>*/
public class DimDijkstraSP {
    private int[][] disTo;
    private int[][][] edgeTo;//注释：例[2][3][0] = 1  [2][3][0] = 2即（2，3）-》（1，2）
    private PriorityQueue<Node> pq;
    private final int WIDTH;//X
    private  final int HEIGHT;
    private boolean findREsult;
    int startX ;
    int startY;
    int targetX;
    int targetY;
    int startIdx;int targetIdx;
    //房间序号
    public DimDijkstraSP(block[][] blocks,int startX,int startY,int targetX,int targetY,int startIdx,int targetIdx){
        this.startX = startX;this.startY = startY;this.targetX =targetX;this.targetY =targetY;this.startIdx = startIdx;this.targetIdx = targetIdx;
        this.WIDTH = blocks.length;        // 80
        this.HEIGHT = blocks[0].length;    // 40
        this.disTo = new int[WIDTH][HEIGHT];
        this.edgeTo = new int[WIDTH][HEIGHT][2];
        for (int i = 0; i < disTo.length; i++) {
            Arrays.fill(disTo[i], Integer.MAX_VALUE);
        }
        pq = new PriorityQueue<>(Comparator.comparingDouble(vd -> vd.dist));
        disTo[startX][startY] = 0;
        pq.add(new Node(startX,startY,0));
        while (!pq.isEmpty()){
            Node T = pq.poll();
            int currTX = T.x;int currTy= T.y;

            if(currTX==targetX&&currTy == targetY ){
                this.findREsult = true;
                break;
            }
            //上下左右
            int[][] directs = new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
            for(int[] directDiam:directs){
                int directX = currTX+directDiam[0];//
                int directY = currTy + directDiam[1];
                if(directX <0||directX>=this.WIDTH||directY <0||directY >=this.HEIGHT)continue;
                block newDirectBlock = blocks[directX][directY];
                //不允许闯入其他房间,
                if(!(newDirectBlock.room == startIdx||newDirectBlock.room == targetIdx||newDirectBlock.room <0))continue;
                // 通行规则：要么是可变为空地，要么是可变门的墙且为它的房间将序号为目标两地的其中一个，我的设计例只允许墙变为门
                if((newDirectBlock.BecameDoor&&(newDirectBlock.room==this.startIdx||newDirectBlock.room == targetIdx))||newDirectBlock.isanopenspace){

                    //松弛
                    if(disTo[currTX][currTy] +newDirectBlock.price < disTo[directX][directY] ){
                           int InspiratorNum =(int) Math.sqrt((Math.abs(currTX -targetX)*Math.abs(currTX-targetX) + Math.abs(currTy - targetY)* Math.abs(currTy - targetY)));
                            disTo[directX][directY] = disTo[currTX][currTy]  ;
                             edgeTo[directX][directY][0] = currTX;   // direct 的前驱是 curr
                             edgeTo[directX][directY][1] = currTy;
                            pq.add(new Node(directX,directY, newDirectBlock.price +disTo[directX][directY]+InspiratorNum));
                    }

                }



            }



        }
        if(!findREsult){
            System.out.println("堵塞出问题了");
        }
    }
    //返回列表坐标
    public Iterable<int[]> pathTo(){
        if(!findREsult){
            System.out.println("堵塞");
            return null;
        }
        List<int[]> path = new ArrayList<>();
        int x =targetX;int y =targetY;
        while (x!=startX||y!=startY){
            path.add(new int[]{x,y});
            x = edgeTo[x][y][0];
            y = edgeTo[x][y][1];
        }
        path.add(new int[]{startX,startY});
        Collections.reverse(path);
        return path;
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
