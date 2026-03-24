package deque;
import java.util.Iterator;



import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Iterable<T> {

    // 双向节点
    private static class Node<T> {
        T item;
        Node<T> prev;
        Node<T> next;

        Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<T> sentinel;   // 哨兵节点，不存数据
    private int size;

    /**
     * 空 deque 构造：哨兵自循环
     */
    public LinkedListDeque() {
        sentinel = new Node<>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    // ========== 基础 API ==========

    public void addFirst(T item) {
        Node<T> newNode = new Node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    public void addLast(T item) {
        Node<T> newNode = new Node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node<T> cur = sentinel.next;
        while (cur != sentinel) {
            System.out.print(cur.item + " ");
            cur = cur.next;
        }
        System.out.println();
    }
    @Override
    public String toString(){
        String[] StrLink = new String[this.size];
        Node<T> curr = sentinel.next;
        for(int i = 0;i < this.size;i++){
                StrLink[i] = curr.item.toString();
                curr = curr.next;
        }
        return "[" + String.join(",",StrLink) + "]";

    }
    public T removeFirst() {
        if (isEmpty()) return null;
        Node<T> first = sentinel.next;
        sentinel.next = first.next;
        first.next.prev = sentinel;
        size--;
        T item = first.item;
        // 帮助垃圾回收
        first.prev = null;
        first.next = null;
        return item;
    }

    public T removeLast() {
        if (isEmpty()) return null;
        Node<T> last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        size--;
        T item = last.item;
        last.prev = null;
        last.next = null;
        return item;
    }



    // 递归版本 get
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node<T> node, int index) {
        if (index == 0) return node.item;
        return getRecursiveHelper(node.next, index - 1);
    }



    //迭代器
    @Override
    public Iterator<T> iterator(){
        return new Iterator<>(){
            private Node<T> curr = sentinel.next;
            @Override
            public boolean hasNext(){
                return curr != sentinel;
            }
            @Override
            public T next(){
                    if (!hasNext()) throw new NoSuchElementException();
                    T item = curr.item;
                    curr = curr.next;
                    return item;
            }


        };

    }
    @Override
    public boolean equals(Object other){
        if (this == other)return true;
        if (other == null)return false;
        if (!(other instanceof LinkedListDeque)) return false;
        LinkedListDeque<?> Other = (LinkedListDeque<?>) other;
        Node<?> currOther = Other.sentinel.next;
        Node<?> currThis = this.sentinel.next;
        if(Other.size != this.size) return false;
        int i = 0;
        while (i<this.size){
            if (currThis.item != currOther.item)return false;
            currOther = currOther.next;
            currThis = currThis.next;
        }

        return true;
    }

    public static void main(String[] args){
        LinkedListDeque<Integer> test = new LinkedListDeque<>();
        test.addLast(1);
        test.addLast(2);
        test.addLast(3);
    }



}