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


public class Engine {
    TERenderer ter = new TERenderer();
    /* 您可以随意更改宽度和高度。*/
    public static final int WIDTH = 80;
    public static final int HEIGHT = 80;
    public TETile[][] world;
//每个世界房间数量
    private final int MaxROOMS= 20;
    private final int MINROOMS = 8;
    public int roomNum;
    public int roomsdis = 16;
    /**
     * 用于探索全新世界的探索方法。此方法应能处理所有输入内容，
     * 包括来自主菜单的输入。*/
    public void interactWithKeyboard() {
        //菜单N 表示“新世界”，L 表示“加载世界”，Q 表示退出。
        String s = menu();
       //根据s 处理
        if(s.equalsIgnoreCase("q")) return;
        //加载世界，自己创建的方块对象
       block[][] Blockworld= loadWorld(s);
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
    private static void rendergraph(block[][] world){
        int x = world.length;int y = world[0].length;
        TETile[][] Tworld = new TETile[x][y];
        for(int i =0;i<x;i++){
            for(int j =0;j<y;j++){
                Tworld[i][j] = world[i][j].blockName;
            }
        }
        TERenderer ter = new TERenderer();
        ter.initialize(x,y);
        ter.renderFrame(Tworld);
    }
    private  block[][] creatWorld(long seed,Random rand){
        block[][] world = new block[this.WIDTH][this.HEIGHT];
        //初始
        initWorld(world);
        //每个世界7个房间,半径为15，中心距离差至少10,即100

        List<twoDim> rooms = new ArrayList<>();
        rooms.add(new twoDim(rand.nextInt(this.WIDTH),rand.nextInt( this.HEIGHT)));
        int times =0;
        while( rooms.size() < this.roomNum){
            int x =rand.nextInt(this.WIDTH);int y = rand.nextInt(this.HEIGHT);
           if(isConformingDiffer(x,y,rooms)){
               if(times > 10000)break;
               rooms.add(new twoDim(x,y));
               times++;
           }
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
        boolean boundaryX = (x - 0)>this.roomsdis/3 &&(this.WIDTH -x)>this.roomsdis/3;
        boolean boundaryY = (y - 0)>this.roomsdis/3 && (this.HEIGHT -y)>this.roomsdis/3;
        if(!boundaryX||!boundaryY) return false;
        List<Integer> dis = new ArrayList<>(); int i=0;
        while(i < rooms.size()){
            int disTo = (x-rooms.get(i).x )*(x-rooms.get(i).x) +(y - rooms.get(i).y)*(y - rooms.get(i).y);
            if(disTo < this.roomsdis*roomsdis)return false;
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
    private void creatRoom(twoDim towDim,block[][] world,Random rand,int sigalRoom){
        int x = towDim.x-this.roomsdis/2;int y = towDim.y-this.roomsdis/2;
        int xLast = x + this.roomsdis;int yLast = y+ this.roomsdis;
        for(int i= towDim.y-this.roomsdis/2;i<yLast;i++){
            for (int j=towDim.x-this.roomsdis/2;j<xLast;j++){
                if(i<0||i>=this.HEIGHT||j<0||j>=this.WIDTH){
                    continue;
                }
                if(i==yLast-1||i== y||j==x||j==xLast-1){
                    boolean[] isWallorflower= new boolean[]{true,true,true,true,true,true,true,false};
                    if(isWallorflower[rand.nextInt(isWallorflower.length)]){
                        world[j][i]= new WallBlock();
                        world[j][i].room = sigalRoom;
                    }else world[j][i]= new flowerBlock();

                }
                else {
                    if(i ==0 || i==this.HEIGHT -1||j ==0||j==this.WIDTH-1){
                        world[j][i]= new WallBlock();
                        continue;
                    }
                    world[j][i] = new spaceBlock();
                }


            }
        }





    }
    public static  void main(String[] args){
        Engine test = new Engine();
        test.interactWithKeyboard();
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
