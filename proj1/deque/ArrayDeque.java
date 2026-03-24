package deque;
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
public class ArrayDeque<T> {
    private T[] items;
    private int size;
    public ArrayDeque(){
        items = (T[]) new Object[16];///////////////
        this.size =0;
    }
    public void addLast(T item){
        if(this.size == items.length){
            resize(this.size * 2);
        }
        items[this.size] = item;
        this.size++;
        shrinkCapacity();


    }

//数组扩展
    public void resize(int capacity){
        T[] newArr = (T[]) new Object[capacity];
        System.arraycopy(this.items,0,newArr,0,this.size);
        this.items = newArr;
    }


//️内存使用与元素数量成比例：当数组长度≥16时，使用率不得低于25%（低于则缩容）
  public void  shrinkCapacity(){
       if(this.items.length >= 16){
           if(this.size * 4 < this.items.length){
               T[] newCapacity = (T[]) new Object[this.size];
               System.arraycopy(this.items,0,newCapacity,0,this.size);
               this.items = newCapacity;
           }
       }

  }



    public static void main(String[] args){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addLast(1);
        test.addLast(2);
        test.addLast(3);
        test.addLast(4);
        test.addLast(5);
        test.addLast(6);
        test.addLast(7);
        test.addLast(8);
        test.addLast(9);
    }


}
