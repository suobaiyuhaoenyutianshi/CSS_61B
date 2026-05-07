package byow.block;

import byow.TileEngine.Tileset;

public class decorateflowerBlock extends block{
    public static String Name = "decorateflowerBlock";
    public decorateflowerBlock(int x,int y){
        this.through =true;this.blood=0;this.blockName = Tileset.FLOWER;
        this.price = 3;
        this.room = -10;//装饰方块都是-10
        this.x = x;
        this.y =y;
        this.isanopenspace =true;

    }

    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return true;
    }


}
