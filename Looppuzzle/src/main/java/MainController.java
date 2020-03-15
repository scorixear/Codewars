import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class MainController {

  @FXML
  public GridPane GameFrame;
  @FXML
  public ComboBox columnComboBox;
  @FXML
  public ComboBox rowComboBox;
  @FXML
  public TextArea gridTextArea;
  @FXML
  public TextArea commandTextArea;
  @FXML
  public Button solveButton;

  private Thread solveThread;

  @FXML
  public void initialize() {
    columnComboBox.getItems().clear();
    columnComboBox.getItems().addAll("2", "3", "4", "5", "6", "7", "8", "9");
    rowComboBox.getItems().clear();
    rowComboBox.getItems().addAll("2", "3", "4", "5", "6", "7", "8", "9");
    columnComboBox.getSelectionModel().clearAndSelect(1);
    rowComboBox.getSelectionModel().clearAndSelect(1);
    initGrid(3, 3);
  }

  @FXML
  public void solve_Click() {
    if (solveButton.getText().equals("Solve")) {
      solveButton.setText("Stop");
    } else {
      solveThread.interrupt();
      solveButton.setText("Solve");
      return;
    }
    solveThread = new Thread(() -> {
      List<String> commands;
      List<Integer> cells = Arrays.stream(gridTextArea.getText().split(" ")).map(Integer::parseInt).collect(Collectors.toList());
      List<Integer> sorted = new ArrayList<>(cells);
      Collections.sort(sorted);
      int rows = Integer.parseInt(rowComboBox.getSelectionModel().getSelectedItem().toString());
      int columns = Integer.parseInt(columnComboBox.getSelectionModel().getSelectedItem().toString());
      char[][] mixedBoard = new char[rows][columns];
      char[][] solvedBoard = new char[rows][columns];
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
          mixedBoard[i][j] = (char) (int) cells.get(i * columns + j);
          solvedBoard[i][j] = (char) (int) sorted.get(i * columns + j);
        }
      }
      commands = Solver.solve(mixedBoard, solvedBoard);
      Platform.runLater(() -> {
        if (commands == null) {
          commandTextArea.setText("Not Solvable");
          solveButton.setText("Solve");
          return;
        }
        commandTextArea.setText(String.join(", ", commands));
      });

      if(commands == null){
        return;
      }
      for (String command : commands) {
        int index = Integer.parseInt(command.trim().substring(1));
        char direction = command.toLowerCase().trim().toCharArray()[0];
        Platform.runLater(() -> {
          switch (direction) {
            case 'r':
              GameFrame.getChildren().stream().filter((c -> GridPane.getRowIndex(c) == index)).collect(Collectors.toList()).forEach(node -> {
                if (GridPane.getColumnIndex(node) == GameFrame.getColumnCount() - 1) {
                  GridPane.setColumnIndex(node, 0);
                } else {
                  GridPane.setColumnIndex(node, GridPane.getColumnIndex(node) + 1);
                }
              });
              break;
            case 'l':
              GameFrame.getChildren().stream().filter((c -> GridPane.getRowIndex(c) == index)).collect(Collectors.toList()).forEach(node -> {
                if (GridPane.getColumnIndex(node) == 0) {
                  GridPane.setColumnIndex(node, GameFrame.getColumnCount() - 1);
                } else {
                  GridPane.setColumnIndex(node, GridPane.getColumnIndex(node) - 1);
                }
              });
              break;
            case 'u':
              GameFrame.getChildren().stream().filter((c -> GridPane.getColumnIndex(c) == index)).collect(Collectors.toList()).forEach(node -> {
                if (GridPane.getRowIndex(node) == 0) {
                  GridPane.setRowIndex(node, GameFrame.getRowCount() - 1);
                } else {
                  GridPane.setRowIndex(node, GridPane.getRowIndex(node) - 1);
                }
              });
              break;
            case 'd':
              GameFrame.getChildren().stream().filter((c -> GridPane.getColumnIndex(c) == index)).collect(Collectors.toList()).forEach(node -> {
                if (GridPane.getRowIndex(node) == GameFrame.getRowCount() - 1) {
                  GridPane.setRowIndex(node, 0);
                } else {
                  GridPane.setRowIndex(node, GridPane.getRowIndex(node) + 1);
                }
              });
              break;
          }
        });
        try {
          int sleepTime = 15000/commands.size();
          Thread.sleep(Math.min(sleepTime, 100));
        } catch (InterruptedException e) {
          //e.printStackTrace();
        }
      }
      Platform.runLater(() -> {
        solveButton.setText("Solve");
      });
    });
    solveThread.start();

  }


  private void initGrid(int row, int column) {

    GameFrame.getColumnConstraints().clear();
    GameFrame.getRowConstraints().clear();
    for (int i = 0; i < row; i++) {
      RowConstraints rowConstraints = new RowConstraints();
      rowConstraints.setPercentHeight(100.0 / row);
      rowConstraints.setFillHeight(true);
      GameFrame.getRowConstraints().add(rowConstraints);
    }
    for (int i = 0; i < column; i++) {
      ColumnConstraints columnConstraints = new ColumnConstraints();
      columnConstraints.setPercentWidth(100.0 / column);
      columnConstraints.setFillWidth(true);
      GameFrame.getColumnConstraints().add(columnConstraints);
    }

  }

  @FXML
  public void generate_Click() {
    int rows = Integer.parseInt(rowComboBox.getSelectionModel().getSelectedItem().toString());
    int columns = Integer.parseInt(columnComboBox.getSelectionModel().getSelectedItem().toString());
    initGrid(rows, columns);

    List<String> cells;
    List<String> sorted;
    if(gridTextArea.getText().trim().isEmpty()) {
      cells = new ArrayList<>();
      for (int i = 0; i < rows * columns; i++) {
        cells.add(i + "");
      }
      sorted = new ArrayList<>(cells);
      Collections.shuffle(cells);
      gridTextArea.setText(String.join(" ", cells));
    }
    else {
      cells = Arrays.asList(gridTextArea.getText().split(" "));
      sorted = new ArrayList<>(cells);
      Collections.sort(sorted);
    }



    GameFrame.getChildren().clear();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        Label label = new Label();
        label.setText(cells.get((i * columns) + j));
        label.setFont(new Font(20));
        double hue = (sorted.indexOf(cells.get(i * columns + j)) / (double) sorted.size()) * 360;
        label.setBackground(new Background(new BackgroundFill(Color.hsb(hue, 0.7, 0.8, 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
        label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN)));
        label.setMaxHeight(Double.MAX_VALUE);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setAlignment(Pos.CENTER);

        GameFrame.add(label, j, i);
      }
    }

  }

}
