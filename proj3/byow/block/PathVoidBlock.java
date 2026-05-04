package byow.block;

import byow.TileEngine.Tileset;

public class PathVoidBlock extends block{
    public PathVoidBlock(){
        super();
        this.blockName = Tileset.NOTHING;
        this.through = false;
        this.blood = Integer.MAX_VALUE;
        this.room =-1;
        this.price = 1;
        this.isanopenspace =true;
    }
}
