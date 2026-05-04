package byow.block;

import byow.TileEngine.Tileset;

public class flowerBlock extends block{
    public flowerBlock(int room,int x,int y){
        this.through =true;this.blood=0;this.blockName = Tileset.FLOWER;
        this.price = 1;
        this.room = room;
        this.x = x;
        this.y =y;
    }
}
