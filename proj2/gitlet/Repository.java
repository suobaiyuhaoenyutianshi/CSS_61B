package gitlet;

import java.io.File;
import java.util.TreeMap;

import static gitlet.Utils.*;


// TODO: 在此处导入你需要的任何类


/** 表示一个 gitlet 仓库。
 *  TODO: 在这里给出一个高层次描述，说明这个类还做什么其他事情。
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: 在此处添加实例变量。
     *
     * 在这里列出 Repository 类的所有实例变量，并在每个变量上方添加有用的注释，
     * 描述该变量代表什么以及如何使用。我们为你提供了两个示例。
     */

    /** 当前工作目录。 */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** .gitlet 目录。 */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    //初始化仓库
    public static void setuppresisit(){
        if(!GITLET_DIR.exists()){
            //如果.gitlet目录不存在，
            GITLET_DIR.mkdir();
            //存放所有 blob 和 commit 对象（以 SHA-1 命名的文件）
            File objects = Utils.join(GITLET_DIR.getPath(),"object");
            //先写个简单的，单级目录，
            objects.mkdir();
            File blob = Utils.join(objects.getPath(),"blob");
            File commit = Utils.join(objects.getPath(),"commit");
            //分别为blob与commit创建目录
            blob.mkdir();
            commit.mkdir();
            //创建refs
            File refs = Utils.join(GITLET_DIR.getPath(),"refs");
            refs.mkdir();
            //创建记录分支，即commit的hash的目录
            File heads = Utils.join(refs.getPath(),"heads");
            heads.mkdir();
            //初始要建个master分支内容为空
            File master =Utils.join(heads,"master");
            //传入序列化
            TreeMap<String,String> start0 = new TreeMap<>();
            Commit startFile = new Commit("start0","","",start0);
            Utils.writeContents(master,startFile.SHA);
            // 保存 commit 对象到 objects/commit/ 下，文件名用它的 SHA
            File commitInit = Utils.join(commit.getPath(),startFile.SHA);
            Utils.writeObject(commitInit,startFile);

            // 创建 HEAD 文件，内容为 "master"
            File headFile = Utils.join(GITLET_DIR, "HEAD");
            Utils.writeContents(headFile, "master");
            //暂存区目录
            File indexFile = Utils.join(GITLET_DIR, "index");
            indexFile.mkdir();
            // 创建两个空文件，并写入初始数据（空的 TreeMap 和 TreeSet）
            TreeMap<String, String> emptyAdd = new TreeMap<>();
            Utils.writeObject(Utils.join(GITLET_DIR, "staging_add"), emptyAdd);

            TreeMap<String,String> emptyRm = new TreeMap<>();
            Utils.writeObject(Utils.join(GITLET_DIR, "staging_rm"), emptyRm);
            return;
        }
        //该目录存在，这报错
        System.out.println("A GITlet exist!");
    }

    /* TODO: 填写此类的其余部分。 */
}
