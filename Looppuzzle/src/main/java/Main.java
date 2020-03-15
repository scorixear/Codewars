import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Main extends Application {

  public static void main(String[] args) {
    if (args.length > 0) {
      String[] boards = String.join(" ", args).split(";");
      List<List<Character>> array = Arrays.stream(boards[0].split(" ")).map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList());
      List<List<Character>> array2 = Arrays.stream(boards[1].split(" ")).map(s -> s.chars().mapToObj(i -> (char) i).collect(Collectors.toList())).collect(Collectors.toList());

      char[][] mixedBoard = ConvertToArray(array);
      char[][] solvedBoard = ConvertToArray(array2);
      System.out.println(Solver.solve(mixedBoard, solvedBoard));
      return;
    }


    launch(args);
  }

  public static char[][] ConvertToArray(List<List<Character>> array) {
    char[][] finished = new char[array.size()][];
    for (int i = 0; i < array.size(); i++) {
      List<Character> sub = array.get(i);
      char[] subArray = new char[sub.size()];
      for (int j = 0; j < sub.size(); j++) {
        subArray[j] = sub.get(j);
      }
      finished[i] = subArray;
    }
    return finished;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Looppuzzle");
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainFrame.fxml")));
    primaryStage.setScene(new Scene(root, 1200, 700));
    primaryStage.show();
  }
}
