package org.example.lab2;

import java.util.*;

public record EightPuzzle(int[][] board) {

    private final static int[][] GOAL = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0},
    };

    public int getHeuristic() {
        int count = 0;
        for (int i = 0; i < GOAL.length; i++) {
            for (int j = 0; j < GOAL[i].length; j++) {
                if (GOAL[i][j] != board[i][j]) {
                    count++;
                }
            }
        }
        return count;
        // manhattan distance
//        var sum = 0;
//        for (int i = 0; i < GOAL.length; i++) {
//            for (int j = 0; j < GOAL[i].length; j++) {
//                var indexes = findTileIndex(GOAL[i][j]);
//                sum += Math.abs(i - indexes.getKey()) + Math.abs(j - indexes.getValue());
//            }
//        }
//        return sum;
    }

    public boolean isSolved() {
        return Arrays.deepEquals(board, GOAL);
    }

    private boolean isSolvable() {
        int inversions = 0;
        int[] boardAsArray = new int[9];
        int k = 0;
        for (int[] row : board) {
            for (int cell : row) {
                boardAsArray[k++] = cell;
            }
        }
        for (int i = 0; i < boardAsArray.length; i++) {
            for (int j = i + 1; j < boardAsArray.length; j++) {
                if (boardAsArray[i] != 0 && boardAsArray[j] != 0 && boardAsArray[i] > boardAsArray[j]) {
                    inversions++;
                }
            }
        }
        return inversions % 2 == 0;
    }

    private AbstractMap.SimpleImmutableEntry<Integer, Integer> findTileIndex(int tile) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == tile) {
                    return new AbstractMap.SimpleImmutableEntry<>(i, j);
                }
            }
        }
        throw new IllegalArgumentException("The tile " + tile + " is not present in the board");
    }

    public List<EightPuzzle> getChildStates() {
        var list = new ArrayList<EightPuzzle>();

        var indexes = findTileIndex(0);
        int i = indexes.getKey(), j = indexes.getValue();

        if (i > 0) {
            var child = new EightPuzzle(board);
            child.board()[i][j] = child.board()[i - 1][j];
            child.board()[i - 1][j] = 0;
            list.add(child);
        }
        if (i < 2) {
            var child = new EightPuzzle(board);
            child.board()[i][j] = child.board()[i + 1][j];
            child.board()[i + 1][j] = 0;
            list.add(child);
        }
        if (j > 0) {
            var child = new EightPuzzle(board);
            child.board()[i][j] = child.board()[i][j - 1];
            child.board()[i][j - 1] = 0;
            list.add(child);
        }
        if (j < 2) {
            var child = new EightPuzzle(board);
            child.board()[i][j] = child.board()[i][j + 1];
            child.board()[i][j + 1] = 0;
            list.add(child);
        }

        return list;
    }

    public EightPuzzle(int[][] board) {
        if (board.length != 3) {
            throw new IllegalArgumentException("The board must have 3 rows");
        }
        for (int[] row : board) {
            if (row.length != 3) {
                throw new IllegalArgumentException("The board must have 3 columns");
            }
        }
        var presentValues = new Boolean[9];
        for (int[] row : board) {
            for (int cell : row) {
                try {
                    presentValues[cell] = true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("The board must contain only values from 0 to 8");
                }
            }
        }
        if (Arrays.stream(presentValues).anyMatch(Objects::isNull)) {
            for (var row : board) {
                System.out.println(Arrays.toString(row));
            }
            throw new IllegalArgumentException("The board must contain all values from 0 to 8");
        }
        this.board = new int[3][3];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
        }
        if (!isSolvable()) {
            throw new IllegalArgumentException("The puzzle is not solvable");
        }
    }

    @Override
    public String toString() {
        return "EightPuzzle: {\n" +
                "Board:\n" +
                Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]") +
                "\n}";
    }

}
