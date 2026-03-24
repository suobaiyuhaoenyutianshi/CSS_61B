package deque;

import java.util.Iterator;

/*核心规则：

        ⭐️初始数组大小为8

⭐️增删（除扩容外）为常数时间，get和size为常数时间

⭐️内存使用与元素数量成比例：当数组长度≥16时，使用率不得低于25%（低于则缩容）

必须使用循环数组（避免移动元素）

扩容/缩容需仔细处理边界
所有实现必须包含以下方法（⭐️不可改动签名）：

void addFirst(T item) / void addLast(T item)：添加元素（item永不为null）

boolean isEmpty() / int size()：判空与大小

void printDeque()：从前往后打印，空格分隔，最后换行

T removeFirst() / T removeLast()：删除并返回首/尾元素，空时返回null

T get(int index)：获取索引处元素（0为队首），空返回null，⭐️不得修改deque

额外要求：⭐️实现Iterator<T> iterator()（使Deque可迭代）和⭐️boolean equals(Object o)（内容顺序相同*/
public class ArrayDeque<T> implements Iterable<T>{
    private T[] items;
    private int size;
    private int last;//记录最后的
    private int first;
    public ArrayDeque(){
        items = (T[]) new Object[8];///////////////
        this.size =0;
        this.last = 0;//记录位置
        this.first = 0;
    }
    public void addLast(T item){
        if(this.size == items.length){
            resize(this.size * 2);
        }
        int curr = last +1;
        if(this.size==0)curr = 0;
        items[curr] = item;
        this.last = curr;
        this.size++;

    }
    public void addFirst(T item){
        if(this.size == items.length){
            resize(this.size * 2);
        }
        int curr = this.first -1;
        if(this.first == 0)curr = this.items.length -1;

        this.items[curr] = item;
        this.first = curr;
        this.size++;
    }
    //获得顺序的
    public T get(int i){
        if(i < 0 ||i >= this.size)throw new ArrayIndexOutOfBoundsException("越界");

        //加到前面的数量
        int startNum = this.size - (this.last +1);
        //顺序头

        if(i >(startNum - 1)){
            return items[i - startNum];
        }
        //若有插入前面
        else { //                        0
            //这是加开头的的零元素的一个位置| | |
            int startfrontFirstnum = this.items.length - startNum;
            return this.items[startfrontFirstnum + i];
        }

    }

    public T removeFirst(){
        if(isEmpty())return null;
        T curr = this.items[this.first];
        this.items[first] = null;
        if(this.first != this.items.length-1){
            this.first++;
        }
        else if (this.first == this.items.length-1){
       this.first = 0;
        }
        this.size--;
        shrinkCapacity();
        return curr;
    }

    public T removeLast(){
        if(isEmpty())return null;
        T curr = this.items[last];
        this.items[last] = null;
        this.last--;
        this.size--;
        shrinkCapacity();
        return curr;
    }

    //重组数组
    public void resize(int capacity){
        T[] newArr = (T[]) new Object[capacity];
        //要将last =this.size -1,first = 0
        for(int i = 0;i < this.size;i++){
            newArr[i] = get(i);
        }
        this.items = newArr;
        this.last = this.size - 1;
        this.first = 0;
    }


//️内存使用与元素数量成比例：当数组长度≥16时，使用率不得低于25%（低于则缩容）
  public void  shrinkCapacity(){
     int capacity = this.items.length;
     if( this.size*4 < capacity &&this.size >16){
         int newCapacity = capacity / 2;
         if(newCapacity < 8)newCapacity =8;
         resize(newCapacity);
     }

  }

    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
    @Override
    public String toString(){
        String[] strArr = new String[this.size];
        for(int i =0;i < this.size;i++){
            strArr[i] = get(i).toString();
        }
        return "[" + String.join(",",strArr) + "]";
    }
    @Override
    public Iterator<T> iterator(){
        return new Iterator<T>() {
            private int curr = 0;
            @Override
            public boolean hasNext() {
                return curr < size;
            }

            @Override
            public T next() {
               if(!hasNext()){
                   throw new ArrayIndexOutOfBoundsException();
               }
               T item = get(curr);
               curr++;
               return item;
            }
        };
    }

    // equals
    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof ArrayDeque))return false;
        if(o == this)return true;
        ArrayDeque<?> other =(ArrayDeque<?>) o;
        int i =0;
        while (i < this.size){
            if(other.get(i)!=this.get(i)){
                return false;
            }
            i++;
        }
     return true;
    }

    public static void main(String[] args){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        for(int i =0;i<10;i++)test.addLast(i);
        for(int i =0;i<10;i++)test.addFirst(i);
      //  test.resize(20);
        test.printDeque();
        System.out.println(test);
        test.removeFirst();
        test.removeFirst();
        test.removeFirst();
        test.removeFirst();
        test.removeFirst();
        test.removeFirst();
        test.removeLast();
    }


}
