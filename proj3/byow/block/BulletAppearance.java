package byow.block;

import byow.TileEngine.Tileset;

public class BulletAppearance extends block{
    public static String Name = "Bullet";
    public BulletAppearance(int x,int y){
        this.blockName = Tileset.Bullet;
        this.revealed = true;
        this.destroy= true;
        this.through =false;
        this.x = x;
        this.y =y;
    }

    public String getName(){
        return Name;
    }

}
