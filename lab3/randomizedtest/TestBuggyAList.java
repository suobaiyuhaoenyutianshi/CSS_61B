package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
  public void testThreeAddThreeRemove() {
      AListNoResizing<Integer> correct = new AListNoResizing<>();
      BuggyAList<Integer> buggy = new BuggyAList<>();

      // 添加三个元素
      correct.addLast(4);
      buggy.addLast(4);

      correct.addLast(5);
      buggy.addLast(5);

      correct.addLast(6);
      buggy.addLast(6);

      // 依次移除并比较
      assertEquals(correct.removeLast(), buggy.removeLast());
      assertEquals(correct.removeLast(), buggy.removeLast());
      assertEquals(correct.removeLast(), buggy.removeLast());
  }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4); // 生成 0~3 的随机数

            if (operationNumber == 0) { // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                // addLast 没有返回值，无需比较
            } else if (operationNumber == 1) { // size
                assertEquals(L.size(), B.size());
            } else if (operationNumber == 2) { // getLast
                // 仅在列表不为空时调用
                if (L.size() > 0) {
                    assertEquals(L.getLast(), B.getLast());
                } else {
                    // 如果列表为空，可以跳过或什么都不做
                    // 注意：也可以重新生成随机数，但简单跳过即可
                }
            } else if (operationNumber == 3) { // removeLast
                if (L.size() > 0) {
                    assertEquals(L.removeLast(), B.removeLast());
                }
            }
        }
    }
}
