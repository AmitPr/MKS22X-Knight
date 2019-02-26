import java.util.ArrayList;;

public class KnightBoard {
    private static final int[][] offsets = { { 1, 2 }, { 2, 1 }, { 2, -1 }, { 1, -2 }, { -1, -2 }, { -2, -1 },
            { -2, 1 }, { -1, 2 } };

    public static void main(String[] args) {
        for (int i = 64; i < 100; i++) {
            KnightBoard kb = new KnightBoard(i, i);
            System.out.println("Start - " + i + "x" + i);
            long start = System.nanoTime();
            System.out.println(kb.solve(0, 0));
            System.out.println((System.nanoTime() - start) / 100000 + " ms - done");
        }
        //
    }

    public int[][] moves;
    public int[][] possible;
    private int m;
    private int n;

    /**
     * @throws IllegalArgumentException when either parameter is <= 0.
     **/
    public KnightBoard(int m, int n) {
        if (m <= 0 || n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        moves = new int[n][m];
        possible = new int[n][m];
        this.m = m;
        this.n = n;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                if (y == 0 || y == n - 1) {
                    if (x == 0 || x == m - 1) {
                        possible[y][x] = 2;
                    } else if (x < 2 || x >= m - 2) {
                        possible[y][x] = 3;
                    } else {
                        possible[y][x] = 4;
                    }
                } else if (y == 1 || y == n - 2) {
                    if (x == 0 || x == m - 1) {
                        possible[y][x] = 3;
                    } else if (x < 2 || x >= m - 2) {
                        possible[y][x] = 4;
                    } else {
                        possible[y][x] = 6;
                    }
                } else {
                    if (x == 0 || x == m - 1) {
                        possible[y][x] = 4;
                    } else if (x < 2 || x >= m - 2) {
                        possible[y][x] = 6;
                    } else {
                        possible[y][x] = 8;
                    }
                }
            }
        }
    }

    /**
     * Modifies the board by labeling the moves from 1 (at startingRow,startingCol)
     * up to the area of the board in proper knight move steps.
     * 
     * @throws IllegalStateException    when the board contains non-zero values.
     * @throws IllegalArgumentException when either parameter is negative or out of
     *                                  bounds.
     * @return true when the board is solvable from the specified starting position
     */
    public boolean solve(int startRow, int startCol) {
        if (startRow < 0 || startRow > m || startCol < 0 || startCol > n) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (moves[j][i] != 0) {
                    throw new java.lang.IllegalStateException();
                }
            }
        }
        return solveHelper(startCol, startRow, 1);
    }

    // Recursive helper for solve()
    private boolean solveHelper(int x, int y, int curJump) {
        moves[y][x] = curJump;
        if (curJump < m * n) {
            ArrayList<Integer> sortedIndexes = new ArrayList<Integer>();
            ArrayList<Integer> sortedValues = new ArrayList<Integer>();
            for (int i = 0; i < 8; i++) {
                int nextX = offsets[i][0] + x;
                int nextY = offsets[i][1] + y;
                if (nextX >= 0 && nextX < m && nextY >= 0 && nextY < n) {
                    if (moves[nextY][nextX] == 0) {
                        possible[nextY][nextX] -= 1;
                        int val = possible[nextY][nextX];
                        if (sortedIndexes.size() == 0) {
                            sortedValues.add(0, val);
                            sortedIndexes.add(0, i);
                        } else {
                            for (int curIndex = 0; curIndex < sortedValues.size(); curIndex++) {
                                if (val < sortedValues.get(curIndex)) {
                                    sortedValues.add(curIndex, val);
                                    sortedIndexes.add(curIndex, i);
                                    break;
                                }
                                curIndex++;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < sortedIndexes.size(); i++) {
                int index = sortedIndexes.get(i);
                int nextX = offsets[index][0] + x;
                int nextY = offsets[index][1] + y;
                if (solveHelper(nextX, nextY, curJump + 1)) {
                    return true;
                }
            }
            for (int i = 0; i < 8; i++) {
                int nextX = offsets[i][0] + x;
                int nextY = offsets[i][1] + y;
                if (nextX >= 0 && nextX < m && nextY >= 0 && nextY < n) {
                    possible[nextY][nextX] += 1;
                }
            }
            moves[y][x] = 0;
            return false;
        }
        return true;
    }

    /**
     * @throws IllegalStateException    when the board contains non-zero values.
     * @throws IllegalArgumentException when either parameter is negative or out of
     *                                  bounds.
     * @return the number of solutions from the starting position specified
     */
    public int countSolutions(int startRow, int startCol) {
        if (startRow < 0 || startRow > m || startCol < 0 || startCol > n) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (moves[j][i] != 0) {
                    throw new java.lang.IllegalStateException();
                }
            }
        }
        return countHelper(startCol, startRow, 1);
    }

    // Recursive helper for countSolutions()
    private int countHelper(int x, int y, int curJump) {
        moves[y][x] = curJump;
        if (curJump < m * n) {
            int sum = 0;
            for (int i = 0; i < 8; i++) {
                int nextX = offsets[i][0] + x;
                int nextY = offsets[i][1] + y;
                if (nextX >= 0 && nextX < m && nextY >= 0 && nextY < n) {
                    if (moves[nextY][nextX] == 0) {
                    sum += countHelper(nextX, nextY, curJump + 1);
                    }
                }
            }
            moves[y][x] = 0;
            return sum;
        }
        moves[y][x] = 0;
        return 1;
    }

    // Determines whether a board square is available.
    /*
    private boolean canMove(int x, int y) {
        if (x >= 0 && x < m && y >= 0 && y < n) {
            if (moves[y][x] == 0) {
                return true;
            }
        }
        return false;
    }*/

    // Returns the board.
    public String toString() {
        String board = "";
        int spacer = ((m * n) + "").length() + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int x = moves[i][j];
                int len = (x + "").length();
                for (int l = len; l < spacer; l++) {
                    board += " ";
                }
                board += x;
            }
            board += "\n";
        }
        return board;
    }
}