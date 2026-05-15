package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
//import byow.TileEngine.Tileset;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import byow.block.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import byow.graph.*;
import byow.role;
public class Engine {
    TERenderer ter = new TERenderer();
    /* 您可以随意更改宽度和高度。*/
    public static final int WIDTH = 200;
    public static final int HEIGHT = 200;
    public TETile[][] world;
//每个世界房间数量
    private final int MaxROOMS= 100;
    private final int MINROOMS = 4;
    public int roomNum ;
    public int roomsdis = 10;
    private int area = roomsdis*roomsdis;
    //房间寻找依靠，尤其它的坐标与序号，注：链表的0对应就是房间0，这类里面也有它的序号避免，你弄错
    private List<ROOM> ROOMS;
    //种子
    public Random rand;
    //世界
    public block[][]  Blockworld;
    private int viewW = 80;   // 屏幕显示的格子数
    private int viewH = 40;
    private int camX = 0;     // 摄像机左下角在世界中的 X 坐标
    private int camY = 0;     // 摄像机左下角在世界中的 Y 坐标
    // 初始偏移：让玩家位于视口中心
    private int deadX =15;
    private int deadY =15;
    //主角
    private role me;
    /**
     * 用于探索全新世界的探索方法。此方法应能处理所有输入内容，
     * 包括来自主菜单的输入。*/
    //迷雾显示半径
    public int mistyRadius = 7;
    //道路大小
    private int pathThick = 15;
    public void interactWithKeyboard() {
        //房间
        ROOMS = new ArrayList<>();
        //菜单N 表示“新世界”，L 表示“加载世界”，Q 表示退出。
        String s = menu();
       //根据s 处理
        if(s.equalsIgnoreCase("q")) return;
        //加载世界，自己创建的方块对象
        this.Blockworld= loadWorld(s);

       //最小图:生成非加载
        if (!s.equalsIgnoreCase("q")&&!s.equalsIgnoreCase("l")) {
            //加载阻碍先
            this.resetBlockingBlock();
            minGrap(Blockworld,this.ROOMS);
        }

        //渲染

           // rendergraph(Blockworld);

        //角色 //生成在一个房子中间
        if(!s.equalsIgnoreCase("l")){
            this.me = new role(4,this.Blockworld[ROOMS.get(0).XLoc][ROOMS.get(0).Yloc]);
        }

        //覆盖让主角登场
        this.Blockworld[me.place.x][me.place.y] =me.role;

        camX = Math.max(0, me.place.x - viewW / 2);
        camY = Math.max(0, me.place.y - viewH / 2);
        //初始显示周边
        revealAround(me);
        // 视口大小（窗口显示的瓦片数，可调）
     /**   if (camX < 0) camX = 0;
        if (camY < 0) camY = 0;
        if (camX > WIDTH - viewW) camX = WIDTH - viewW;
        if (camY > HEIGHT - viewH) camY = HEIGHT - viewH;
        // 初始摄像机位置
        camX = Math.max(0, me.place.x - viewW / 2);
        camY = Math.max(0, me.place.y - viewH / 2);
        int xOffset = Math.max(0, Math.min(me.place.x - viewW / 2, WIDTH - viewW));
        int yOffset = Math.max(0, Math.min(me.place.y - viewH / 2, HEIGHT - viewH));

*/
        ter.initialize(viewW, viewH);   // 不需要偏移参数了
        while (true) {
            rendergraph(Blockworld);
            StdDraw.pause(100);

            String c = null;
            while (c == null) {
                if (StdDraw.hasNextKeyTyped()) {
                    c = String.valueOf(StdDraw.nextKeyTyped());
                }
            }

            if (!c.equals("w") && !c.equals("s") && !c.equals("a") && !c.equals("d")) continue;
            if (c.equals("q")) break;

            move(c, me);
            // 死区更新摄像机（只在这里更新一次）
            int centerX = camX + viewW / 2;
            int centerY = camY + viewH / 2;
            int dx = me.place.x - centerX;
            int dy = me.place.y - centerY;
            if (Math.abs(dx) > deadX) {
                camX += (dx > 0) ? (Math.abs(dx) - deadX) : -(Math.abs(dx) - deadX);
            }
            if (Math.abs(dy) > deadY) {
                camY += (dy > 0) ? (Math.abs(dy) - deadY) : -(Math.abs(dy) - deadY);
            }
            // 边界限制
            if (camX < 0) camX = 0;
            if (camY < 0) camY = 0;
            if (camX > WIDTH - viewW) camX = WIDTH - viewW;
            if (camY > HEIGHT - viewH) camY = HEIGHT - viewH;

         }


    }

