package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import byow.block.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import byow.graph.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* 您可以随意更改宽度和高度。*/
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public TETile[][] world;
//每个世界房间数量
    private final int MaxROOMS= 20;
    private final int MINROOMS = 8;
    public int roomNum;
    public int roomsdis = 15;
    private int area = roomsdis*roomsdis;
    //房间寻找依靠，尤其它的坐标与序号，注：链表的0对应就是房间0，这类里面也有它的序号避免，你弄错
    private List<ROOM> ROOMS;

    /**
     * 用于探索全新世界的探索方法。此方法应能处理所有输入内容，
     * 包括来自主菜单的输入。*/
    public void interactWithKeyboard() {
        //房间
        ROOMS = new ArrayList<>();
        //菜单N 表示“新世界”，L 表示“加载世界”，Q 表示退出。
        String s = menu();
       //根据s 处理
        if(s.equalsIgnoreCase("q")) return;
        //加载世界，自己创建的方块对象
       block[][] Blockworld= loadWorld(s);
       //最小图
        if (!s.equalsIgnoreCase("n")) {
            minGrap(Blockworld,this.ROOMS);
        }

        //渲染

            rendergraph(Blockworld);
        while (true) {
            StdDraw.pause(1000);       // 保持窗口不关闭，不重复渲染
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
        Random rand = new Random(seed);
        //房间数量,后面不可实现时他会修改
        this.roomNum = rand.nextInt(MaxROOMS - MINROOMS) + MINROOMS;
        return creatWorld(seed,rand);
    }


    /**只负责渲染，输入数组，渲染，什么颜色，内部逻辑都不归他管
     * */
    private  void rendergraph(block[][] world){
        int x = this.WIDTH;int y = this.HEIGHT;
        TETile[][] Tworld = new TETile[x][y];
        for(int i =0;i<x;i++){
            for(int j =0;j<y;j++){
                Tworld[i][j] = world[i][j].blockName;
            }
        }
        ter.initialize(this.WIDTH,this.HEIGHT);
        this.ter.renderFrame(Tworld);
    }
    private  block[][] creatWorld(long seed,Random rand){
        block[][] world = new block[this.WIDTH][this.HEIGHT];
        //初始
        initWorld(world);


        List<twoDim> rooms = new ArrayList<>();
       // rooms.add(new twoDim(rand.nextInt(this.WIDTH),rand.nextInt( this.HEIGHT)));
        int times =0;int idx =0;
        while( rooms.size() < this.roomNum&&times < 10000 && this.area*(idx+1)< (this.WIDTH*this.HEIGHT)/3*2){
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
                world[x][y]= new voidBlock();
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
    int x = towDim.x - this.roomsdis / 2;
    int y = towDim.y - this.roomsdis / 2;
    int xLast = x + this.roomsdis;
    int yLast = y + this.roomsdis;
    for (int i = y; i < yLast; i++) {
        for (int j = x; j < xLast; j++) {
            world[j][i] = new spaceBlock();
            if (i == y || i == yLast - 1 || j == x || j == xLast - 1) {
                boolean isWall = rand.nextInt(8) != 0; // 7/8 概率是墙
                if (isWall) {
                    world[j][i] = new WallBlock();
                    world[j][i].room = sigalRoom;
                } else {
                    world[j][i] = new flowerBlock();
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
/**路的生成，依靠房间序号-1，最后，不行我还要再写一种阻塞方块，为-2，围墙只能作为边界，他只后不能添加，之后添加阻塞方块来干扰路线，但若遇上房间为双方的变为门，所以只有房间号-1会变为空地
 * */
    private void thoughTwoRoom(block[][] blocks,ROOM T,ROOM V){

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
        // TODO：请完善此方法，使其使用作为参数传入的输入来运行引擎，并返回一个二维的图块表示形式，该表示形式代表了如果将相同的输入传递给 interactWithKeyboard() 函数时将会绘制出的世界画面。//
// 请参阅 proj3.byow.InputDemo 示例，了解如何创建一个美观简洁的界面，该界面能够适用于多种不同的输入类型。

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
}
