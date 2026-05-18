package byow.block;

import byow.TileEngine.Tileset;

public class PathVoidBlock extends block{
    public static String Name = "Path";
    public PathVoidBlock(int x,int y){
        super();
        this.blockName = Tileset.deFLOOR;
        this.through = true;
        this.blood = Integer.MAX_VALUE;
        this.room = -3;//路径上的方块为房间-3
        this.price = 1;
        this.isanopenspace =true;
        this.x=x;
        this.y =y;
        this.destroy= false;

    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return false;
    }

}
