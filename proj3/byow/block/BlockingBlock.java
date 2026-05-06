package byow.block;

import byow.TileEngine.Tileset;
//房间号-110
public class BlockingBlock extends block{
    public static String Name = "decorateflowerBlock";
    public BlockingBlock(int x,int y){
        this.through =true;this.blood=0;this.blockName = Tileset.MOUNTAIN;
        this.price = 3;
        this.room = -110;
        this.x = x;
        this.y =y;
        this.isanopenspace =false;

    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return false;
    }
}
