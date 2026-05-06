package byow.block;

import byow.TileEngine.Tileset;

public class flowerBlock extends block{
    public static String Name = "Flower";
    public flowerBlock(int room,int x,int y){
        this.through =true;this.blood=0;this.blockName = Tileset.FLOWER;
        this.price = 3;
        this.room = room;
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
