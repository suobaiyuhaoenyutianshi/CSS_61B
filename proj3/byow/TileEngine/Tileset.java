package byow.TileEngine;

import java.awt.Color;

/**
 * 包含固定的图块对象，以避免在代码的不同部分重复创建相同的图块。*
 * 您可以自由地（并且被鼓励去）为这份文件添加您自己的图块。这份文件将与您的其他代码一并提交。*
 * 示例：
 *      world[x][y] = Tileset.FLOOR*
 * 如果您尝试对此文件进行样式检查时出现错误，可能是由于使用了 Unicode 字符所致。这完全正常。*/

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");

    public static final TETile deFLOOR = new TETile('·', new Color(128, 170, 192), Color.orange,
            "decoratefloor");

    public static final TETile decorateWALL = new TETile('#', new Color(128, 156, 216), Color.darkGray,
            "decoratewall");
    public static final TETile deGRASS = new TETile('"', Color.green, Color.yellow, "grass");
    public static final TETile deUNLOCKED_DOOR = new TETile('▢', Color.orange, Color.green,
            "unlocked door");

    public static final TETile myUNLOCKED_DOOR = new TETile('^', Color.orange, Color.WHITE,
            "unlocked door");

    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");


}


