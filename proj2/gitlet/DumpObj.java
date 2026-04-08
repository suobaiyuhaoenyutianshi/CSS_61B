package gitlet;

import java.io.File;

/** 一个调试类，其主程序可以如下调用：
 *      java gitlet.DumpObj 文件...
 *  其中每个“文件”是由 Utils.writeObject 生成的文件（或任何包含序列化对象的文件）。
 *  这将会简单地读取文件，反序列化它，并对结果对象调用 dump 方法。
 *  该对象必须实现 gitlet.Dumpable 接口才能工作。例如，你可以像这样定义你的类：
 *
 *        import java.io.Serializable;
 *        import java.util.TreeMap;
 *        class MyClass implements Serializeable, Dumpable {
 *            ...
 *            @Override
 *            public void dump() {
 *               System.out.printf("size: %d%nmapping: %s%n", _size, _mapping);
 *            }
 *            ...
 *            int _size;
 *            TreeMap<String, String> _mapping = new TreeMap<>();
 *        }
 *
 *  如示例所示，你的 dump 方法应该打印来自你类对象的有用信息。
 *  @author P. N. Hilfinger
 */
public class DumpObj {

    /** 反序列化并对 FILES 中每个文件的内容应用 dump。 */
    public static void main(String... files) {
        for (String fileName : files) {
            Dumpable obj = Utils.readObject(new File(fileName),
                                            Dumpable.class);
            obj.dump();
            System.out.println("---");
        }
    }
}

