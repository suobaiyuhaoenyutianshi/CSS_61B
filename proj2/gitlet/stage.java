package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

public class stage {
    public static void main(String[] args) {
        if (args.length == 0) return;
        File stageFile = new File(args[0]);

        if (!stageFile.exists()) {
          System.out.println("没有目录i");
          return;
        }
            System.out.println("---");
        TreeMap<String,String> obj = Utils.readObject(stageFile,
                TreeMap.class);
    }
}
