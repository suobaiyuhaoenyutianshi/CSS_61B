package byow.block;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class spaceBlock extends block{
    public static String Name =  "SPACEBlock";
    public spaceBlock(int room,int x,int y){
        super();
        this.blood= 0;
        this.through = true;
        //空地
        TETile[] spaceArea= new TETile[]{Tileset.GRASS,Tileset.FLOOR};
        this.blockName = spaceArea[new Random().nextInt(2)];
        this.price = 1;
        this.isanopenspace = true;
        this.room = room;
        this.x = x;
        this.y =y;

    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return true;
    }
}
