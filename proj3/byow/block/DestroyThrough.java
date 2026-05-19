package byow.block;

import byow.TileEngine.Tileset;

public class DestroyThrough extends block{
    public static String Name = "destroyShowBlock";
    public DestroyThrough (int x,int y){
        this.blockName = Tileset.destroyTrough;
        this.revealed = true;
        this.destroy= false;//路不可破
        this.through =true;//可走
        this.x =x;this.y =y;
    }

    public String getName(){
        return Name;
    }
}
