package byow.lab13;

// MemoryGameDemo.java
// 可直接运行，无需依赖其他文件（除了 StdDraw）
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGameDemo {
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
        // 默认种子，可手动修改
        long seed = 12345;
        if (args.length >= 1) {
            seed = Long.parseLong(args[0]);
        }
        MemoryGameDemo game = new MemoryGameDemo(40, 40, seed);
        game.startGame();
    }

    public MemoryGameDemo(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        if (!gameOver) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            String task = playerTurn ? "Type!" : "Watch!";
            StdDraw.text(width / 2.0, height - 1, task);
            String encourage = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            StdDraw.textRight(width - 1, height - 1, encourage);

        }
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width / 2.0, height / 2.0, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(String.valueOf(letters.charAt(i)));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder input = new StringBuilder();
        while (input.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                input.append(c);
                drawFrame(input.toString());
            }
            StdDraw.pause(50);
        }
        return input.toString();
    }

    public void startGame() {
        gameOver = false;
        round = 1;
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1000);

            String target = generateRandomString(round);
            flashSequence(target);

            playerTurn = true;
            String userInput = solicitNCharsInput(round);

            if (userInput.equals(target)) {
                round++;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
                StdDraw.pause(3000);
                System.exit(0);
            }
        }
    }
}