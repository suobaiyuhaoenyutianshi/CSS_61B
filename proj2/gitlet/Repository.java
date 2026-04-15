package gitlet;

import com.sun.source.tree.Tree;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.concurrent.Callable;
import java.io.File;
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
            Commit startFile = new Commit("initial commit","","",start0,"");
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
        //检测snapShotCache区有没有它，有就不加blob，也不用加入add区
        String cachedSHA = SnapShotCacheFile.get(addFileName);
        if( cachedSHA != null&& cachedSHA.equals(blobSHA)) return;
        //保存 blob 对象
        Utils.writeContents(Utils.join(BLOB_path,blobSHA),content);
        ///
        TreeMap<String,String> addTreeMap = Utils.readObject(Repository.ADD_path,TreeMap.class);

        addTreeMap.put(addFileName,blobSHA);
        TreeMap<String,String> rmContents = Utils.readObject(Repository.RM_path ,TreeMap.class);

        if(!rmContents.isEmpty()){
            if(rmContents.get(addFileName) != null){
                rmContents.remove(addFileName);
            }
            //序列化覆盖之前的rm区
            Utils.writeObject(Repository.RM_path,rmContents);
        }


        Utils.writeObject(ADD_path,addTreeMap);//先加入add暂存区  在commit时与rm一起再考虑是否加入commit的Treemap中

    }
    /* TODO: 填写此类的其余部分。如果文件在 staging_add 中：
只从 staging_add 中移除该文件，不加入 staging_rm，不删除工作目录文件。
（因为用户之前 add 了但未提交，现在取消暂存，文件应该保留在工作目录中。）

否则，如果文件在 snapshotCache 中（注意：snapshotCache 代表下一次提交的完整快照，它初始来自当前 commit，然后被 add 更新过吗？你的 add 没有更新 snapshotCache，所以 snapshotCache 始终是当前 commit 的快照，除非 commit 后更新。实际上，在你的流程中，snapshotCache 在 commit 后会被更新为新提交的 files。因此，snapshotCache 在未提交期间反映的是上一次提交的快照，而不是累积了 add 和 rm 后的快照。这有点混乱，但我们可以这样理解：snapshotCache 是“当前 commit 的文件快照”，而 staging_add 和 staging_rm 是增量。所以判断文件是否被“跟踪”应该看 snapshotCache（因为它是当前 commit 的快照）。）

因此，如果文件不在 staging_add 中，但存在于 snapshotCache 中（即被当前 commit 跟踪），那么：

将该文件加入 staging_rm（标记删除）。

删除工作目录中的该文件（如果存在）。

否则（既不在 staging_add，也不在 snapshotCache 中）：
报错 "No reason to remove the file." */
    public static void rm(String rmFilename){
        TreeMap<String,String> SnapShotCacheFile = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        if(SnapShotCacheFile.isEmpty()){
            Repository.copyToSnapShotCach();
        }
        //要rm文件路径
        File rmFile = Utils.join(Repository.CWD,rmFilename);
        byte [] contents = Utils.readContents(rmFile);

        TreeMap<String,String> addcontents = Utils.readObject(Repository.ADD_path,TreeMap.class);

        if(!addcontents.isEmpty()){
                if(addcontents.containsKey(rmFilename)){
                    addcontents.remove(rmFilename);
                }
            //序列回add区
            Utils.writeObject(Repository.ADD_path,addcontents);
                return;
        }

        if(SnapShotCacheFile.containsKey(rmFilename)){
            if(rmFile.exists()){
                Utils.restrictedDelete(rmFile);
            }
        }
        else {
                System.out.println("No reason to remove the file.");
                return;
            }

                //加入rm区
                TreeMap<String,String> rmFileMap = Utils.readObject(Repository.RM_path,TreeMap.class);
                rmFileMap.put(rmFilename,null);
                Utils.writeObject(Repository.RM_path,rmFileMap);
            }

    public static void commitFile(String message){
        if (message == null || message.trim().isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }
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
            System.out.println("File does not exist in that commit.");
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
        public static void checkBranch(String branchName){
            List<String> points = Utils.plainFilenamesIn(Utils.join(Repository.GITLET_DIR,"refs","heads"));
            if(!points.contains(branchName)){
                System.out.println("the branch does not exist.");
                return;
            }
            TreeMap<String ,String> addStage = Utils.readObject(Repository.ADD_path,TreeMap.class);
            TreeMap<String,String> rmStage = Utils.readObject(Repository.RM_path,TreeMap.class);
                if(addStage != null && !addStage.isEmpty() || rmStage != null && !rmStage.isEmpty()){
                    System.out.println("the content in the stage has not committed.");
                    return;
                }
                TreeMap<String,String> files = Repository.ModificationItems(addStage,rmStage);
                if( !files.isEmpty()){
                    System.out.println("\"There is an untracked file in the way; delete it, or add and commit it first.\"");
                    return;

                }
                //去除跟踪的文件,即未跟踪文件
                List<String> Untrackfiles = Repository.UntrackItems(addStage,rmStage);
            //切换到这的分支需要检查，切换的分支是否有与未跟踪分支有相同文件
                //切换分支的commit
            Commit switchBranch = strNamePointXFindCommit(branchName);
                TreeMap<String,String> switchBranchContent = switchBranch.commitFiles();
                List<String> sameFiles = new ArrayList<>();
                for(Map.Entry<String,String> switchFile:switchBranchContent.entrySet()){
                    if(Untrackfiles.contains(switchFile.getKey())){
                       sameFiles.add(switchFile.getKey());
                    }
                }
                if(!sameFiles.isEmpty()){
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
                File pointHEAD = Utils.join(Repository.GITLET_DIR,"HEAD");
                Utils.writeContents(pointHEAD,branchName);
                File HEADCommitfile = findBranchCommitFile();
                //更新snapShot区
            //旧的
                TreeMap<String,String> oldFiles = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
                Commit HEADSnapShotCommit = Utils.readObject(HEADCommitfile,Commit.class);
                TreeMap<String,String> HEADSnapShot = HEADSnapShotCommit.commitFiles();
                Utils.writeObject(Repository.SnapSHOTCACHE_path,HEADSnapShot);
                Repository.updateFiles(HEADSnapShot,oldFiles);
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
            System.out.println("File does not exist in that commit.");
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
        if(findedCommitName.size() >1 || findedCommitName.isEmpty()){
            System.out.println("No commit with that id exists.");
            return null;
        }

        return findedCommitName.get(0);

    }

    //打印commit的溯源log
    private static void printLog(Commit commitcontent){
            System.out.println("===");
            System.out.println("commit "+ commitcontent.SHA);
            if(!commitcontent.calParent2().equals("")){
                System.out.println("Merge :  "+ commitcontent.calParent1().substring(0, 7) + " " + commitcontent.calParent2().substring(0, 7));
            }
            System.out.println(commitcontent.calData());
            System.out.println(commitcontent.calMessege());
            System.out.println();
            System.out.println();
            if(!commitcontent.calParent1().equals("")){
                Commit nextCommit = Utils.readObject(Utils.join(Repository.COMMIT_path,commitcontent.calParent1()),Commit.class);
                Repository.printLog(nextCommit);
            }
    }



    //查看commit的溯源log,主要方便自己,查看从一个commit的溯源记录
    public static void commitLog(String commitId){
            //这个是为了我以后专门查用的
        commitId = Repository.findFullCommitId(commitId);
        File commitFile = Utils.join(Repository.COMMIT_path,commitId);
        Commit  originalCommit = Utils.readObject(commitFile,Commit.class);
        Repository.printLog(originalCommit);
    }

    //查看当前分支commit的溯源log
    public static void HEADlog(){
        //HEADd的commit的id
       File  commitIdFile = Repository.findBranch();
       String commitId = Utils.readContentsAsString(commitIdFile);
       Repository.commitLog(commitId);

    }

    private static void printBranch(List<String> branchsName){
            System.out.println("=== Branches ===");
            System.out.print("*");
            for(String branchName:branchsName){
                System.out.println(branchName);
            }
    }

    //查看分支
    private static void viewedBranch(){
            List<String> branchsName = new ArrayList<>(Utils.plainFilenamesIn(Utils.join(Repository.GITLET_DIR,"refs","heads")));
            File HEADPath = Utils.join(Repository. GITLET_DIR.getPath(),"HEAD");
            String strHEAD = Utils.readContentsAsString(HEADPath);//读取是那个个分支
            branchsName.remove(strHEAD);
            branchsName.add(0,strHEAD);
            //打印分支
            printBranch(branchsName);
            System.out.println();System.out.println();
    }

    private static void vieweaddStage(TreeMap<String,String> addStage){
           System.out.println("=== Staged Files ===");
           for(String addName:addStage.keySet()){
               System.out.println(addName);
           }
        System.out.println();System.out.println();
    }

    private static void viewedRmStage(TreeMap<String ,String> rmStage){
            System.out.println("=== Removed Files ===");
            for(String rmStageName:rmStage.keySet()){
                System.out.println(rmStageName);
            }
            System.out.println();System.out.println();
    }
    //寻找该目录下的文件
    //不存在，且不在rm区即deleted
    // 存在，SHA值对比——》modified


    private static void printInformationREcored(TreeMap<String,String> informations){
            for (Map.Entry<String,String> informtion:informations.entrySet()){
                System.out.println(informtion.getKey() + " (" + informtion.getValue() + ")");
            }
        System.out.println();System.out.println();
    }

//返回跟踪修改但未暂存的对象TreeMap,如果add区与rm区未空，就只会返回跟踪但修改未提交的
    private static TreeMap<String,String> ModificationItems(TreeMap<String,String> addStage,TreeMap<String,String> rmStage){
        TreeMap<String,String> informations = new TreeMap<>();
        //暂存区add,即动过 且没脱离跟踪
        for(Map.Entry<String,String> addEntry:addStage.entrySet()){
            File addFile = Utils.join(Repository.CWD,addEntry.getKey());
            //文件不在&& rmStage.containsKey(addEntry.getKey())
            if(!addFile.exists() ){
                informations.put(addEntry.getKey(),"deleted");
            }
            //文件在
            else  {
                byte[]  contence = Utils.readContents(addFile);
                String fileSHA = Utils.sha1(contence);
                if(!addEntry.getValue().equals(fileSHA)){
                    informations.put(addEntry.getKey(),"modififed");
                }
            }

        }
        //被跟踪，理应未被修改的
        TreeMap<String,String> snapShotCache = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        //除去跟add踪修改的与被除去跟踪rm//修改
        for(String key:addStage.keySet()){
            snapShotCache.remove(key);
        }
        for(String key:rmStage.keySet()){
            snapShotCache.remove(key);
        }
        for(Map.Entry<String,String> entry:snapShotCache.entrySet()){
            //如果工作目录不存在 → 记录 "deleted"
            //如果工作目录存在且 SHA 与 snapShotCache 中不同 → 记录 "modified"
            File file = Utils.join(Repository.CWD,entry.getKey());
            if(!file.exists()) informations.put(entry.getKey(),"deleted");
            else {
                byte [] contentence = Utils.readContents(file);
                String fileHSA = Utils.sha1(contentence);
                if(!entry.getValue().equals(fileHSA)){
                    informations.put(entry.getKey(),"modififed");
                }
            }
        }
        return informations;
    }
    /**
     * 对哦，rm要么使文件脱离跟踪，要脱离跟踪与被删，压根不用考虑rm
     * rmStage（即暂存删除区）中的文件，不需要在“修改但未暂存”中考虑。因为用户已经明确要删除这些文件，无论工作目录中是否存在同名文件，都不应再报告为“修改未暂存”
     * 。如果工作目录中又重新创建了该文件，那它属于“未跟踪文件”的范畴（额外学分），而不是“修改未暂存”。*/
    private static void ModificationNotStagedForCommit(TreeMap<String,String> addStage,TreeMap<String,String> rmStage){
      TreeMap<String,String> informations = ModificationItems(addStage,rmStage);
        //打印informationrecord
        System.out.println("=== Modifications Not Staged For Commit ===");
        printInformationREcored(informations);
    }

    private static void printUntracked(List<String> files){
        for(String fileName:files){
            System.out.println(fileName);
        }
        System.out.println();System.out.println();
    }
 //返回未跟踪的对象List
    private static List<String> UntrackItems(TreeMap<String,String> addStage, TreeMap<String,String> rmStage){
        List<String> filenames = new ArrayList<>(Utils.plainFilenamesIn(Repository.CWD));// Utils.plainFilenamesIn(Repository.CWD) 只会返回普通文件，而 .gitlet 是一个目录，所以它不会出现在返回的列表中。
        List<String> staticFileName = Utils.plainFilenamesIn(Repository.CWD);
        for(String addFile:addStage.keySet()){
            filenames.remove(addFile);
        }



        // 移除当前 commit 中跟踪的文件（snapshotCache）
        TreeMap<String, String> snapShotCache = Utils.readObject(Repository.SnapSHOTCACHE_path, TreeMap.class);
        for (String trackedFile : snapShotCache.keySet()) {
            filenames.remove(trackedFile);
        }
        //一个文件在rm区，说明脱离跟踪且被删除，那么此时我在新建同名的文件，那理应是未跟踪
        for(String rmfile:rmStage.keySet()){
            if(staticFileName.contains(rmfile)){
                filenames.add(rmfile);
            }
        }


        return filenames;
    }

//未被跟踪
    private static void unTracked(TreeMap<String,String> addStage, TreeMap<String,String> rmStage){
       List<String> filenames = UntrackItems(addStage,rmStage);
        //打印
        System.out.println("=== Untracked Files ===");
        printUntracked(filenames);
    }
    //查看当前状态,在我设计里在snapShot区与add区即被跟踪
    public static void status(){
         //先把必要的TreeMap列出来与不要创建该文件夹的目录/blob的TreeMap很多要用，而是查的时候在计算与查询
        TreeMap<String,String> addStage = Utils.readObject(Repository.ADD_path,TreeMap.class);
        TreeMap<String,String> rmStage = Utils.readObject(Repository.RM_path,TreeMap.class);
        //查看分支
        Repository.viewedBranch();
        //已暂存的文件（add 过但未提交）
        Repository.vieweaddStage(addStage);
        //已标记删除的文件（rm 过但未提交）
        Repository.viewedRmStage(rmStage);
        //修改但未暂存的文件,这个跟踪
        Repository.ModificationNotStagedForCommit(addStage,rmStage);
        //未被跟踪的文件，既不在add区也不在snapSHotCAche区，rm(使用rm要么使文件脱离跟踪，如果在rm区脱离跟踪与被删，压根不用考虑rm),只用查键
        Repository.unTracked(addStage,rmStage);


    }

    public static void BranchName(String newbranch){
        List<String> points = Utils.plainFilenamesIn(Utils.join(Repository.GITLET_DIR,"refs","heads"));
        if(points.contains(newbranch)){
            System.out.println("A branch with that name already exists.");
            return;
        }
        File HEADPoint = Repository.findBranch();
        String HEADCommitIdHAS = Utils.readContentsAsString(HEADPoint);
        File newBuildBranch = Utils.join(Repository.GITLET_DIR,"refs","heads",newbranch);
        Utils.writeContents(newBuildBranch,HEADCommitIdHAS);

    }

    public static void rmBranch(String branchName){
        List<String> branchpointS = Utils.plainFilenamesIn(Utils.join(Repository.GITLET_DIR,"refs","heads"));
        if(!branchpointS.contains(branchName)){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        File HEADfile = Utils.join(Repository.GITLET_DIR,"HEAD");
        String HEADName = Utils.readContentsAsString(HEADfile);
        if(HEADName.equals(branchName)){
            System.out.println("Cannot remove the current branch.");
            return;
        }

        File branchFile = Utils.join(Repository.GITLET_DIR,"refs","heads",branchName);
        branchFile.delete();
    }

    public static void viewedGlobalLog(){
        File globCommitFile = Repository.COMMIT_path;
        List<String> globCommitions = Utils.plainFilenamesIn(globCommitFile);
        for (String CommitId:globCommitions){
            Commit commitcontent = Utils.readObject(Utils.join(Repository.COMMIT_path,CommitId),Commit.class);
            System.out.println("===");
            System.out.println("commit "+ commitcontent.SHA);
            if(!commitcontent.calParent2().equals("")){
                System.out.println("Merge :  "+ commitcontent.calParent1().substring(0, 7) + " " + commitcontent.calParent2().substring(0, 7));
            }
            System.out.println(commitcontent.calData());
            System.out.println(commitcontent.calMessege());
            System.out.println();
            System.out.println();
        }

    }


    public static void findCommitMessege(String commitMessege){
        List<String> GlobalCommitIds = Utils.plainFilenamesIn(Repository.COMMIT_path);
        int i = 0;
        for (String commitId:GlobalCommitIds){
            Commit commit = Utils.readObject(Utils.join(Repository.COMMIT_path,commitId),Commit.class);
            if(commit.calMessege().equals(commitMessege)){
                System.out.println(commit.SHA);
                i++;
            }
        }
        if(i == 0){
            System.out.println("Found no commit with that message.");
        }
    }

    //输入commitId,将snapShot区将加载它的TreeMap
    private static void updataSnapShotCache(String commitID){
        Commit commitContent = Utils.readObject(Utils.join(Repository.COMMIT_path,commitID),Commit.class);
        TreeMap<String,String> files = commitContent.commitFiles();
        Utils.writeObject(Repository.SnapSHOTCACHE_path,files);
    }

    public static void reset(String commitID){
        commitID = findFullCommitId(commitID);
        if (commitID == null) return;
        TreeMap<String ,String> addStage = Utils.readObject(Repository.ADD_path,TreeMap.class);
        TreeMap<String,String> rmStage = Utils.readObject(Repository.RM_path,TreeMap.class);
        if(addStage != null && !addStage.isEmpty() || rmStage != null && !rmStage.isEmpty()){
            System.out.println("the content in the stage has not committed.");
            return;
        }
        TreeMap<String,String> files = Repository.ModificationItems(addStage,rmStage);
        if( !files.isEmpty()){
            System.out.println("\"There is an untracked file in the way; delete it, or add and commit it first.\"");
            return;

        }
        //去除跟踪的文件,即未跟踪文件
        List<String> Untrackfiles = Repository.UntrackItems(addStage,rmStage);
        //切换到这的分支需要检查，切换的分支是否有与未跟踪分支有相同文件
        //切换分支的commit
        Commit switchBranch = Utils.readObject(Utils.join(Repository.COMMIT_path, commitID),Commit.class);
        TreeMap<String,String> switchBranchContent = switchBranch.commitFiles();
        List<String> sameFiles = new ArrayList<>();
        for(Map.Entry<String,String> switchFile:switchBranchContent.entrySet()){
            if(Untrackfiles.contains(switchFile.getKey())){
                sameFiles.add(switchFile.getKey());
            }
        }
        if(!sameFiles.isEmpty()){
            System.out.println("切换的分支与该分支中未跟踪的文件相同");
            for(String sameFile:sameFiles){
                System.out.println(sameFile+" ");
            }
            return;
        }

        Utils.writeContents(Repository.findBranch(),commitID);
        File HEADCommitfile = findBranchCommitFile();
        //更新snapShot区
        //旧的
        TreeMap<String,String> oldFiles = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        Commit HEADSnapShotCommit = Utils.readObject(HEADCommitfile,Commit.class);
        TreeMap<String,String> HEADSnapShot = HEADSnapShotCommit.commitFiles();
        Utils.writeObject(Repository.SnapSHOTCACHE_path,HEADSnapShot);
        Repository.updateFiles(HEADSnapShot,oldFiles);
    }
    private static List<String> commitSHALog(Commit commitContent,List<String> commitsSHA){
        commitsSHA.add(commitContent.SHA);
        if(commitContent.calParent1().equals("")){
            return commitsSHA;
        }

        commitContent = Utils.readObject(Utils.join(Repository.COMMIT_path,commitContent.calParent1()),Commit.class);

        return commitSHALog(commitContent,commitsSHA);
    }
    //遍历merge查父
    private static Commit mergeFather(List<String> currentCommitLog ,Commit mergecommit){
        if(currentCommitLog.contains(mergecommit.SHA))return mergecommit;
        mergecommit = Utils.readObject(Utils.join(Repository.COMMIT_path,mergecommit.calParent1()),Commit.class);
        return mergeFather(currentCommitLog,mergecommit);
    }
    private static Commit fatherCommit(Commit nowHEAD,Commit mergedBranch){
        List<String> curremtCommitLog = new ArrayList<>();
       List<String> currentLogHAS = Repository.commitSHALog(nowHEAD,curremtCommitLog);
       return Repository.mergeFather(curremtCommitLog,mergedBranch);

    }

    //先对比Commit,但凡spilit与其中一个相同，更新snap区  返回true，结束
    private static boolean compareCommit(Commit nowCommit,Commit mergeCommit,Commit spilit){
        String currCommitId =nowCommit.SHA;
        String  givenCommitId = mergeCommit.SHA;
        String spilitCommitId =spilit.SHA;
        if(spilitCommitId.equals(currCommitId) && !givenCommitId.equals(spilitCommitId)){
            System.out.println("Current branch fast-forwarded.");
            Utils.writeObject(Repository.SnapSHOTCACHE_path,mergeCommit.commitFiles());
            //生成merge的文件在目录
            TreeMap<String,String> givenFiles = mergeCommit.commitFiles();

            Utils.writeContents(Repository.findBranch(),mergeCommit.SHA);
            Repository.updateFiles(givenFiles,nowCommit.commitFiles());
            return true;
        }
        if (spilitCommitId.equals(givenCommitId) && !spilitCommitId.equals(currCommitId)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return true;
        }
        if(spilitCommitId.equals(currCommitId) && spilitCommitId.equals(givenCommitId)){
            return true;
        }
        if(currCommitId.equals(givenCommitId))return true;
        return false;
    }
    //处理curr!=given!=split
        private static String conflicFile(String currBolbFileId,String givenBlobFileId,String Filename){
            String currBlobContent = (currBolbFileId.equals(""))?"":Utils.readContentsAsString(Utils.join(Repository.BLOB_path,currBolbFileId));
            String givebBlobContent = (givenBlobFileId.equals(""))?"":Utils.readContentsAsString(Utils.join(Repository.BLOB_path,givenBlobFileId));
            String updateContent = "<<<<<<< HEAD\n" + currBlobContent + "\n=======\n" + givebBlobContent + "\n>>>>>>>\n";
            //不因直接修该，
           // Utils.writeContents(Utils.join(Repository.CWD,Filename),updateContent);
            //加到暂存区add,与生成blob值
            String blobHSA = Utils.sha1(updateContent);
            Utils.writeContents(Utils.join(Repository.BLOB_path,blobHSA),updateContent);
            return blobHSA;

        }
    //处理curr != split && given != split && curr == given 即curr == given !=split
    private static boolean ProcessCurrEqualGivenUnequalSplit(String currContent,String giveContent,String splitContent){
        if(currContent.equals("")) return false;
        return true;
    }

    //处理curr != split && given == split 即curr != given == split
    private static boolean ProcessCurrUnequalSplitEqualGiven(String currFile,String giveFile,String splitFile){
        if(currFile.equals("")) return false;
        return true;
    }
//处理curr == split && given != split 即given != curr == split
    private static boolean ProceesGiveUnequalSplitEquualcuure(String currentFileContent ,String givenFileContent ,String splitFileContent){
        if(givenFileContent.equals("")){
            return false;
        }
        return true;
    }
    //处理 curr == given == split 太简单了，方式不写了
    private static void proceesCommitFiles(TreeMap<String,String> currFiles,TreeMap<String,String> givenFiles,TreeMap<String,String> splitFiles){
        currFiles = new TreeMap<>(currFiles);
        givenFiles = new TreeMap<>(givenFiles);
        splitFiles = new TreeMap<>(splitFiles);
        //创建新的空位之后snap区add区，rm区更新做准备
        TreeMap<String,String> newSnapShot = new TreeMap<>();
        TreeMap<String,String> newAddStage = new TreeMap<>();
        TreeMap<String,String> newRmStage = new TreeMap<>();
        //被删文件，每个for循环后清空
        List<String> ProcessedDocuments = new ArrayList<>();
        boolean conflict = false;
        /**String cuurntFileContent;
        String givenFileContent;
        String splitFileContent;*/
        //文件会出现冲突,与没有冲突，没冲突的直接加到新snanp上
        //先spilit开始
         for(Map.Entry<String,String> splitfile:splitFiles.entrySet()){
             //在遍历spilit的文件时对于split的文件只有修改与没被修改，删除了也是一种修改，1：cure的删除，given存在看given的与spilit同不同，不同是冲突，同保持删除状态；given删除同理，不过同是删除，并添加到rm区  2 如果cur的文件或given的文件与soilit相同，而另个不同，则存不同的 3，两个文件存在，都不同，冲突
           //本质只有cuurent?given?split
             String splitfileName = splitfile.getKey();
             String splitFileContent = splitfile.getValue();
             String currentFileContent = (currFiles.get(splitfileName) == null)?"":currFiles.get(splitfileName);
             String givenFileContent = (givenFiles.get(splitfileName) == null)?"":givenFiles.get(splitfileName);
             boolean confire;
             if(currentFileContent.equals(splitFileContent) && givenFileContent.equals(splitFileContent)){
                 newSnapShot.put(splitfileName,splitFileContent);
                 ProcessedDocuments.add(splitfileName);
                 continue;
             }
             if(currentFileContent.equals(splitFileContent) && !givenFileContent.equals(splitFileContent)){
                 //true是加到add,false是rm
                  boolean fileStage = Repository.ProceesGiveUnequalSplitEquualcuure(currentFileContent,givenFileContent,splitFileContent);
                 //不要返回删除这文件，最后所有文件处理后由newsnaap处理加载
                 if(fileStage){
                     newAddStage.put(splitfileName,givenFileContent);
                     ProcessedDocuments.add(splitfileName);
                     continue;
                 }else{
                     newRmStage.put(splitfileName,givenFileContent);
                     ProcessedDocuments.add(splitfileName);
                     continue;
                 }
             }
             //true是存在
             if(!currentFileContent.equals(splitFileContent) && givenFileContent.equals(splitFileContent)){
                 boolean fileSnap = Repository.ProcessCurrUnequalSplitEqualGiven(currentFileContent,givenFileContent,splitFileContent);
                 if(fileSnap){
                     newSnapShot.put(splitfileName,currentFileContent);
                     ProcessedDocuments.add(splitfileName);
                     continue;
                 }else{//这只能说明文件是不在的，无法加到暂存区
                     ProcessedDocuments.add(splitfileName);
                     continue;
                 }

             }
             if(!currentFileContent.equals(splitFileContent) && !givenFileContent.equals(splitFileContent) && currentFileContent.equals(givenFileContent)){
                 boolean fileSnap = Repository.ProcessCurrEqualGivenUnequalSplit(currentFileContent,givenFileContent,splitFileContent);
                 if(fileSnap){
                     newSnapShot.put(splitfileName,currentFileContent);
                     ProcessedDocuments.add(splitfileName);
                     continue;

                 }else {
                     ProcessedDocuments.add(splitfileName);
                     continue;
                 }
             }
            if(!currentFileContent.equals(splitFileContent) && !givenFileContent.equals(splitFileContent) && !currentFileContent.equals(givenFileContent)){
                //即三者都不同
                String newaddStageBlob = Repository.conflicFile(currentFileContent,givenFileContent,splitfileName);
                conflict = true;
                //加到暂存add区
                newAddStage.put(splitfileName,newaddStageBlob);
                ProcessedDocuments.add(splitfileName);

            }

         }
//每次一个循环结束去除ProcessedDocuments的文件并清空
        for(String file:ProcessedDocuments){
            currFiles.remove(file);givenFiles.remove(file);splitFiles.remove(file);
        }
        ProcessedDocuments = new ArrayList<>();
        for(Map.Entry<String,String> currFile:currFiles.entrySet()){
            //且curr一定存在，否则怎么遍历
            //只有curr==given!=split或curr != given != split的情况
            String currentFileName = currFile.getKey();
            String currFileContent = currFile.getValue();
            String splitFileContent = null;
            String givenFileContent = (givenFiles.get(currentFileName) == null)?"":givenFiles.get(currentFileName);
            if(currFileContent.equals(givenFileContent)){
               //curr一定存在
                newSnapShot.put(currentFileName,currFileContent);
                ProcessedDocuments.add(currentFileName);

            }
            else {
                //given是null，而split也是null,直接加入newSnap
                if(givenFileContent == null){
                    newSnapShot.put(currentFileName,currFileContent);

                }//不相同还存在，冲突
               else {
                   String fileSHA = Repository.conflicFile(currFileContent,givenFileContent,currentFileName);
                   conflict =true;
                   newAddStage.put(currentFileName,fileSHA);
                   ProcessedDocuments.add(currentFileName);

                }

            }
        }
        for(String file:ProcessedDocuments){
            currFiles.remove(file);givenFiles.remove(file);//Split不用去了
        }
        ProcessedDocuments = new ArrayList<>();
        //given只剩下了curr == split != given
        //yinc直接加到暂存区，存在则add区，
        for(Map.Entry<String,String> givenFile:givenFiles.entrySet()){
            newAddStage.put(givenFile.getKey(),givenFile.getValue());
        }
        
        //ok,所有遍历结束
        //处理new
        //加载到暂存区
        Utils.writeObject(Repository.SnapSHOTCACHE_path,newSnapShot);
        Utils.writeObject(Repository.RM_path,newRmStage);
        Utils.writeObject(Repository.ADD_path,newAddStage);
        if(conflict) System.out.println("Encountered a merge conflict.");
    }





    public static void merge(String mergeBranch){
        //检查该被合并分支是否存在
        List<String> points = Utils.plainFilenamesIn(Utils.join(Repository.GITLET_DIR,"refs","heads"));
        if(!points.contains(mergeBranch)){
            System.out.println("该被合并的分支不存在");
            return;
        }
        //若暂存区add与rm区还有东西，退出
        TreeMap<String,String> addStage =Utils.readObject(Repository.ADD_path,TreeMap.class);
        TreeMap<String,String> rmStage = Utils.readObject(Repository.RM_path,TreeMap.class);
        if(!addStage.isEmpty() || !rmStage.isEmpty()){
            System.out.println("stage has files");
            return;
        }
        //无论怎么样都要找到共同的父节点
        Commit nowHEAD = Utils.readObject(Repository.findBranchCommitFile(),Commit.class);
        Commit mergedCommitBranch = Repository.strNamePointXFindCommit(mergeBranch);
        //返回共同的父节点Commit提交
        Commit spilit = fatherCommit(nowHEAD,mergedCommitBranch);
        //snap暂存区不处理先，有下面更新处理；

        //先对比Commit,但凡spilit与其中一个相同，且另个不同，更新snap区 或两个都相同  返回true，结束
        if(Repository.compareCommit(nowHEAD,mergedCommitBranch,spilit)){
            System.out.println("单纯commit更新");
            return;
        }
        //上面没返回，说明3个commit不同，那么每个文件都要查
        TreeMap<String,String> currentFiles = new TreeMap<>(nowHEAD.commitFiles());
        TreeMap<String,String> givenFiles = new TreeMap<>(mergedCommitBranch.commitFiles());
        Repository.proceesCommitFiles(currentFiles,givenFiles,spilit.commitFiles());
        String currentBranch = Utils.readContentsAsString(Utils.join(GITLET_DIR, "HEAD")).trim();
        String mergeMessage = "Merged " + mergeBranch + " into " + currentBranch + ".";
        commitFile(mergeMessage, nowHEAD.SHA, mergedCommitBranch.SHA);
        //打印commit即snap的文件
        //写个读取snap区的信息将blob文件加载到当前目录，reset与checkout [branch]也需要，不对合并已经写了，不对冲突只是应该加到blob文件，不因直接修改原文件，在commit在根据snap修改
        //根据更新后的snap区跟新目录文件
        TreeMap<String,String> updateSnap = Utils.readObject(Repository.SnapSHOTCACHE_path,TreeMap.class);
        //旧就是现在的未更新前snap
        Repository.updateFiles(updateSnap,currentFiles);
    }
    private static void updateFiles(TreeMap<String,String> newfiles,TreeMap<String,String> oldFiles){
//先获取当前工作目录中的所有文件（或根据旧快照）。
//
//对比新快照，如果某个文件只存在于工作目录而新快照中没有，则删除它
        for(Map.Entry<String,String> newFile:newfiles.entrySet()){
            Utils.writeContents(Utils.join(Repository.CWD,newFile.getKey()),Utils.readContents(Utils.join(Repository.BLOB_path,newFile.getValue())));
            oldFiles.remove(newFile.getKey());
        }
        for (Map.Entry<String,String> oldFile:oldFiles.entrySet()){
            Utils.restrictedDelete(Utils.join(Repository.CWD,oldFile.getKey()));
        }
    }


    //通过分支的名字找到它的commit，返回commit
    private static Commit strNamePointXFindCommit(String mergename){
        String mergeCommitId = Utils.readContentsAsString(Utils.join(Repository.GITLET_DIR,"refs","heads",mergename));
        return Utils.readObject(Utils.join(Repository.COMMIT_path,mergeCommitId),Commit.class);

    }


//第二种的commitfile，主要之前的那个不能乱动
public static void commitFile(String message,String newParent1,String newParent2){
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
    Commit submit = new Commit(message,newParent1,newParent2,snapShotCAche);
    Utils.writeObject(Utils.join(Repository.COMMIT_path,submit.SHA),submit);
    // 更新当前分支指针
    //HEAD是分支映射，还在这分支，我们也没有新建分支.gitlet/refs/heads/没添加内容，修改.gitlet/refs/heads/master里master内容即可
    Utils.writeContents(Repository.findBranch(),submit.SHA);
    //清理暂存区的add区与rm区,snapshotCache 通常不需要清空，因为它应始终反映下一次提交的完整快照（在 commit 后应更新为新 commit 的 files，而不是清空）
    TreeMap<String, String> empty = new TreeMap<>();
    Utils.writeObject(Repository.ADD_path,empty);
    Utils.writeObject(Repository.RM_path,empty);

}




}
