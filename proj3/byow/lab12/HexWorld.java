package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 *绘制一个由六边形区域组成的世界。
 */
public class HexWorld {

    //1，addHexagon 小6边形（world(数组)，坐标，边长）
    //2，randomTile 返回方块，从4种中随机（）
    //3，GenerateLargeHexagon生成大6边形的（world(数组)，小6边形的边长，坐标，大6边形的边长）
    //越界出问题，
    public void addHexagon(TETile[][] world,int x,int y,int edge){
        TETile tile = randomTile();
        int i = 0;
        int row = x;//横坐标
        int col = y;
        int length = edge;
        while(i < edge){

            for(int k = 0;k < length;k++){

                if(row+k >=0 && row+k <world.length && col >= 0&& col<world[0].length ){
                    world[row +k][col] = tile;
                }
            }
            row--;col++;i++;length +=2;
        }
        length -=2;row++;
        i = 0;
        while(i < edge){

            for(int k = 0;k < length;k++){

                if(row+k >=0 && row+k <world.length && col >= 0&& col<world[0].length ){
                    world[row +k][col] = tile;
                }
            }
            row++;col++;i++;length -= 2;
        }

    }
    //2
    private TETile randomTile(){
        int randNum = new Random().nextInt(4);
        switch (randNum){
            case 0 : return Tileset.FLOWER;
            case 1:return Tileset.SAND;
            case 2:return Tileset.GRASS;
            case 3:return Tileset.WALL;
            default: return Tileset.NOTHING;
        }
    }



//大的y的单位是小6边形
    public void GenerateLargeHexagon(TETile[][] world,int smallEdge,int bigEdge,int x,int y){
        int[] row = new int[]{x + smallEdge + ((smallEdge - 1)*2 + smallEdge)};
        int[] col = new int[]{ y - smallEdge*(bigEdge -1)};
        int num = bigEdge;
       for(int i =0;i < bigEdge;i++){
            //共big-1竖
            veritc(world,row,col,smallEdge,num);
            num++;
            row[0]= row[0]- (smallEdge -1) -smallEdge;
           col[0] = col[0] - smallEdge;


        }
      //big -1
        row[0] = x - smallEdge*bigEdge;
        col[0]=y - smallEdge*2;
        num = bigEdge;
        for(int i =0;i < bigEdge-1;i++){
            veritc(world,row,col,smallEdge,num);
            num++;
            row[0]= row[0]+ (smallEdge -1) +smallEdge;
            col[0] = col[0] - smallEdge;
        }






    }
    private void veritc(TETile[][] world,int[] x,int[] y,int smallEdge,int num){
        int inity =y[0];
        for (int i =0;i < num;i++){
            addHexagon(world,x[0],y[0],smallEdge);
            y[0] = y[0] + smallEdge *2;
        }
        y[0] = inity;
    }



    public static void main(String[] args){
        int width = 100;
        int hight = 50;
        TERenderer ter = new TERenderer();
        ter.initialize(width,hight);
        TETile[][] world = new TETile[width][hight];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < hight; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        //
         //
         //
        HexWorld test = new HexWorld();
       // test.addHexagon(world,3,20,4);
        int[] x= new int[]{50};int[] y = new int[]{40};
        //test.veritc(world,x,y,4,4);
        test.GenerateLargeHexagon(world,2,5,40,20);
        ter.renderFrame(world);
    }
}