    public void move(String c,role me){
        //上下左右
      int[][] Direct = new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
      int[] move={0,0} ;
      if(c.equals("w")){
          move = Direct[0];
      }else if (c.equals("s")) move = Direct[1];
      else if (c.equals("a")) move = Direct[2];
      else if(c.equals("d")) move = Direct[3];
      int x =me.place.x + move[0];
      int y =me.place.y + move[1];
      if (x < 0 || x >= this.WIDTH || y < 0 || y >= this.HEIGHT) return;
      //角色记录信息,恢复复原原先地方

      me.record(Blockworld[x][y],this.Blockworld);
        revealAround(me);
      //移动显示周边



    }

    private void revealAround(role me){
        for(int x=me.place.x - this.mistyRadius;x <=me.place.x + this.mistyRadius ;x++){
            for(int y= me.place.y-this.mistyRadius;y<=me.place.y+this.mistyRadius;y++){
                if(x<0||x>=this.WIDTH||y<0||y>=this.HEIGHT)continue;
                if((x==me.place.x - this.mistyRadius)||(x ==(me.place.x + this.mistyRadius))||(y == (me.place.y-this.mistyRadius))||(y==(me.place.y+this.mistyRadius))){
                   //已经修改为true的不许动
                    if(this.Blockworld[x][y].revealed){
                        continue;
                    }//目的让边界粗超些
                    if(this.rand.nextInt(3)==2){
                        this.Blockworld[x][y].revealed=true;
                    }
                    continue;
                }
                this.Blockworld[x][y].revealed = true;
            }
        }



    }







    private static String  menu(){
        String s = null;
        StdDraw.setCanvasSize(600,600);
        StdDraw.setXscale(0,600 );
        StdDraw.setYscale(0, 600);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        while (s == null) {


            StdDraw.setFont(new Font("Monaco", Font.BOLD, 100));
            StdDraw.text(200, 445, "N   S  L");
            StdDraw.show();

            if (StdDraw.hasNextKeyTyped()) {
                s = String.valueOf(StdDraw.nextKeyTyped());
            }

            StdDraw.pause(50);               // 短暂休眠，避免 CPU 空转
        }
        StdDraw.clear();
        if(s.equalsIgnoreCase("N")){

            String k = "";char c = 0;

            while(c != 's'){
                StdDraw.clear();
                    StdDraw.text(300,500,"Seed+S");
                StdDraw.text(200,200,k);
                StdDraw.show();
                if(StdDraw.hasNextKeyTyped()){
                    c = StdDraw.nextKeyTyped();
                    if (c != 's') {              // 避免把结束字符's'加入k
                        k = k + c;
                    }
                }

            }
            s = k;
        } else if (s.equalsIgnoreCase("L") || s.equalsIgnoreCase("Q")) {
            return s;
        }
        StdDraw.clear();
        return s;

    }
    /**加载世界，L加载之前创建的，N是新的世界，返回*/
    public  block[][] loadWorld(String s){
        if(s.equalsIgnoreCase("l")){
            //加载之前最后保存的世界；
            return null;
        }

        //创建型世界
        long seed = Long.parseLong(s);
        this.rand = new Random(seed);
        //房间数量,后面不可实现时他会修改
        this.roomNum = rand.nextInt(MaxROOMS - MINROOMS) + MINROOMS ;
        return creatWorld(seed,rand);
    }


