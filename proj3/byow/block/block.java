package byow.block;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**一个新类，方块block,方块现在我只有3种，草地，空的，堡垒，门
 * */
public class block {
        //血量，名字
        //
        //装饰物的房间号为  -10
    //可变为装饰墙
    //可破坏的
    public boolean destroy = true;
    public boolean becameDecorateWall;
    public static String Name = "block";
     //是否可变为空地
    public boolean isanopenspace;
    //是否可以变为门
    public boolean BecameDoor;
    public int price;
    public int room;
    public int  blood;
    public TETile blockName;
    //通行，true可通行
    public boolean through;
    //坐标
    public int x;
    public int y;
    //是否揭露
    public boolean revealed = false;
    public void hurt(int x){
        if(x<=0) return;//这样不会使门变为为0时变为空地
        if((blood - x) <= 0){
            this.through = true;
            this.blood =0;
            //空地
            TETile[] spaceArea= new TETile[]{Tileset.GRASS,Tileset.FLOOR};
            this.blockName = spaceArea[new Random().nextInt(2)];
        }
    }
    public String getName(){
        return Name;
    }
    public boolean canBeDecorated(){
        return false;
    }
    public boolean isBecameDecorateWall(){
        return becameDecorateWall;
    }

    //该方块的所有有变量，我的设计理论来说，应该变量都有，但说实话这，如果要为专门的方块特殊的，只能为哪个类单独设计与检测
    public String SaveState(){
        return "type=" + getName() +
                ",room=" + room +
                ",blood=" + blood +
                ",through=" + through +
                ",isanopenspace=" + isanopenspace +
                ",BecameDoor=" + BecameDoor +
                ",price=" + price +
                ",revealed=" + revealed +
                ",becameDecorateWall=" + becameDecorateWall+
                ",x"+ this.x+
                ",y" + this.y+
                ",destroy" + this.destroy;



    }

    /**
     * 从状态字符串中恢复方块内部状态。各子类应重写，先调用 super.loadState() 恢复基类字段。
     * 状态字符串格式与 saveState() 对应，以逗号分隔，键值对用等号连接。
     */
    //java中没有元组，只有数组
    public void loadState(String data){
        String[] pairs = data.split(",");
        for(String pair:pairs){
            String[] KeyVal = pair.split("=",2);
            if(KeyVal.length !=2)continue;
            String K = KeyVal[0].trim();//.trim() 是 Java 字符串的一个方法，用于移除该字符串前后的空白字符（包括空格、制表符 \t、换行符 \n、回车 \r 等）。中间的空白会保留。
            String V = KeyVal[1].trim();
            switch (K) {
                case "room":   this.room = Integer.parseInt(V); break;
                case "blood":  this.blood = Integer.parseInt(V); break;
                case "through": this.through = Boolean.parseBoolean(V); break;

                case  "destroy": this.destroy = Boolean.parseBoolean(V);break;
                case "isanopenspace": this.isanopenspace = Boolean.parseBoolean(V); break;
                case "BecameDoor": this.BecameDoor = Boolean.parseBoolean(V); break;
                case "price":  this.price = Integer.parseInt(V); break;
                case "revealed": this.revealed = Boolean.parseBoolean(V); break;
                case "becameDecorateWall": this.becameDecorateWall = Boolean.parseBoolean(V); break;
                case "x": this.x = Integer.parseInt(V); break;
                case "y": this.y = Integer.parseInt(V); break;

                // type 字段在创建对象时已经确定
            }
        }

    }


}















