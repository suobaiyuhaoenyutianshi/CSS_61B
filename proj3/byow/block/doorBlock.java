package byow.block;


import byow.TileEngine.Tileset;

public class doorBlock extends block {
    public doorBlock(){
        super();
        this.blockName = Tileset.SAND;
        this.blood =0;
        through = true;
        this.room =-1;
    }


}
