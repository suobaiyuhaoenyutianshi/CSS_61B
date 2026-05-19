package byow;
import byow.block.*;

import java.util.Random;

public class Bullet {
    int x;//主角位置，初始化后会变为子弹位置
    int y;//
    int dx;//方向
    int dy;
    public block bullet;
    public block place;
    public boolean isExist =true;//存在
    public final int moveInterval =2;
    public int moveCooldown =1;//等于moveInterval时，例如这就时过了2帧后，就在第3帧才可以动;当然这也取决与你在那个为更新，在rendGrap前就1帧
    //传进来时那方向的，例主角（0.1）子弹（0，2）,你检测在传进来，如果就时可破坏物，直接破坏，别传了/别建了
    public Bullet(block place,int x,int y,int dx,int dy,block[][]world){
     //就是子弹现在的位置
        this.place = place;this.x=x;this.y= y;this.dx=dx;this.dy = dy;this.bullet=new BulletAppearance(x,y);
        world[x][y]=bullet;

    }
    //先调用这
    public void record(block[][] world, Random seed){
        if(this.moveCooldown != this.moveInterval)return;//时间未到
        this.x = x+dx;this.y=y+dy;//子弹将到位置
        if(x>=world.length||x<0||y<0||y>=world[0].length){
            this.isExist = false;
            return;
        }
        //我一定要跟你强调你的设计是没接触是破坏
        block nextBlock = world[x][y];
        if(nextBlock.destroy){
            int k = seed.nextInt(2);
            int p =seed.nextInt(2);
            for(int i = x-k;i<=x+p;i++){
                for (int j =y-p;j<=y+k;j++){
                    if(i>=world.length||i<0||j<0||j>=world[0].length)continue;
                    if(world[i][j].destroy){
                        //避免破坏角色
                        if(!world[place.x][place.y].getName().equals(roleappearance.Name)){
                            world[place.x][place.y] =this.place;
                        }
                       // world[place.x][place.y] =this.place;
                       // world[place.x][place.y] =this.place;
                        if(i==x&&j==y){
                            world[i][j] = new DestroyThrough(i,j);
                        }else if(seed.nextInt(10)==2){
                            world[i][j] = new DestroyThrough(i,j);
                        }
                        else world[i][j] = new destroyShowBlock(i,j);
                    }

                }
            }
            this.isExist =false;
        }
        else{
            this.isExist=true;
            world[place.x][place.y] =this.place;
            this.place = nextBlock;
            bullet.x = x;bullet.y =y;
            world[x][y]= this.bullet;

        }


        this.moveCooldown =0;
    }
//在主循环例依靠这个判断时将这个对象删除
    public boolean isExist(){
        return isExist;
    }

    //是否



}
