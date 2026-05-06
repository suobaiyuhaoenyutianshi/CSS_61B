package byow.block;


import byow.TileEngine.Tileset;

public class doorBlock extends block {
    public static String Name = "Door";
    public doorBlock(int x,int y){
        super();
        this.blockName = Tileset.SAND;
        this.blood =0;
        through = true;
        this.room =-1;
        this.x = x;
        this.y =y;

        this.isanopenspace = true;
    }
    public String getName(){
        return Name;
    }



}
