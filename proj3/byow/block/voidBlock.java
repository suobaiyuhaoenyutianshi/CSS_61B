package byow.block;

import byow.TileEngine.Tileset;

public class voidBlock extends block{
    public voidBlock(int x,int y){
        super();
        this.blockName = Tileset.NOTHING;
        this.through = false;
        this.blood = Integer.MAX_VALUE;
        this.room =-1;
        this.price = 2;
        this.isanopenspace =true;
        this.x = x;
        this.y =y;
    }
}
