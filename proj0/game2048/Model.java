package game2048;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *将棋盘向 SIDE 方向倾斜。如果棋盘因此发生变化，则返回 true 。*这是目的
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        System.out.println("tilt called with side: " + side);
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.修改 this.board（或许还有 this.score），以考虑向侧面 SIDE 倾斜的情况。如果棋盘发生了变化，将 changed 局部变量设置为 true。
        board.setViewingPerspective(side);//改变实例的side，为视觉
        int size = board.size();
        changed = visualNorth(size);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        board.setViewingPerspective(Side.NORTH);//最后要改回来
        return changed;
    }
    //想视觉方向北移动
    // 从北向南处理，每个 tile 向上查找目标位置
    public boolean visualNorth(int size) {
        boolean change = false;
        for (int col = 0; col < size; col++) {
            int moveDistance = size - 1;          // 当前列最北的可放置行（初始为最北）
            boolean[] merge = new boolean[size];  // 标记某行是否已合并（避免二次合并）
            for (int row = size - 1; row >= 0; row--) { // 从北向南遍历
                Tile t = board.tile(col, row);
                if (t == null) continue;
                int changeLocation = tileLocation(t, moveDistance, col, row, merge);
                 if (changeLocation != row) {
                    change = true;                 // 有棋子移动或合并
                }
                moveDistance = changeLocation ; // 更新可放置的最北位置（当前行已被占）
            }
        }
        return change;
    }

    // 查找可合并的相同值（向上找第一个相同且未合并的 tile）
    public int findEqualValue(Tile t, int moveDistance, int col, int row, boolean[] merge) {
        int find = row + 1; // 从当前行的上一行开始
        while (find <= moveDistance) {
            Tile t2 = board.tile(col, find);
            if (t2 != null) {
                if (t2.value() == t.value() && !merge[find]) {
                    merge[find] = true;               // 标记该位置已合并
                    this.score += t2.value() * 2;      // 加上新方块的值
                    return find;                       // 合并到 find 位置
                }
            }
            find++;
        }
        return row; // 未找到可合并的
    }

    // 遇到不同值阻挡时，放在阻挡块的下方（即 find-1）
    public int findDifferent(Tile t, int moveDistance, int col, int row, boolean[] merge) {
        int find = row + 1;
        while (find <= moveDistance) {
            Tile t2 = board.tile(col, find);
            if (t2 != null) {
                int target = find - 1; // 放在这个阻挡块的下方
                if (target >= row) {
                    return target;
                } else {
                    return row; // 理论上不会执行
                }
            }
            find++;
        }
        return row;
    }

    // 向上找第一个空位
    public int aTleastNull(int moveDistance, int col, int row) {

        for (int lit = moveDistance;lit > row;lit--) {
            if (board.tile(col, lit) == null) {
                return lit;
            }
        }
        return row;
    }
    public int tileLocation(Tile t, int moveDistance, int col, int row, boolean[] merge) {
        // 1. 尝试合并
        int changLocation = findEqualValue(t, moveDistance, col, row, merge);
        if (changLocation != row) {
            board.move(col, changLocation, t);
            return changLocation;
        }
        // 2. 尝试处理不同值阻挡
        changLocation = findDifferent(t, moveDistance, col, row, merge);
        if (changLocation != row) {
            board.move(col, changLocation, t);
            return changLocation;
        }
        // 3. 尝试找空位
        changLocation = aTleastNull(moveDistance, col, row);
        if (changLocation != row) {
            board.move(col, changLocation, t);
            return changLocation;
        }
        return row;
    }
    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        for(int col = 0; col < size; col++){
            for (int row = 0; row < size; row++){
                if(b.tile(col, row) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size();
        for(int col = 0; col < size; col++){

            for(int row = 0; row < size; row++){
                Tile t = b.tile(col, row);
                if(t != null && b.tile(col, row).value() == MAX_PIECE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        return emptySpaceExists(b) || hasAdjacentEqual(b);
    }
    private static boolean hasAdjacentEqual(Board b) {
        int size = b.size();
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                Tile t = b.tile(col, row);
                if (t == null) continue; // 空格不参与合并
                int val = t.value();

                // 检查右边
                if (col + 1 < size) {
                    Tile right = b.tile(col + 1, row);
                    if (right != null && right.value() == val) {
                        return true;
                    }
                }
                // 检查上边（根据你的坐标系，row+1 可能表示向上）
                if (row + 1 < size) {
                    Tile above = b.tile(col, row + 1);
                    if (above != null && above.value() == val) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
