package byow.block;

import byow.TileEngine.Tileset;


public class WallBlock extends block {
    public WallBlock(int room){
        super();
        this.blood= 2;
        this.through = false;
        this.blockName = Tileset.WALL;
        this.price = 2;
        this.BecameDoor=true;
        this.room =room ;
    }
}
