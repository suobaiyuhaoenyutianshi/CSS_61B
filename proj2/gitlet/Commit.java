package gitlet;

// TODO: any imports you need here

// TODO: 在此处导入你需要的任何类
import javax.xml.crypto.Data;
import java.util.Date; // TODO: 你可能会在这个类中使用它

/** 表示一个 gitlet 提交对象。
 *  TODO: 在这里给出一个高层次描述，说明这个类还做什么其他事情。
 *
 *  @author TODO
 */
public class Commit {
    /**
     * TODO: 在此处添加实例变量。
     *
     * 在这里列出 Commit 类的所有实例变量，并在每个变量上方添加有用的注释，
     * 描述该变量代表什么以及如何使用。我们为 `message` 提供了一个示例。
     */
    /** 此提交的消息。提交时用户输入的消息，即备注*/
    private String message;
    private Date timeStamp;
    private String parent1;
    private String parent2;
    //把哈希值存下来以免反复计算
    public String SHA;
    public Commit(String message ,String parent1 , String parent2){
        this.message = message;
        this.parent1 = parent1;
        this.parent2 = parent2;
        timeStamp = new Date();
        this.SHA = calSHA(message,parent1,parent2,this.timeStamp);
    }
    //自身SHA的计算
    //其他地方也可以用
    public String calSHA(String message,String parent1,String parent2 ,Date timeStamp){
         String SHA = Utils.sha1(message,parent1,parent2,timeStamp);
         return SHA;
    }

    /* TODO: 填写此类的其余部分。 */
}
//