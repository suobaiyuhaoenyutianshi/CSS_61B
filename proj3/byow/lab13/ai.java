package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class ai {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {
            "You can do this!", "I believe in you!", "You got this!",
            "You're a star!", "Go Bears!", "Too easy for you!", "Wow, so impressive!"
    };

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        long seed = Long.parseLong(args[0]);
        ai game = new ai(40, 40, seed);
        game.startGame();
    }

    public ai(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        // 初始化随机数生成器
        this.rand = new Random(seed);
    }

    /** 生成长度为 n 的随机小写字母字符串 */
    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(CHARACTERS.length);
            sb.append(CHARACTERS[index]);
        }
        return sb.toString();
    }

    /**
     * 在屏幕中央绘制字符串 s，并显示顶部状态栏（若游戏未结束）。
     * 这是所有渲染的核心，其他方法通过调用它来更新画面。
     */
    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        // 如果游戏还未结束，绘制顶部状态栏
        if (!gameOver) {
            // 左侧：轮次
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            // 中间：当前任务
            String task = playerTurn ? "Type!" : "Watch!";
            StdDraw.text(width / 2.0, height - 1, task);
            // 右侧：随机鼓励语
            String encourage = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            StdDraw.textRight(width - 1, height - 1, encourage);
        }
        // 中央显示传入的字符串
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2.0, height / 2.0, s);
        StdDraw.show();
    }

    /** 逐个字母闪烁显示目标字符串，每个字母显示 1 秒，间隔 0.5 秒空白 */
    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            // 显示单个字母
            drawFrame(String.valueOf(letters.charAt(i)));
            StdDraw.pause(1000);
            // 清屏（显示空字符串）
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    /** 读取玩家输入的 n 个字符，并实时显示当前输入的字符串 */
    public String solicitNCharsInput(int n) {
        StringBuilder input = new StringBuilder();
        while (input.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                input.append(c);
                // 刷新显示：让玩家看到自己已经输入的字符
                drawFrame(input.toString());
            }
            // 短暂休眠，避免 CPU 空转
            StdDraw.pause(50);
        }
        return input.toString();
    }

    /** 游戏主循环 */
    public void startGame() {
        gameOver = false;
        round = 1;

        while (!gameOver) {
            // 1. 显示当前轮数
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1000);  // 给玩家一点时间看清轮数

            // 2. 生成目标字符串
            String target = generateRandomString(round);

            // 3. 闪烁展示目标字符串
            flashSequence(target);

            // 4. 等待玩家输入
            playerTurn = true;
            String userInput = solicitNCharsInput(round);

            // 5. 判断对错
            if (userInput.equals(target)) {
                round++;          // 正确，进入下一轮
            } else {
                gameOver = true;
                // 显示游戏结束信息
                drawFrame("Game Over! You made it to round: " + round);
                StdDraw.pause(3000);  // 停留 3 秒，让玩家看清结果
                System.exit(0);
            }
        }
    }
}