package gitlet;

// TODO: any imports you need here

// TODO: 在此处导入你需要的任何类
import edu.princeton.cs.algs4.ST;

import javax.xml.crypto.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date; // TODO: 你可能会在这个类中使用它
import java.util.TreeMap;

/** 表示一个 gitlet 提交对象。
 *  TODO: 在这里给出一个高层次描述，说明这个类还做什么其他事情。
 *
 *  @author TODO
 */
public class Commit implements Serializable, Dumpable {
    /**
     * TODO: 在此处添加实例变量。
     *
     * 在这里列出 Commit 类的所有实例变量，并在每个变量上方添加有用的注释，
     * 描述该变量代表什么以及如何使用。我们为 `message` 提供了一个示例。
     */
    /** 此提交的消息。提交时用户输入的消息，即备注*/
    private String message;
    private Date timeStamp;
    private String parent1;//commit
    private String parent2;//commit
    private TreeMap<String, String> files; // 文件名 -> blob SHA-1 ,因为blob只存了文件序列化，没有文件名，需要文件名做建，保留在commit中做对应
    //把哈希值存下来以免反复计算
    public String SHA;
    public Commit(String message , String parent1 , String parent2, TreeMap<String,String> files,String date){
        this(message,parent1,parent2,files);
        this.timeStamp = new Date(0);

    }
    public Commit(String message , String parent1 , String parent2, TreeMap<String,String> files){
        this.message = message;
        this.parent1 = parent1;
        this.parent2 = parent2;
        timeStamp = new Date();
        this.files = files;
        this.SHA = calSHA();
    }
    //自身commit SHA的计算

    public String calSHA(){
        return Utils.sha1(message, parent1, parent2, timeStamp.toString(), files.toString());
    }

    /* TODO: 填写此类的其余部分。 */

    public TreeMap<String,String> commitFiles(){
        return this.files;
    }
    public String calParent1(){
        return  this.parent1;
    }
    public String calParent2(){
        return  this.parent2;
    }
    public String calData(){
        return this.timeStamp.toString();
    }
    public String calSHa(){
        return this.SHA;
    }
    public String calMessege(){
        return this.message;
    }


    public String getShortSHA() {
        return SHA.substring(0, 7);
    }
    @Override
    public void dump() {
        System.out.println("Commit:");
        System.out.println("  message: " + message);
        System.out.println("  timestamp: " + timeStamp);
        System.out.println("  parent1: " + parent1);
        System.out.println("  parent2: " + parent2);
        System.out.println("  files: " + files);
    }
}
//