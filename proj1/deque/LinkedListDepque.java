package deque;
import java.util.Iterator;
import java.util.List;

public class LinkedListDepque<T>{
    //内部节点
    private static class Node<T>{
        T item;
        Node prev;
        Node next;

        public Node(T item,Node prev,Node next){
                this.item = item;
                this.prev = prev;
                this.next = next;
        }
    }

    //哨兵
    private Node<T> sentine;
    private int size;
    //构造
    public LinkedListDepque(){
        sentine = new Node<>(null,null,null);
        sentine.prev = sentine;
        sentine.next = sentine;
        this.size = 0;
    }
    //addLast
    public void addLast(T x){
        Node<T> newNode = new Node<>(x,sentine.next,sentine);
        newNode.prev.next = newNode;
        this.sentine.next = newNode;
        if(this.size==0)this.sentine.prev = newNode;
        this.size++;
    }
    //addFirst
    public void addFirst(T x){
        Node<T> newNode = new Node<>(x,sentine,sentine.prev);
        newNode.next.prev = newNode;
        this.sentine.prev = newNode;
        if (this.size == 0)this.sentine.next = newNode;
        this.size++;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    //可以重新它的toString

    @Override
    public String toString(){
        String[] LinkPhase = new String[this.size];
        for(int i = 0;i < this.size;i++){
            LinkPhase[i] =  get(i).toString();
        }
        return "{" + String.join(",",LinkPhase) +"}";
    }
    public void printDeque(){
        System.out.println(this);
    }

   public T removeFirst(){
        if(isEmpty())return null;
        Node<T> First = sentine.prev;
        this.sentine.prev = First.next;
         First.next.prev = this.sentine;
         T item = First.item;
         First.next = null;
         First.prev = null;
         size--;
        return item;
    }
    public T removeLast(){
        if(isEmpty())return null;
        Node<T> last = sentine.next;
        Node<T> lastButOne = sentine.next.prev;
        this.sentine.next = lastButOne;
        lastButOne.next = this.sentine;
        T item = last.item;
        last.next = null;
        last.prev = null;
        size--;
        return item;
    }

    public T get(int index){
        if (index < 0 || index >= size) return null;
        Node<T> curr = this.sentine.prev;
        for (int i = 0;i < index;i++){
            curr = curr.next;
        }
        return curr.item;
    }

    public static void main(String[] args){
        LinkedListDepque<Integer> test = new LinkedListDepque<>();
      test.addLast(1);
       test.addLast(2);
       test.addLast(3);
        test.addLast(1);
        test.addFirst(2);
        test.addFirst(3);
        int x = test.removeFirst();
        System.out.println(test);
    }



}
