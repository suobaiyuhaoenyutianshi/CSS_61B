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




        }
    }
}

