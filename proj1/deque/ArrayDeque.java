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


import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int first;      // 队首元素下标
    private int last;       // 队尾元素下标

    private static final int INIT_CAPACITY = 8;

    public ArrayDeque() {
        items = (T[]) new Object[INIT_CAPACITY];
        size = 0;
        first = 0;
        last = 0;
    }

    // 添加元素到头部
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        // 向前移动 first，并取模
        first = (first - 1 + items.length) % items.length;
        items[first] = item;
        size++;
    }

    // 添加元素到尾部
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[last] = item;
        // 向后移动 last，并取模
        last = (last + 1) % items.length;
        size++;
    }

    // 删除头部元素
    public T removeFirst() {
        if (isEmpty()) return null;
        T result = items[first];
        items[first] = null;          // 帮助 GC
        first = (first + 1) % items.length;
        size--;
        shrinkCapacity();
        return result;
    }

    // 删除尾部元素
    public T removeLast() {
        if (isEmpty()) return null;
        // 注意：last 指向的是下一个可插入的位置，所以最后一个元素在 (last-1) 处
        int lastIndex = (last - 1 + items.length) % items.length;
        T result = items[lastIndex];
        items[lastIndex] = null;
        last = lastIndex;
        size--;
        shrinkCapacity();
        return result;
    }

    // 获取索引 i 处的元素（0 为队首）
    public T get(int index) {
        if (index < 0 || index >= size) throw new ArrayIndexOutOfBoundsException();
        int pos = (first + index) % items.length;
        return items[pos];
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

    // 扩容/缩容：不依赖 get 方法，直接按逻辑顺序拷贝
    private void resize(int newCapacity) {
        T[] newArr = (T[]) new Object[newCapacity];
        // 按逻辑顺序拷贝
        for (int i = 0; i < size; i++) {
            newArr[i] = items[(first + i) % items.length];
        }
        items = newArr;
        first = 0;
        last = size;    // last 指向下一个可插入的位置
    }

    // 缩容：当数组长度 ≥ 16 且使用率低于 25% 时缩容为一半
    private void shrinkCapacity() {
        int capacity = items.length;
        if (capacity >= 16 && size * 4 < capacity) {
            int newCapacity = capacity / 2;
            if (newCapacity < INIT_CAPACITY) {
                newCapacity = INIT_CAPACITY;
            }
            resize(newCapacity);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;
            @Override
            public boolean hasNext() {
                return cursor < size;
            }
            @Override
            public T next() {
                if (!hasNext()) throw new ArrayIndexOutOfBoundsException();
                return get(cursor++);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayDeque)) return false;
        ArrayDeque<?> other = (ArrayDeque<?>) o;
        if (size != other.size) return false;
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(other.get(i))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}