    /**只负责渲染，输入数组，渲染，什么颜色，内部逻辑都不归他管
     * */
    private void rendergraph(block[][] world) {
        TETile[][] viewport = new TETile[viewW][viewH];
        for (int i = 0; i < viewW; i++) {
            for (int j = 0; j < viewH; j++) {
                int worldX = camX + i;
                int worldY = camY + j;
                if (worldX >= 0 && worldX < WIDTH && worldY >= 0 && worldY < HEIGHT) {
                    //
                    // 显示真实外观
                    if(world[worldX][worldY].revealed){
                        viewport[i][j] = world[worldX][worldY].blockName;
                    }
                   else  viewport[i][j] = Tileset.NOTHING; //显示为，我喜欢
                } else {
                    viewport[i][j] = Tileset.NOTHING;  // 超出世界的地方显示黑色虚空
                }
            }
        }
        ter.renderFrame(viewport);
    }
    private  block[][] creatWorld(long seed,Random rand){
        block[][] world = new block[this.WIDTH][this.HEIGHT];
        //初始
        initWorld(world);


        List<twoDim> rooms = new ArrayList<>();
       // rooms.add(new twoDim(rand.nextInt(this.WIDTH),rand.nextInt( this.HEIGHT)));
        int times =0;int idx =0;
        while( rooms.size() < this.roomNum&&times < 10000){
            int x =rand.nextInt(this.WIDTH);int y = rand.nextInt(this.HEIGHT);
           if(isConformingDiffer(x,y,rooms)){


               rooms.add(new twoDim(x,y));
                ROOMS.add(new ROOM(idx,x,y,this.area));
                idx++;
           }
            times++;

        }
        if (rooms.size() < this.roomNum) {
            this.roomNum = rooms.size(); // 无法生成足够房间，调整实际房间数
        }
        //先试试手
        for(int i =0;i<this.roomNum ;i++){
           //创建房间
            creatRoom(rooms.get(i),world,rand ,i);
        }



        return world;
    }



/**初始世界*/
    private void initWorld(block[][]world){
        for(int y =0;y<this.HEIGHT;y++){
            for (int x=0;x<this.WIDTH;x++){
                world[x][y]= new voidBlock(x,y);
            }
        }
    }



    /**是否符合相差距离*
     *
     */
    private boolean isConformingDiffer(int x,int y,List<twoDim> rooms){
        //中心距离边界距离，为1/3 dis
        boolean boundaryX = (x - 0)>=this.roomsdis/2 &&(this.WIDTH-1 -x)>this.roomsdis/2;
        boolean boundaryY = (y - 0)>=this.roomsdis/2 && (this.HEIGHT-1 -y)>this.roomsdis/2;
        if(!boundaryX||!boundaryY) return false;
        List<Integer> dis = new ArrayList<>(); int i=0;
        while(i < rooms.size()){
            int disTo = (x-rooms.get(i).x )*(x-rooms.get(i).x) +(y - rooms.get(i).y)*(y - rooms.get(i).y);
            if(disTo -(this.roomsdis)*(this.roomsdis/3) < this.roomsdis*roomsdis)return false;
            i++;
        }
        return true;
    }




    //二维坐标
    private class twoDim{
        int x;int y;
        public twoDim(int x,int y){
            this.x =x;this.y = y;
        }
    }
/**创建房间
 * */
private void creatRoom(twoDim towDim, block[][] world, Random rand, int sigalRoom) {
    // 随机宽度和高度，范围 [3, roomsdis]，因为文档要求3为最小
    int width = 3 + rand.nextInt(this.roomsdis - 2);
    int height = 3 + rand.nextInt(this.roomsdis - 2);
    int x = towDim.x - width / 2;
    int y = towDim.y - height / 2;
    int xLast = x + width;
    int yLast = y + height;

    // 边界裁剪
    x = Math.max(0, x);
    y = Math.max(0, y);
    xLast = Math.min(this.WIDTH, xLast);
    yLast = Math.min(this.HEIGHT, yLast);

    for (int i = y; i < yLast; i++) {
        for (int j = x; j < xLast; j++) {
            world[j][i] = new spaceBlock(sigalRoom, j, i);
            if (i == y || i == yLast - 1 || j == x || j == xLast - 1) {
                boolean isWall = rand.nextInt(8) != 0;
                if (isWall) {
                    world[j][i] = new WallBlock(sigalRoom, j, i);
                } else {
                    world[j][i] = new flowerBlock(sigalRoom, j, i);
                }
            }
        }
    }
}
    public static  void main(String[] args){
        Engine test = new Engine();
        test.interactWithKeyboard();
    }


