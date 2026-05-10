package byow.block;

import byow.TileEngine.Tileset;

public class decorateWallBlock extends block{

    public static String Name = "deWall";
    public decorateWallBlock(int x,int y){
        super();
        this.blood= 2;
        this.through = false;
        this.blockName = Tileset.decorateWALL;
        this.price = 2;
        this.room =-10;
        this.x = x;
        this.y =y;
        this.becameDecorateWall =false;//因为路可能交叉
        this.isanopenspace =true;//因为路可能交叉

    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return true;
    }

}
