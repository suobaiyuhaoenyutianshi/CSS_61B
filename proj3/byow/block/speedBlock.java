package byow.block;

import byow.TileEngine.Tileset;

public class speedBlock extends block{
    public static String Name =  "SPEED";
    public int speedNum =10;
    //加速多久多少帧率
    public int speedFrame;//在这期间每按一次，快速向前步
    public speedBlock(int x,int y){
        this.through = true;this.x =x;this.y = y;
        this.speedFrame =300;
        this.blockName = Tileset.Speeed;
        this.destroy =false;

    }
    public String getName(){
        return Name;
    }
    public int rReturnSpeedFrame(){
        return speedFrame;
    }
}
