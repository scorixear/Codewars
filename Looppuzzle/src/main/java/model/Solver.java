package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

enum Direction {
  UP("U"),
  DOWN("D"),
  LEFT("L"),
  RIGHT("R");
  String direction;

  Direction(String dir) {
    direction = dir;
  }
}
// 1 3 4 2 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20
public class Solver {
  public static List<String> solve(char[][] mixedUpBoard, char[][] solvedBoard) {
    ArrayList<String> moves = new ArrayList<>();
    int rows = solvedBoard.length;
    int columns = solvedBoard[0].length;
    char[][] boardCopy = mixedUpBoard.clone();
    // Solve first row
    for (int i = 0; i < columns; i++) {
      Pair<Integer, Integer> index = findChar(solvedBoard[0][i], boardCopy);
      if (index.row == 0) {
        if (index.column == i) {
          continue;
        }
        moves.addAll(moveBoard(Direction.DOWN, 1, i, boardCopy));
        index.row = 1;
      }
      if (index.column - i < 0) {
        moves.addAll(moveBoard(Direction.RIGHT, i - index.column, index.row, boardCopy));
      } else if (index.column != i) {
        moves.addAll(moveBoard(Direction.LEFT, index.column - i, index.row, boardCopy));
      }
      moves.addAll(moveBoard(Direction.UP, index.row, i, boardCopy));
    }
    // Solve second to second to last
    for (int i = 1; i < rows - 1; i++) {
      for (int j = 0; j < columns; j++) {
        Pair<Integer, Integer> index = findChar(solvedBoard[i][j], boardCopy);
        // if cell is already in correct row, move it away
        if (index.row == i) {
          if (index.column == j) {
            continue;
          }
          moves.addAll(moveBoard(Direction.DOWN, 1, index.column, boardCopy));
          moves.addAll(moveBoard(Direction.RIGHT, 1, i + 1, boardCopy));
          moves.addAll(moveBoard(Direction.UP, 1, index.column, boardCopy));
          index.column = (index.column + 1) % columns;
          index.row += 1;
        }
        if (index.column == j) {
          moves.addAll(moveBoard(Direction.RIGHT, 1, index.row, boardCopy));
          index.column = (index.column + 1) % columns;
        }

        moves.addAll(moveBoard(Direction.DOWN, index.row - i, j, boardCopy));
        if (index.column - j < 0) {
          moves.addAll(moveBoard(Direction.RIGHT, j - index.column, index.row, boardCopy));
        } else {
          moves.addAll(moveBoard(Direction.LEFT, index.column - j, index.row, boardCopy));
        }
        moves.addAll(moveBoard(Direction.UP, index.row - i, j, boardCopy));
      }
    }

    // Handle last row

    // rotate until first element fits
    Pair<Integer, Integer> firstItem = findChar(solvedBoard[rows - 1][0], boardCopy);
    moves.addAll(moveBoard(Direction.LEFT, firstItem.column, rows - 1, boardCopy));

    if(columns <=3) {
      if(boardCopy[rows-1][columns -2] != solvedBoard[rows-1][columns-2] || boardCopy[rows-1][columns-1] != solvedBoard[rows-1][columns-1]){
        System.out.println("Not Possible without last row");
        printBoard(boardCopy);
        return null;
      }
      return moves;
    }

    for(int i = 1; i< columns - 2;i++){
      Pair<Integer, Integer> index = findChar(solvedBoard[rows- 1][i], boardCopy);
      if(index.column == i){
        continue;
      }
      // Select second swap item
      int secondReplacer;
      if (index.column == columns - 1){
        secondReplacer = i + 1;
      } else {
        secondReplacer = index.column + 1;
      }

      // Swap over wanted row
      // Insert replacer into top
      moves.addAll(moveBoard(Direction.UP, 1, i, boardCopy));
      moves.addAll(moveBoard(Direction.LEFT, secondReplacer - i, rows-1, boardCopy));
      moves.addAll(moveBoard(Direction.DOWN, 1, i, boardCopy));


      // move wanted item to position
      if(secondReplacer < index.column) {
        moves.addAll(moveBoard(Direction.LEFT, index.column - secondReplacer, rows-1, boardCopy));
      } else {
        moves.addAll(moveBoard(Direction.RIGHT, secondReplacer - index.column, rows - 1, boardCopy));
      }


      //undo replacer & insert wanted item into correct location
      moves.addAll(moveBoard(Direction.UP, 1, i, boardCopy));
      moves.addAll(moveBoard(Direction.RIGHT, index.column - i, rows-1, boardCopy));
      moves.addAll(moveBoard(Direction.DOWN, 1, i, boardCopy));
    }

    if(boardCopy[rows-1][columns -2] != solvedBoard[rows-1][columns-2] || boardCopy[rows-1][columns-1] != solvedBoard[rows-1][columns-1]){
      System.out.println("Not Possible at two last items");
      printBoard(boardCopy);
      return null;
    }

    return moves;
  }

  private static void printBoard(char[][] boardCopy) {
    for(char[] row : boardCopy) {
      System.out.print("[ ");
      for(char c : row){
        System.out.print((int)c+" ");
      }
      System.out.print("]");
      System.out.println();
    }
  }

  private static List<String> moveBoard(Direction direction, int amount, int index, char[][] board) {
    ArrayList<String> moves = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      moves.add(direction.direction + index);
    }
    char[] temp;
    switch (direction) {
      case UP:
        temp = new char[board.length];
        for (int i = 0; i < board.length; i++) {
          int newIndex = (i - amount)%board.length;
          if (newIndex < 0) {
            newIndex = board.length + newIndex;
          }
          if(newIndex == board.length){
            newIndex = 0;
          }
          temp[newIndex] = board[i][index];
        }
        for (int i = 0; i < board.length; i++) {
          board[i][index] = temp[i];
        }
        break;
      case DOWN:
        temp = new char[board.length];
        for (int i = 0; i < board.length; i++) {
          int newIndex = (i + amount) % board.length;
          if(newIndex < 0){
            newIndex = board.length + newIndex;
          }
          if(newIndex == board.length){
            newIndex = 0;
          }
          temp[newIndex] = board[i][index];
        }
        for (int i = 0; i < board.length; i++) {
          board[i][index] = temp[i];
        }
        break;
      case LEFT:
        temp = new char[board[index].length];
        for (int i = 0; i < board[index].length; i++) {
          int newIndex = (i - amount)%board.length;
          if (newIndex < 0) {
            newIndex = board[index].length + newIndex;
          }
          if(newIndex == board[index].length){
            newIndex = 0;
          }
          temp[newIndex] = board[index][i];
        }
        System.arraycopy(temp, 0, board[index], 0, board[index].length);
        break;
      case RIGHT:
        temp = new char[board[index].length];
        for (int i = 0; i < board[index].length; i++) {
          int newIndex = (i + amount) % board[index].length;
          if(newIndex < 0){
            newIndex = board[index].length + newIndex;
          }
          if(newIndex == board[index].length){
            newIndex = 0;
          }
          temp[newIndex] = board[index][i];
        }
        System.arraycopy(temp, 0, board[index], 0, board[index].length);
        break;
    }
    return moves;
  }

  private static Pair<Integer, Integer> findChar(char search, char[][] board) throws IllegalArgumentException {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == search) {
          return new Pair<>(i, j);
        }
      }
    }
    throw new IllegalArgumentException("No given char found");
  }
}

class Pair<T, K> {
  public T row;
  public K column;

  Pair(T a, K b) {
    this.row = a;
    this.column = b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(row, pair.row) &&
        Objects.equals(column, pair.column);
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }
}
