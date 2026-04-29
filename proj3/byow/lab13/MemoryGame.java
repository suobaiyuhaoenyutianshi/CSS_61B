package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
/**generateRandomString(n)：用 Random(seed) 生成长度为 n 的随机小写字母串，保证可复现。

 drawFrame(s)：清除画布 → 设置字体 → 居中绘制字符串 → 显示。这是 StdDraw 的核心渲染循环：每次改画面都是“先擦干净，再重画所有东西”。

 flashSequence(target)：逐个字母闪烁显示目标字符串。每个字母显示 1 秒，间隔 0.5 秒空白。

 solicitNCharsInput(n)：用 StdDraw 的键盘监听 API 读取 n 个按键，返回玩家输入的字符串。让玩家实时看到自己输入了什么。

 startGame()：把所有方法串起来的游戏主循环：更新轮次 → 显示 “Round: X” → 生成目标串 → 闪烁展示 → 等待输入 → 判断对错 → 继续下一轮或显示 “Game Over”。

 💡 关键知识点
 StdDraw 渲染循环：clear() → 画所有内容 → show()。每次改东西都要重绘整个画面。

 键盘输入：用 hasNextKeyTyped() 轮询是否有按键，用 nextKeyTyped() 取出。注意无退格键支持。

 种子随机：用 Random(seed) 保证游戏可复现。

 UI 增强：在顶部状态栏显示轮次、当前任务（Watch/Type）和随机选的一句鼓励语。这正好练习了 StdDraw 的文字绘制和布局。*/
@SuppressWarnings("ALL")
public class MemoryGame {
    /** 本游戏窗口的宽度。*/
    private int width;
    /** 本游戏窗口的高度。*/
    private int height;
    /** 当前用户所处的轮次。*/
    private int round;
    /** 用于随机生成字符串的随机对象。*/
    private Random rand;
    /** 游戏是否已经结束。*/
    private boolean gameOver;
    /** 表示当前是否为玩家的回合。在规范的最后部分“有用的用户界面”中使用。*/
    private boolean playerTurn;
    /** 我们生成随机字符串所依据的字符集。*/
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** 鼓励性话语。用于规范的最后部分“实用用户界面”中。*/
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* 配置 StdDraw，使其画布的尺寸为 16×16 像素的网格
         * 同时设定比例，使左上角坐标为 (0，0)，右下角坐标为 (宽度， 高度)*/
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //  TODO：初始化随机数生成器
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        // 任务说明：生成长度为 n 的随机字母字符串
        String t= "";
        for(int i =0;i < n;i++){
           char c = this.CHARACTERS[this.rand.nextInt(26)];
            t=t+c;
        }
        return t;
    }

    public void drawFrame(String s) {
        //待办事项：获取该字符串，并将其显示在屏幕的中心位置
//待办事项：若游戏尚未结束，则在屏幕顶部显示相关的游戏信息
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco",Font.BOLD , 30));
        if(!gameOver) {// 黑色背景

            switch (new Random().nextInt(2)) {
                case 0:
                    StdDraw.setPenColor(StdDraw.WHITE);
                    break;
                case 1:
                    StdDraw.setPenColor(StdDraw.GREEN);
                    break;
            }       // 白色文字
            String k = "round :" + this.round;
            StdDraw.setFont(new Font("Monaco",Font.BOLD , 40));
            StdDraw.text(width / 2.0 -15 , height/2 + 16,k);
            Font font = new Font("Monaco",Font.BOLD , 100);
            StdDraw.setFont(font);
            StdDraw.text(this.width / 2.0, this.height / 2.0, s);
            StdDraw.setFont(new Font("Monaco",Font.BOLD , 30));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.rectangle(this.width / 2, this.height / 2 + 1.5, 10, 3.5);
            StdDraw.setPenColor(StdDraw.CYAN);
            StdDraw.circle(this.width / 2, this.height / 6 - 7, 8);
            StdDraw.circle(this.width / 2 - 16, this.height / 6 + 4, 5);
            StdDraw.circle(this.width / 2 + 15, this.height / 6 - 1, 5);
            font = new Font("Monaco", Font.PLAIN, 20);
            StdDraw.setFont(font);
            StdDraw.text(this.width / 2, this.height / 6 - 5, ENCOURAGEMENT[new Random().nextInt(7)]);
            StdDraw.text(this.width / 2 - 16, this.height / 6 + 4, ENCOURAGEMENT[new Random().nextInt(7)]);
            StdDraw.text(this.width / 2 + 15, this.height / 6 - 1, ENCOURAGEMENT[new Random().nextInt(7)]);
            StdDraw.setFont(new Font("Monaco",Font.BOLD , 70));
            StdDraw.text(this.width / 2 , this.height / 2 + 16, "watch");
        }//  StdDraw.filledRectangle(this.width/2, this.height/2+1.5, 10, 3.5);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2.0, height / 2.0, s);StdDraw.show();
    }

    public void flashSequence(String letters) {
        // 任务说明：将每个字符以字母形式显示出来，同时确保在每个字符之间清空屏幕
        for(int i = 0;i < letters.length();i++){
            char c = letters.charAt(i);
            drawFrame(String.valueOf(c));
            StdDraw.pause(5000);
        }

    }

    public String solicitNCharsInput(int n) {
        // 任务说明：读取玩家输入的 n 个字母
        StdDraw.pause(5000);
        return null;
    }

    public void startGame() {
        // 说明：在游戏开始前需设置好所有相关变量
//待办事项：建立引擎循环

       // this.drawFrame("aaaa");
        //StdDraw.pause(5000);
        this.flashSequence("abcdef");
    }

}
