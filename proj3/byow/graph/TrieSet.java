package byow.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

////总结，这是我能想到比较合适的：一个接点包含下个节点的信息;父节点只用null来说明存在信息，本身信息在自己的节点中
public class TrieSet {
    private class Node{
        private boolean isKey;//取出的问题
        private HashMap<Character,Node> children;
        public Node(){
            isKey = false;
            children = new HashMap<>();
        }
    }
    private Node root;
    public TrieSet(){
        root = new Node();//始祖
    }
//注跟c语言一样，字母与数字是相同的
    public void add(String word){
        Node curr = this.root;
        for(int i =0;i < word.length();i++){
            char letter = word.charAt(i);
             curr.children.putIfAbsent(letter,new Node());//不存在就建，存在就不管
             curr = curr.children.get(letter);//切换节点
        }
        curr.isKey = true;//该节点
    }
    //返回字符串对应的Node
    private Node getNode(String word){
        Node curr = this.root;
        for(int i =0;i < word.length();i++){
            char letter = word.charAt(i);

            curr = curr.children.get(letter);        //// 进入下一层,即现在的字符串自身节点
            if(curr== null) return null;
        }
        return curr;
    }
    public boolean contains(String word){
        Node correspondNode = getNode(word);
        return correspondNode != null && correspondNode.isKey;
    }


    //单词收集
    public List<String> collect(){
        Node curr = this.root;
        List<String> result = new ArrayList<>();
        //负责收集的,从根节点的每个子节点开始递归收集
        for (char c:curr.children.keySet()){
            colHelp(String.valueOf(c),result,curr.children.get(c));
        }
        return result;
    }
    //字符串自身节点
    private List<String> colHelp(String curentWord,List<String> result,Node n ){
        if(n.isKey) result.add(curentWord);
        for (char c:n.children.keySet()){
            colHelp(curentWord + c,result,n.children.get(c));
        }
        return result;
    }
    /**
     * 收集所有以指定前缀开头的单词。
     * 思路：先定位到前缀对应的节点，然后以该节点为根，调用同样的收集逻辑。
     */
    public List<String> keysWithPrefix(String prefix){
        Node prexNode = getNode(prefix);
        if(prexNode == null) return null;
        List<String> result = new ArrayList<>();
        colHelp(prefix,result,prexNode);
        return result;
    }


    static void main(String[] args){
        TrieSet test = new TrieSet();
        test.add("a");
        test.add("b");
        test.add("abc");
        test.add("ab");
        test.add("abxs");
        List<String> i = test.collect();
        List<String> j = test.keysWithPrefix("ab");
    }
}
//