package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;

    public static void main(String[] args) {
        // 使用宽度为 WIDTH、高度为 HEIGHT 的窗口初始化瓦片渲染引擎
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // 初始化瓦片
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // 填充一个宽 14 块、高 4 块的矩形区域
        for (int x = 0; x < 35; x += 1) {
            for (int y = 15; y < 30; y += 1) {
                world[x][y] = Tileset.FLOWER;
            }
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }


}
