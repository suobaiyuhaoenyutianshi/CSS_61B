package byow.graph;
//
import java.util.HashMap;

//
public class AutocompleteTrie {
    private static class Node{
        private double weight = Double.NEGATIVE_INFINITY;
        private double best = Double.NEGATIVE_INFINITY;
        private String word;
        private boolean isKey = false;
        private HashMap<Character,Node> children = new HashMap<>();
        public Node(String word){
            this.word = word;
        }

    }
    private Node root =new Node("");
    public void add(String word,double weight){
        root = add(word,weight,0,root);
    }
    private Node add(String word, double weight, int d, Node cur) {
        if (d == word.length()) {
            cur.isKey = true;
            cur.weight = weight;
            cur.best = Math.max(cur.best, weight);
            return cur;
        }
        char c = word.charAt(d);
        cur.children.putIfAbsent(c, new Node(word.substring(0, d + 1)));
        Node child = cur.children.get(c);
        add(word, weight, d + 1, child);

        cur.best = Math.max(cur.best, child.best);
        return cur;
    }
    public void insert(String word,double weight){
        this.add(word,weight);
    }
    public boolean search(String word){
        Node correspondNode = colHelp(word);
        return correspondNode != null && correspondNode.isKey;
    }
    //找到该单词对应节点，且返回，没有返回null
    private Node colHelp(String word){
        int j = word.length();
        Node curr = this.root;
        for(int i =0;i < j;i++){
            char c = word.charAt(i);
            curr = curr.children.get(c);//// 进入下一层,即现在的字符串自身节点
            if(curr == null) return null;

        }
        return curr;
    }




    static void main(String[] args){
        AutocompleteTrie test = new AutocompleteTrie();
        test.add("a",1);
        test.add("c",3);

        test.add("acc",4);
        test.add("ac",1);
        test.add("achs",6);
        boolean a = test.search("ach");
        a = test.search("asc");

    }





}
