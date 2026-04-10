package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;

//我觉得我的为文件blob存储是比较搞笑的，不是存储blob类型（有blob的序列化），而是文本，其实是因为我忘了，为了方便查看，但其实我可以专门为blob文件存储写个方式，之后可以该，现在是尾巴大，不想该
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
    //commit内容所在目录/是在object的
    public static final File COMMIT_path = Utils.join(GITLET_DIR.getPath(),"object","commit");
    //blob文件内容所在目录/是在object的
    public static final File BLOB_path = Utils.join(GITLET_DIR.getPath(),"object","blob");
    //暂存区里的缓冲区路径
    public static final File SnapSHOTCACHE_path = Utils.join(GITLET_DIR.getPath(),"index","SnapshotCache");
    //暂存区里的add区路径
    public static final File ADD_path = Utils.join(GITLET_DIR.getPath(),"index","staging_add");
    //暂存区里的rm区路径
    public static final File RM_path = Utils.join(GITLET_DIR.getPath(),"index","staging_rm");
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
            Commit startFile = new Commit("initial commit","","",start0);
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
            Utils.writeObject(Utils.join(indexFile, "staging_add"), emptyAdd);

            TreeMap<String,String> emptyRm = new TreeMap<>();

            Utils.writeObject(Utils.join(indexFile, "staging_rm"), emptyRm);
            TreeMap<String,String> emptySnapshotCache = new TreeMap<>();
            Utils.writeObject(Utils.join(indexFile,"SnapshotCache"),emptySnapshotCache);
            return;
        }
        //该目录存在，这报错
        System.out.println("\"A Gitlet version-control system already exists in the current directory.\"");
    }
    //返回当前分支指针的目录/文件,即.gitlet/refs/heads/。。。。
    public static File findBranch(){
        File HEADPath = Utils.join(Repository. GITLET_DIR.getPath(),"HEAD");
        String strHEAD = Utils.readContentsAsString(HEADPath);//读取是那个个分支
        File nowHEAD = Utils.join(Repository. GITLET_DIR.getPath(),"refs","heads",strHEAD);//找有头指针分支的commit的哈希的文件，注我虽然这么说，但分支就是是commit,这里也只是为了
        return nowHEAD;
    }
   //返回当前分支的commit文件
    public static File findBranchCommitFile(){
        File HEADPath = Utils.join(Repository. GITLET_DIR.getPath(),"HEAD");
        String strHEAD = Utils.readContentsAsString(HEADPath);//读取是那个个分支
        File nowHEAD = Utils.join(Repository. GITLET_DIR.getPath(),"refs","heads",strHEAD);//找有头指针分支的commit的哈希的文件，注我虽然这么说，但分支就是是commit,这里也只是为了得到committ的hash
        //读取文件记录commit的hash
        String commitHash = Utils.readContentsAsString(nowHEAD);
        //通过这个在object找commit
        File HEADcommit = Utils.join(COMMIT_path.getPath(),commitHash);
        return HEADcommit;
    }


    public static void copyToSnapShotCach(){
        //寻找当前分支的/////commit///找到committ就是找到TreeMap，TreeMap在commit
        //返回当前分支的commit文件
        File HEAdcommitFile = Repository.findBranchCommitFile();
        //反序列化
        Commit commitContent = Utils.readObject(HEAdcommitFile,Commit.class);
        //读取commit的TreeMAp
        TreeMap<String,String> NowCommittreeFiles= commitContent.commitFiles();
        //copy给SnapShotCache
        TreeMap<String,String> copy = new TreeMap<>(NowCommittreeFiles);
        //序列回个暂存区
        Utils.writeObject(Repository.SnapSHOTCACHE_path,copy);
    }
    public static void add(String addFileName){

        //然后在在该分支即commit里，如果SnapShotCache为空则copy里的TreeMap给SnapShotCach，不为空不copy
        //该方式给File 分支，它将该分支的TreeMap拷贝给SnapShotCache
        TreeMap<String,String> SnapShotCacheFile = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        if(SnapShotCacheFile.isEmpty()){
            Repository.copyToSnapShotCach();
        }
        //add文件路经
        File addFile = Utils.join(Repository.CWD,addFileName);
        //SHA-1计算它的hash-1，将文件名，与它的hash-1,存入暂存区add TreeMap中
        byte [] content = Utils.readContents(addFile);
        String blobSHA = Utils.sha1(content);
        //保存 blob 对象
        Utils.writeContents(Utils.join(BLOB_path,blobSHA),content);
        ///
        TreeMap<String,String> addTreeMap = Utils.readObject(Repository.ADD_path,TreeMap.class);

        addTreeMap.put(addFileName,blobSHA);
        TreeMap<String,String> rmContents = Utils.readObject(Repository.RM_path ,TreeMap.class);
        if(!rmContents.isEmpty()){
           for(String rmKey:rmContents.keySet()){
               if(addTreeMap.containsKey(rmKey)){
                   rmContents.remove(rmKey);
               }
           }
            //序列化覆盖之前的rm区
            Utils.writeObject(Repository.RM_path,rmContents);
        }
        Utils.writeObject(ADD_path,addTreeMap);//先加入add暂存区  在commit时与rm一起再考虑是否加入commit的Treemap中

    }
    /* TODO: 填写此类的其余部分。 */
    public static void rm(String rmFilename){
        TreeMap<String,String> SnapShotCacheFile = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        if(SnapShotCacheFile.isEmpty()){
            Repository.copyToSnapShotCach();
        }
        //要rm文件路径
        File rmFile = Utils.join(Repository.CWD,rmFilename);
        byte [] contents = Utils.readContents(rmFile);
        //物理删除，目录上面的文件
        if(rmFile.exists()){
            Utils.restrictedDelete(rmFile);
        }
        //加入rm区
        TreeMap<String,String> rmFileMap = Utils.readObject(Repository.RM_path,TreeMap.class);
        //删除目录上的文件

        rmFileMap.put(rmFilename,null);
        //检测add区
        TreeMap<String,String> addcontents = Utils.readObject(Repository.ADD_path,TreeMap.class);
        if(!addcontents.isEmpty()){
            for(String rmKey:rmFileMap.keySet()){
                if(addcontents.containsKey(rmKey)){
                    addcontents.remove(rmKey);
                }
            }
            //序列回add区
            Utils.writeObject(Repository.ADD_path,addcontents);
        }
        Utils.writeObject(Repository.RM_path,rmFileMap);
    }


    public static void commitFile(String message){
        //生成新的commit,父节点1是上次的commit的hsa，
        TreeMap<String,String> addcontents = Utils.readObject(Repository.ADD_path,TreeMap.class);
        TreeMap<String,String> rmContents = Utils.readObject(Repository.RM_path ,TreeMap.class);
        TreeMap<String ,String> snapShotCAche = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        //当前分支commit的TreeMAp
        Commit HEADCommit = Utils.readObject(Repository.findBranchCommitFile(),Commit.class);
        TreeMap<String,String> HEADTreeMap = HEADCommit.commitFiles();
        if(!addcontents.isEmpty()){
            snapShotCAche.putAll(addcontents);
        }
        if(!rmContents.isEmpty()){
            for(String rmKey:rmContents.keySet()){
                snapShotCAche.remove(rmKey);
            }

        }

        if(HEADTreeMap.equals(snapShotCAche)){
            System.out.println("No changes added to the commit.");
            return;
        }

        //将snapSHot输入回去
        Utils.writeObject(Repository.SnapSHOTCACHE_path,snapShotCAche);
        //生成commit给object/commit
        Commit submit = new Commit(message,HEADCommit.SHA,"",snapShotCAche);
        Utils.writeObject(Utils.join(Repository.COMMIT_path,submit.SHA),submit);
        // 更新当前分支指针
        //HEAD是分支映射，还在这分支，我们也没有新建分支.gitlet/refs/heads/没添加内容，修改.gitlet/refs/heads/master里master内容即可
       Utils.writeContents(Repository.findBranch(),submit.SHA);
       //清理暂存区的add区与rm区,snapshotCache 通常不需要清空，因为它应始终反映下一次提交的完整快照（在 commit 后应更新为新 commit 的 files，而不是清空）
        TreeMap<String, String> empty = new TreeMap<>();
        Utils.writeObject(Repository.ADD_path,empty);
        Utils.writeObject(Repository.RM_path,empty);
    }


    /**1. checkout -- [file name]
     目的：从当前提交（HEAD）中恢复单个文件到工作目录。
     影响范围：
     工作目录：指定的文件被覆盖为 HEAD 中的版本。
     暂存区：不改变。无论你用的是增量暂存（staging_add / staging_rm）还是完整快照（snapshotCache），这个命令都不会修改它们。
     当前分支：不变。
     是否会读取 commit 的 TreeMap 给 snapshot 区？
     不会。它只读取 HEAD commit 的 files 映射，找到该文件对应的 blob SHA，然后从 blob 对象中取出内容写入工作目录。暂存区的 snapshotCache 保持原样*/
    public static void checkFile(String fileName){
        //获得当前分支commit的序列化文件
        File nowBranch = Repository.findBranchCommitFile();
        Commit nowBranchCommit = Utils.readObject(nowBranch,Commit.class);
        TreeMap<String,String> nowCommitFiles = nowBranchCommit.commitFiles();
        if(!nowCommitFiles.containsKey(fileName)){
            //该文件在commit的TreeMap中记录为空，
            System.out.println("This file does not exist in the current branch");
            return;
        }
        String FileSHA = nowCommitFiles.get(fileName);
        //找到blob文件，并读取到目录上

        Repository.unloadBlob(FileSHA,fileName);


    }
    private static void unloadBlob(String blobName,String filename){

        File blobFile = Utils.join(Repository.BLOB_path,blobName);
        //String读取
        String blobContents = Utils.readContentsAsString(blobFile);
        //覆盖目录下该文件名的内容,write..会自动创建文件
        Utils.writeContents(Utils.join(Repository.CWD,filename),blobContents);
    }

