package byow.Core;
/**
 *联通次数一个房间最多有俩次
 * */
public class ROOM implements Comparable<ROOM>{
    public int roomNum;
    public int XLoc;
    public int Yloc;
    int UnionNum;
    int area;
    public ROOM(int roomNum,int XLoc,int Yloc,int area){
        this.roomNum =roomNum;
        this.XLoc=XLoc;
        this.Yloc =Yloc;
        //好像不需要this.UnionNum =0;
        this.area =area;
    }
    @Override
    public int compareTo(ROOM other) {
        return Integer.compare(this.roomNum, other.roomNum);
    }
}
