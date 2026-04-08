package gitlet;

/** Gitlet 的驱动程序类，Gitlet 是 Git 版本控制系统的一个子集。
 *  @author TODO
 */

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
            case "add":
                // TODO: 处理 `add [文件名]` 命令
                break;
            // TODO: 填写其余部分
        }
    }
}
