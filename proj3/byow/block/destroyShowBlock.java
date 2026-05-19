package byow.block;

import byow.TileEngine.Tileset;

public class destroyShowBlock extends  block{
    public static String Name = "destroyShowBlock";
    public destroyShowBlock (int x,int y){
        this.blockName = Tileset.destroyShow;
        this.revealed = true;
        this.destroy= true;//周边的装饰自然可破坏
        this.through =false;//周边的装饰自然不可走
        this.x =x;this.y =y;
    }

    public String getName(){
        return Name;
    }
}
