package byow;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.block.*;
public class role {
    public boolean burstFinish =false;//xxx
    public int blood;
    public block role;
    public int speedNum=4 ;//多走4步(默认)
    public int temporaryspped=0;
    public int speedFrame =0;
    public boolean weapon =true;//是否有武器
    //储存现在的位置，离开时要依靠这个复原
    public block place;
    public int moveCooldown =20;
    public role(int blood,block place){
        this.blood = blood;
        this.role= new roleappearance() ;
        this.place =place;

    }//恢复原先的位置，记录下次即将的位置,即之后的现在位置，且覆盖
    public boolean record(block nextPlace, block[][] world) {
        if (!nextPlace.through) return false;                // 直接用目标方块判断
        world[this.place.x][this.place.y] = this.place; // 恢复原地
        this.place = nextPlace;
        if (nextPlace.getName().equals(speedBlock.Name)) {
            speedBlock speed = (speedBlock)nextPlace;
            this.speedFrame = speed.speedFrame;
            this.speedNum = speed.speedNum;
        }
        world[nextPlace.x][nextPlace.y] = new roleappearance(); // 角色站上去
        return true;
    }

}
