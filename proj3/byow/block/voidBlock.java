package byow.block;

import byow.TileEngine.Tileset;

public class voidBlock extends block{
    public static String Name = "void";
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
        //可变为装饰墙
        this.becameDecorateWall = true;

    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return true;
    }

}
