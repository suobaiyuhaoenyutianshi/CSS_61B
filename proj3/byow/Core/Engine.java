package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;


public class Engine {
    TERenderer ter = new TERenderer();
    /* 您可以随意更改宽度和高度。*/
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * 用于探索全新世界的探索方法。此方法应能处理所有输入内容，
     * 包括来自主菜单的输入。*/
    public void interactWithKeyboard() {
        //菜单N 表示“新世界”，L 表示“加载世界”，Q 表示退出。
        menu();

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
        return s;
    }

    /**只负责渲染，输入数组，渲染，什么颜色，内部逻辑都不归他管
     * */
    public static  void main(String[] args){
      menu();
    }
    private void rendergraph(TETile[][] world){

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
