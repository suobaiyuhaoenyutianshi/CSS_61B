package byow.block;

import byow.TileEngine.Tileset;


public class WallBlock extends block {
    public static String Name = "Wall";
    public WallBlock(int room,int x,int y){
        super();
        this.blood= 2;
        this.through = false;
        this.blockName = Tileset.WALL;
        this.price = 2;
        this.BecameDoor=true;
        this.room =room;
        this.x = x;
        this.y =y;

    }
    public String getName(){
        return Name;
    }

}
