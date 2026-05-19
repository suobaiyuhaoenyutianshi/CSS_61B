package byow.block;

import byow.TileEngine.Tileset;

public class roleappearance extends block{
    public static String Name = "role";
    public roleappearance(){
        this.blockName = Tileset.AVATAR;
        this.revealed = true;
        this.destroy= false;
    }
    public String getName(){
        return Name;
    }
}
