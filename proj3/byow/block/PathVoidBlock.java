package byow.block;

import byow.TileEngine.Tileset;

public class PathVoidBlock extends block{
    public PathVoidBlock(){
        super();
        this.blockName = Tileset.GRASS;
        this.through = true;
        this.blood = Integer.MAX_VALUE;
        this.room = -3;//路径上的方块为房间-3
        this.price = 1;
        this.isanopenspace =true;
        this.Name = "Path";
    }
}