    /**最小树，联通房间的顺序依靠，依靠某种方式返回Iterable<IEdge<T>> edges()，在依靠，创建个Map或直接数组序号也罢，都可以*/
    public void minGrap(block[][] blocks,List<ROOM> ROOMS){
        EdgeWeightedGraph<ROOM> graph = new EdgeWeightedGraph<>(ROOMS.size());
        for(int i =0;i< ROOMS.size();i++){
            //加除了自己的对象
            for(int j =0;j<ROOMS.size();j++){
                if(j==i)continue;
                //距离
                ROOM T =ROOMS.get(i);ROOM V = ROOMS.get(j);
                double weight = (T.XLoc -V.XLoc)*(T.XLoc -V.XLoc) + (T.Yloc - V.Yloc)*(T.Yloc - V.Yloc);
                graph.addEdge(new UndirectedEdge<>(T, V,weight));
            }
        }
        //ok,照着这个修改blocks,先写个简单的直线
        KruskalMST<ROOM> minTree = new KruskalMST<>(graph);
        for (IEdge<ROOM> edge : minTree.edges()) {
            ROOM T = edge.either();
            ROOM V = edge.other(T);
            thoughTwoRoom(blocks, T, V); // 挖掘从 T 到 V 的lu
        }



    }

/**路的生成，依靠房间序号-1，最后，不行我还要再写一种阻塞方块，为-2，围墙只能作为边界，他只后不能添加，之后添加阻塞方块来干扰路线，但若遇上房间为q墙的变为门，
 * 房间为负数的才可覆盖，为什么是负数而不是-1，因为我之后要加阻塞方快：不可通行，为-2，prince为3，避免无路可走,注：我没有为chen
 * 注意我只允许墙可变为门！！！！！！！！！
 * */
    private void thoughTwoRoom(block[][] blocks,ROOM T,ROOM V){
        DimDijkstraSP path =new DimDijkstraSP(blocks,T.XLoc,T.Yloc, V.XLoc, V.Yloc,T.roomNum,V.roomNum);
        for(int[]coordinates:path.pathTo()){
            //先全部变为空地试试
            int x =coordinates[0];int y =coordinates[1];
            /// 返回的路径要么是可变为空地，要么是可变门的墙且为它的房间将序号为目标两地的其中一个，我的设计例只允许墙变为门,/
            if (blocks[x][y].getName().equals(WallBlock.Name)) {
                blocks[x][y] = new doorBlock(x, y);
            } else if (blocks[x][y].getName().equals(voidBlock.Name)) {
                //核心路径
                blocks[x][y] = new PathVoidBlock( x, y);
            }else if(blocks[x][y].room<0) {
                // 其他任何出现在路径上的方块，也强制换成 PathVoidBlock
                // 这样就不会留任何可装饰的尾巴
                blocks[x][y] = new PathVoidBlock(x, y);
            }
            //必须在核心路径形成后，否则会破坏它

        }
        decorateAround(blocks,path);
    }

/**对核心路径装饰，注，这个装饰可以走，之后会为装饰物周围，不如可被装饰墙覆盖,装饰墙只是不可通行，仍然可变空地，可被装饰，可被装饰墙覆盖装饰墙与void方块，其余不可。堵塞方块不过比装饰方块修改了可变为空地为false且不可变为装饰方块
 * */
    private void   decorateAround(block[][] blocks,DimDijkstraSP path){
       //所有装饰方块
        List<block> decorateCoordinates= new ArrayList<>();
        //装饰墙厚度,不想设计这个厚度先为1
        //int thickness = ((this.roomsdis +3-1)/3+1)/3;
        for(int[] coordinates:path.pathTo()){
            int x =coordinates[0];int y =coordinates[1];
            //可变为空地且不为path的有概率覆盖
            for(int i=x-this.pathThick/2;i<=x+pathThick/2;i++){
                for(int j = y-this.pathThick/2;j <=y+ this.pathThick/2;j++){
                    // 整数运算公式（推荐，高效且避免浮点）
                    //对于正整数 a 和 b，向上取整公式为：
                    //(a + b - 1) / b
                    if(i<0 ||i>=this.WIDTH||j<0||j>=this.HEIGHT)continue;
                    //不允许破坏核心路径
                    if(blocks[i][j].canBeDecorated() &&blocks[i][j].room<0 &&!blocks[i][j].getName().equals(PathVoidBlock.Name)){


                        //装饰物的房间号为
                        if(rand.nextInt(10) == 1){
                            blocks[i][j] =new decorateflowerBlock(i,j);
                            decorateCoordinates.add( blocks[i][j]);
                        }else {
                            blocks[i][j] = new decorateSpaceblock(i,j);
                            decorateCoordinates.add( blocks[i][j]);
                        }

                    }
                }
            }
        }
        //上下左右，可被装饰墙覆盖装饰墙与void方块
        int[][] direct = new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
        //装饰完才能知道边缘，且内部填好，这样不会堵住出口才能填加
        for(block decorateBlock:decorateCoordinates){
           for(int[]d:direct){
               int dx = decorateBlock.x +d[0];int dy =decorateBlock.y + d[1];
               if(dx <0 ||dx>= WIDTH||dy <0 ||dy>= this.HEIGHT) continue;
               if(blocks[dx][dy].becameDecorateWall){
                   blocks[dx][dy] = new decorateWallBlock(dx,dy);
               }


           }

        }



    }
//插入堵塞方块,只能覆盖虚空
    private void resetBlockingBlock(){
        //一个地点多少,不一定有这么多，有随机性
        int radu =3;
        //随机几个 1到8个
        int blockingNum = this.rand.nextInt(8-1)+1;
        int i=0;
        //方向
        int[][] direct= new int[][]{{0,1},{0,-1},{-1,0},{1,0}};
        while(i < blockingNum){
            int x = this.rand.nextInt(this.WIDTH);
            int y = this.rand.nextInt(this.HEIGHT);
            if(!this.Blockworld[x][y].getName().equals(voidBlock.Name)) continue;
            i++;
           // this.Blockworld[x][y] = 堵塞
            for(int j= x - radu;j<=x+radu;j++){
                    for(int k =y -radu;k<= y+radu;k++){
                        if(j <0 ||j>= WIDTH||k <0 ||k>= this.HEIGHT) continue;
                        if(!this.Blockworld[j][k].getName().equals(voidBlock.Name)) continue;
                        if(this.rand.nextInt(3)==2){
                            this.Blockworld[j][k]= new BlockingBlock(j,k);
                        }
                    }

            }

        }


    }




