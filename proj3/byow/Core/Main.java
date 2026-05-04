package byow.Core;

/** 这是程序的主要入口点。此类仅解析命令行输入，并让 byow.Core.Engine 类在键盘模式或输入字符串模式下接管。*/
public class Main {
    public static void main(String[] args) {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
            //engine.interactWithInputString(args[1]);
            System.out.println(engine.toString());
            // 请勿更改这些行，先别动哈 ;)
        } else if (args.length == 2 && args[0].equals("-p")) { System.out.println("Coming soon."); }
        // 请勿更改这些行，先别动哈 ；)
        else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
    }
}
