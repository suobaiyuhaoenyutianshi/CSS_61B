package byow.block;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class decorateSpaceblock extends block{
        public static String Name =  "decorateSpaceblock";
        public decorateSpaceblock(int x,int y){
            super();
            this.blood= 0;
            this.through = true;
            //空地
            TETile[] spaceArea= new TETile[]{Tileset.deGRASS,Tileset.deFLOOR};
            this.blockName = spaceArea[new Random().nextInt(2)];
            this.price = 1;
            this.isanopenspace = true;
            this.room = -10;//装饰方块都是-10
            this.x = x;
            this.y =y;
            this.becameDecorateWall = false;
            this.destroy = false;//不可破坏

        }
        public String getName(){
            return Name;
        }



}