/**checkout [branch name]
 目的：切换整个分支，将工作目录、暂存区、HEAD 全部切换到目标分支的状态。
 影响范围：
 工作目录：全部文件被替换为目标分支 HEAD commit 的快照（添加、删除、修改文件）
 暂存区：必须清空或更新。根据文档，切换分支后暂存区应被清空（因为暂存区是与分支相关的）。如果你使用 snapshotCache 作为下一次提交的完整快照，那么切换分支后，应该将 snapshotCache 更新为目标分支 HEAD commit 的 files 副本（而不是清空）。但文档明确要求“The staging area is cleared”，所以更合理的做法是清空 staging_add 和 staging_rm，并将 snapshotCache 设置为目标 commit 的 files 副本（以便后续 add 命令基于正确的快照）。
 当前分支：HEAD 指向目标分支。是否会读取 commit 的 TreeMap 给 snapshot 区？
 会。切换分支后，需要将目标 commit 的 files 映射复制到 snapshotCache（或类似结构）中，以确保下一次 add / commit 基于正确的快照。同时需要清空 staging_add 和 staging_rm。*/
//写了生成分支在写这个
        public static void checkBranch(){

        }



//跟checkFile差不多
    public static void checkCommitFilename(String commitId,String fileName){
            //支持缩写
        commitId = Repository.findFullCommitId(commitId);
        //为null返回
        if(commitId == null)return;

            //反序列读取commit——》读取TreeMap
        File pointCommitFile = Utils.join(Repository.COMMIT_path,commitId);
        Commit pointCommit = Utils.readObject(pointCommitFile,Commit.class);
        //读取TreeMap
        TreeMap<String,String> pointCommitTreeMap = pointCommit.commitFiles();
        if(!pointCommitTreeMap.containsKey(fileName)){
            System.out.println("this pointCommit not exist " + fileName);
            return;
        }
        //存在则
        String pointfileShA = pointCommitTreeMap.get(fileName);
        //找到blob文件，并读取到目录上
        Repository.unloadBlob(pointfileShA,fileName);


    }
    //通过几位数找到完整commit id
    private static String findFullCommitId(String commitId){
            //项目提供了工具方法
        //如果目录不存在，plainFilenamesIn 返回 null。
        //返回的列表是文件名（即 commit 的完整 SHA‑1 字符串），已按字典序排序
        List<String> commitFiles =Utils.plainFilenamesIn(Repository.COMMIT_path);
        List<String> findedCommitName = new ArrayList<>();
        for(String commitName:commitFiles){
            if(commitName.startsWith(commitId)){
                findedCommitName.add(commitName);
            }
        }
        if(findedCommitName.size() >1){
            System.out.println("请多输入几位数");
            return null;
        }
        if (findedCommitName.isEmpty()){
            System.out.println("该commit不存在");
            return null;
        }
        return findedCommitName.get(0);

    }
    //查看commit的溯源log
    public static void commitLog(String commitId){

    }

    //查看当前分支commit的溯源log
    public static void HEADlog(){

    }

}
