package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;

public class testcolour {
    public static void main(String[] args){
        int width =20;int hight =20;
        TETile[][] world = new TETile[width][hight];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < hight; y += 1) {
               /** world[x][y] = Tileset.WALL;//城墙
                world[x][y] = Tileset.GRASS;//空地
                world[x][y] = Tileset.FLOOR;//空地
                world[x][y] = Tileset.AVATAR;//主角
                */
                world[x][y] = Tileset.SAND;//门

            }
        }
        TERenderer ter = new TERenderer();
        ter.initialize(width,hight);
        ter.renderFrame(world);
    }


}
