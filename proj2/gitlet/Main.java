package gitlet;

/** Gitlet 的驱动程序类，Gitlet 是 Git 版本控制系统的一个子集。
 *  @author TODO
 */

import com.sun.source.tree.Tree;

import java.io.File;
import java.util.TreeMap;

/** 用法：java gitlet.Main ARGS，其中 ARGS 包含
 *  <命令> <操作数1> <操作数2> ...
 */
public class Main {


    public static void main(String[] args) {
        // TODO: 如果 args 为空怎么办
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.setuppresisit();
                // TODO: 处理 `init` 命令
                break;
            case "add"://添加到暂存区
                // TODO: 处理 `add [文件名]` 命令
                //每次只加一个

                //然后在在该分支即commit里，如果SnapShotCache为空则copy里的TreeMap给SnapShotCach，不为空不copy
                //该方式给File 分支，它将该分支的TreeMap拷贝给SnapShotCache
              String FileName = args[1];

                Repository.add(FileName);
                break;
            // TODO: 填写其余部分
            case "rm":
                //基本与add没什么区别
                //每次只删除一个
                String rmFileName = args[1];
                Repository.rm(rmFileName);

                break;

            case "commit":
                //提交信息
                String message = args[1];
                Repository.commitFile(message);
                break;
            case "checkout"://签出
                //java gitlet.Main checkout -- [file name] → 从当前 HEAD 恢复文件
                //java gitlet.Main checkout [commit id] -- [file name] → 从指定 commit 恢复文件
                if(args.length == 3) {
                    String symbol1 = args[1];
                    String symbol2 = args[2];
                    Repository.checkFile(symbol2);
                    break;
                }
                else if(args.length == 4){
                    String symbol1 = args[1];//commit  id
                    String symbol2 = args[3];//指定恢复文件
                    Repository.checkCommitFilename(symbol1,symbol2);
                    break;
                }
                else{
                    String switchBranchName = args[1];
                    Repository.checkBranch(switchBranchName);
                }
                break;
            case "log":
                //查看当前分支的日志
                Repository.HEADlog();
                break;
            case "status":
                Repository.status();
                break;


            //创建新分支但不切换
            case "branch":
                String symbol = args[1];
                Repository.BranchName(symbol);
                break;

            //删除一个分支指针
            case "rm-branch":
                String branchname = args[1];
                Repository.rmBranch(branchname);
                break;
            case "global-log":
                Repository.viewedGlobalLog();
                break;
            case "find":
//打印所有具有指定消息的 commit 的 SHA-1（每行一个），若无则输出 `"Found no commit with that message."`
                String commitMessege = args[1];
                Repository.findCommitMessege(commitMessege);
                break;
            case "reset":
            String pointCommitId = args[1];
            Repository.reset(pointCommitId);
            break;
            case "merge":
                String mergeName = args[1];
                Repository.merge(mergeName);

        }





    }
}