    /**
     * 用于自动批改和测试您代码的方法。输入的字符串将是一个字符序列（例如，“n123sswwdasdassadwas”、“n123sss：q”、“lwww”。
     * 该引擎应表现得如同用户使用 interactWithKeyboard 将这些字符输入到引擎中一样。*
     请记住，以“：q”结尾的字符串应会使游戏自动保存。例如，
     * 如果我们执行 interactWithInputString("n123sss:q") 这个操作，我们预期游戏会先运行前 7 个指令（n123sss），
     * 然后退出并保存状态。接着，如果我们再执行 interactWithInputString("l") 这个操作，我们应该会回到完全相同的初始状态。*
     换句话说，这两条调用语句：
     - interactWithInputString("n123sss:q")
     - interactWithInputString("lww*
     * 应该产生与以下内容完全相同的世界状态：
     *   - interactWithInputString("n123sssww*
     * @参数 input：将输入的字符串提供给您的程序
     * @返回值：表示世界状态的二维 TETile[][] 数组*/
    //接收一个指令字符串，返回处理完所有指令后的世界状态 (TETile[][])，用于自动评分。
    public TETile[][] interactWithInputString(String input) {
        // 1. 解析输入，提取 seed
        input = input.toUpperCase();
        if (input.startsWith("N")) {
            int sIndex = input.indexOf('S');
            if (sIndex == -1) return null; // 输入不合法
            String seedStr = input.substring(1, sIndex);
            long seed = Long.parseLong(seedStr);

            // 2. 用这个 seed 生成世界
            this.rand = new Random(seed);
            this.ROOMS = new ArrayList<>();
            this.roomNum = rand.nextInt(MaxROOMS - MINROOMS) + MINROOMS;
            this.Blockworld = creatWorld(seed, this.rand);

            // 3. 如果你需要阻塞方块和走廊（Phase 1 要求世界必须完整，走廊和房间都要有）
            resetBlockingBlock();
            minGrap(Blockworld, ROOMS);

            // 4. 转换成 TETile[][]
            TETile[][] worldTiles = new TETile[WIDTH][HEIGHT];
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    worldTiles[x][y] = Blockworld[x][y].blockName;
                }
            }
            return worldTiles;
        }
        return null; // 如果输入不是 N...S
    }
}
