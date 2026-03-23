package deque;
import java.util.Iterator;
public class LinkedListDepque<T>  implements Iterable{
//节点内部结构
    private static class Node<I>{
        I item;
        Node<I> next;
        public Node(I item, Node<I> next){
            this.item = item;
            this.next = next;
        }
    }

    //链表是私有变量
    private int size;//列表长度
    private Node<T> sentry;//哨兵
    private Node<T> tail;//跟着尾巴
    //创建空链表
    public LinkedListDepque(){
        this.size = 0;
        this.sentry = new Node<>(null,null);
        this.tail = null;
    }

//判空
    public boolean isEmpty(){
        if(size == 0)return true;
        return false;
    }

    //添加尾巴
    public void addLast(T x){
        if(this.tail == null){
            this.sentry.next = new Node<>(x,null);
            this.tail = this.sentry.next;
        }
        else{
            Node<T> newNode = new Node<>(x,null);
            this.tail.next = newNode;
            this.tail = newNode;
        }

        this.size++;
    }
    public void addFirst(T x){

        if(this.size == 0){
              addLast(x);
        }
        else{
            insert(x,0);
        }

    }
    //返回第一个元素
    public T getFirst(){
        return this.sentry.next.item;
    }
    //返回最后一个元素
    public T getLast(){
        return this.tail.item;
    }

    public void insert(T x , int position){
        if( position < 0 || position >= this.size){
            throw new IndexOutOfBoundsException("你的插入范围 0<=x<=thi.size");

        }
        //需要找到position的前个位置
        Node<T> prevItem = findNode(position - 1);
        //创建新节点
        Node<T> newNode = new Node<>(x,null);
        newNode.next = prevItem.next;
        prevItem.next = newNode;
        if(this.size == position){
            this.tail = newNode;
        }
        this.size++;

    }
    //T get(int index)：获取索引处元素（0为队首），空返回null，
    public T get(int x){
       if(this.size == 0)return null;
        return findNode(x).item;
    }

//内部辅助方式搜索索引为i的元素，甚至是哨兵
    private Node<T> findNode(int i){
        if(i < -1 || i >= this.size){
            throw new IndexOutOfBoundsException("无法搜索");
        }//-1是哨兵节点
        Node<T> curr = this.sentry;
        for(int x = -1 ; x < i; x++){
            curr = curr.next;
        }
        return curr;
    }
//T removeFirst() / T removeLast()：删除并返回首/尾元素，空时返回null
    public T removeFirst(){
        if(this.size == 0)return null;
        return remove(0);
    }
    public T removeLast(){
        if(this.size == 0)return null;
        return remove(this.size - 1);
    }




//返回列表长度
    public int size(){
        return this.size;
    }
//删除
    public T remove(int x){
        if (x < 0 || x >= this.size){
            throw new IndexOutOfBoundsException("删除越界");
        }
        Node<T> deleteNode = findNode(x);
        Node<T> prevNode = findNode(x - 1);
        prevNode.next = deleteNode.next;
        deleteNode.next = null;
        if (x == this.size - 1)this.tail = prevNode;
        this.size--;
        return deleteNode.item;
    }



    public static void main(String[] args){
        LinkedListDepque<Integer> test = new LinkedListDepque<>();
        test.addLast(3);
        test.addLast(4);
        test.addLast(6);
        test.addLast(8);
        test.insert(100,3);
        test.insert(120,0);
        test.insert(90,2);
        test.remove(6);
        test.remove(0);
        test.remove(2);
        test.addFirst(20000);
    }

}
