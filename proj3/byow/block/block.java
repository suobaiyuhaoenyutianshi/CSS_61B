package byow.block;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**一个新类，方块block,方块现在我只有3种，草地，空的，堡垒，门
 * */
public class block {
        //血量，名字
        //
        //装饰物的房间号为  -10
    //可变为装饰墙
    public boolean becameDecorateWall;
    public static String Name = "block";
     //是否可变为空地
    public boolean isanopenspace;
    //是否可以变为门
    public boolean BecameDoor;
    public int price;
    public int room;
    public int  blood;
    public TETile blockName;
    //通行，true可通行
    public boolean through;
    //坐标
    public int x;
    public int y;
    //是否揭露
    public boolean revealed = false;
    public void hurt(int x){
        if(x<=0) return;//这样不会使门变为为0时变为空地
        if((blood - x) <= 0){
            this.through = true;
            this.blood =0;
            //空地
            TETile[] spaceArea= new TETile[]{Tileset.GRASS,Tileset.FLOOR};
            this.blockName = spaceArea[new Random().nextInt(2)];
        }
    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return false;
    }
    public boolean isBecameDecorateWall(){
        return becameDecorateWall;
    }
}
//////////////////////














