package com.example.sudokusolver.Controller;

import com.example.sudokusolver.Model.AlgorithmXSolver;
import com.example.sudokusolver.Model.SudokuType;
import com.example.sudokusolver.SudokuSolver;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class GameController {
    static MediaPlayer mediaPlayer;
    static MediaPlayer soundPlayer;
    static MediaPlayer masterPlayer;

    private MediaPlayer pencilPlayer;

    private static SudokuType type;
    private static int SIZE, N;
    private static String fileName;
    private int starsAmount = 3;
    private static boolean completed = false;
    private static boolean puzzleStarted = false;

    private static Long threeStarsTime = 0L;
    private static Long twoStarsTime = 0L;
    private static Long oneStarsTime = 0L;

    private static int[][] Grid; // should be original
    private static int[][] solveGrid;
    private static int[][] currentGrid; // should be current

    static Media rosedoku;
    static Media click;
    static Media pencil;

    private boolean started = false;

    @FXML
    private StackPane stackPaneGrid;

    @FXML
    private Button PauseButton;

    @FXML
    private Button StartButton;

    @FXML
    private Button SaveButton;

    @FXML
    private Button PencilButton;

    @FXML
    private VBox hintBox;

    @FXML
    private Label timeLabel;

    @FXML
    private ImageView star3D;

    @FXML
    private ImageView star2D;

    @FXML
    private ImageView star1D;

    @FXML
    private Button hintsButton;

    private GridPane gridPaneFinal;

    private Timeline timeLine;
    private static Long millisecondsElapsed = 0L;

    private Rectangle privacyScreen;
    private VBox winnerScreen;
    private VBox loserScreen;

    private boolean pencilMark = false;
    private boolean hintVisible = false;

    private String pencilButtonStyle;
    private String pauseButtonStyle;

    private int remainingClues;
    private boolean prevValid = true;
    private static boolean hintsUsed = false;

    private int[] lastHighlightedCell;

    private boolean cellHintOn = false;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize(){
        Long hours1 = millisecondsElapsed/3600000;
        Long minutes1 = (millisecondsElapsed%3600000) /60000;
        Long seconds1 = (millisecondsElapsed%60000)/1000;
        Long milliseconds1 = millisecondsElapsed%1000/10;
        if(minutes1 > 60){
            timeLabel.setText(String.format("%02d:%02d:%02d", hours1, minutes1, seconds1));
        }else{
            timeLabel.setText(String.format("%02d:%02d:%02d", minutes1, seconds1, milliseconds1));
        }

        timeLine = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    millisecondsElapsed = millisecondsElapsed + 100;

                    String formattedTime;

                    Long hours = millisecondsElapsed/3600000;
                    Long minutes = (millisecondsElapsed%3600000) /60000;
                    Long seconds = (millisecondsElapsed%60000)/1000;
                    Long milliseconds = millisecondsElapsed%1000/10;
                    if(minutes > 60){
                        formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                    }else{
                        formattedTime = String.format("%02d:%02d:%02d", minutes, seconds, milliseconds);
                    }

                    timeLabel.setText(formattedTime);

                    if(millisecondsElapsed >= threeStarsTime){
                        if(!star3D.isVisible()) {
                            starsAmount--;
                        }
                        star3D.setVisible(true);

                    }
                    if(millisecondsElapsed >= twoStarsTime){
                        if(!star2D.isVisible()) {
                            starsAmount--;
                        }
                        star2D.setVisible(true);
                    }
                    if (millisecondsElapsed >= oneStarsTime){
                        if(!star1D.isVisible()) {
                            starsAmount--;
                        }
                        star1D.setVisible(true);
                    }
                    if(hintsUsed){
                        star1D.setVisible(true);
                        star2D.setVisible(true);
                        star3D.setVisible(true);
                    }
                })
        );
        timeLine.setCycleCount(Timeline.INDEFINITE);


        URL pathPencil = SudokuSolver.class.getResource("/Music/Pencil.mp3");

        assert pathPencil != null;
        pencil = new Media(pathPencil.toExternalForm());

        pencilPlayer = new MediaPlayer(pencil);

        if(currentGrid == null) {
            currentGrid = Arrays.stream(Grid).map(int[]::clone).toArray(int[][]::new);
        }
        AlgorithmXSolver solver = new AlgorithmXSolver(Grid, SIZE, type);

        ArrayList<int[][]> solutions = solver.getFoundSolutions();
        if(solutions.size() == 1){
            solveGrid = solutions.get(0);
        }

        GridPane grid = new GridPane();
        gridPaneFinal = grid;

        grid.setOnMouseClicked(e ->{
            Node node = e.getPickResult().getIntersectedNode();
            if(!(node instanceof TextField)){
                grid.requestFocus();
            }
        });

        grid.setStyle("-fx-background-color: rgba(0, 0, 0, 0.67);");

        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

        double gridSize = 750/N;

        winnerScreen();

        loserScreen();


        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                int value = Grid[row][col];// Replace this with your own function that returns the grid value at row,col
                int saveValue = currentGrid[row][col];
                boolean saveCell = false;
                if(saveValue != value){
                    saveCell = true;
                }
                StackPane sp = new StackPane();

                sp.getStyleClass().add("cell");
                sp.pseudoClassStateChanged(right, col%SIZE == SIZE-1 && col != N-1);
                sp.pseudoClassStateChanged(bottom, row%SIZE == SIZE-1 && row != N-1);

                sp.setPrefSize(gridSize, gridSize);
                sp.setAlignment(Pos.CENTER);

                if (value == 0) { // add a check for if part of current or part of original
                    zeroCell(grid, gridSize, row, col, saveValue, saveCell, sp);
                } else {
                    Label label = new Label(Integer.toString(value));
                    label.setStyle("-fx-text-fill: rgba(255,255,255,0.62)");
                    sp.getChildren().add(label);
                }
                grid.add(sp, col, row);
            }
        }
        grid.setMinSize(750, 750);
        grid.setPrefSize(750, 750);
        grid.setMaxSize(750, 750);

        grid.setGridLinesVisible(true);

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(750);
        rectangle.setHeight(750);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(1);

        stackPaneGrid.getChildren().addAll(grid, rectangle);
    }

    private void zeroCell(GridPane grid, double gridSize, int row, int col, int saveValue, boolean saveCell, StackPane sp) {
        remainingClues++;
        TextField textField = new TextField();

        // Initialize the pencil mark labels
        GridPane pencilMarks = new GridPane();
        pencilMarks.setMaxSize(gridSize, gridSize);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Label num = new Label();
                num.setAlignment(Pos.CENTER);
                num.setPrefSize(gridSize / 3, gridSize / 3);
                num.setStyle("-fx-font-size: 12");
                num.setTextFill(Color.WHITE);
                num.setOpacity(0.7);
                pencilMarks.add(num, i, j);
            }
        }

        sp.getChildren().add(pencilMarks);

        textField.setPrefColumnCount(1);
        textField.setAlignment(Pos.CENTER);
        textField.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-control-inner-background: transparent;");
        if(saveCell) {
            textField.setText(String.valueOf(saveValue));
        }
        textFieldManaging(row, col, textField, pencilMarks);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                sp.setStyle("-fx-border-color: #7FB8DA;");
                lastHighlightedCell = new int[]{row, col};
            } else if (grid.isFocused()) {
                sp.setStyle("-fx-border-color: transparent;");
                lastHighlightedCell = new int[]{-1, -1};
            } else if(hintsButton.isFocused()){
                sp.setStyle("-fx-border-color: #7FB8DA;");
            }else{
                sp.setStyle("-fx-border-color: transparent;");
            }
            if(!started && col == 8 && row == 0){
                sp.setStyle("-fx-border-color: transparent;");
                lastHighlightedCell = new int[]{-1, -1};
            }
        });

        // if part of current, set the correct text in the field
        sp.getChildren().add( textField);
    }

    private void textFieldManaging(int row, int col, TextField textField, GridPane pencilMarks) {

        int block = row / SIZE * SIZE + col / SIZE;

        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue.length() > 1){
                textField.setText(newValue.substring(1, 2));
            }
            else if (!newValue.matches("[1-" + N + "]")) {
                textField.setText("");
            }else{
                int currValue = Integer.parseInt(newValue);
                int i = (currValue - 1) / SIZE;
                int j = (currValue - 1) % SIZE;
                if(!pencilMark) {
                    currentGrid[row][col] = Integer.parseInt(newValue);
                    pencilMarks.getChildren()
                            .forEach(child -> ((Label)child).setText(""));


                    pencilPlayer.seek(Duration.ZERO);
                    pencilPlayer.play();
                }
                else{
                    Label currNum = (Label) pencilMarks.getChildren().stream()
                            .filter(child -> GridPane.getRowIndex(child) == i && GridPane.getColumnIndex(child) == j)
                            .findFirst().orElse(null);
                    if(currNum != null){
                        if(Objects.equals(currNum.getText(), newValue)){
                            currNum.setText("");
                        }else {
                            currNum.setText(newValue);
                        }
                        textField.setText("");
                    }
                }
            }
            if(cellHintOn){
                if(currentGrid[row][col] == solveGrid[row][col]) {
                    textField.setStyle("-fx-text-fill: #8ed78e;");
                }else{
                    textField.setStyle("-fx-text-fill: #fd7878;");
                }
            }else {
                textField.setStyle("-fx-text-fill: white");
            }

            if(gridFilled() && !newValue.equals("")){
                checkWinningCondition();
            }

        });
    }

    private boolean gridFilled(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(currentGrid[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private void loserScreen() {
        loserScreen = new VBox();
        loserScreen.setAlignment(Pos.CENTER);
        loserScreen.setSpacing(25);
        loserScreen.setMaxSize(750, 350);
        loserScreen.setStyle("-fx-background-color: rgba(0,0,0,0.87)");

        Label loser = new Label("So close! \n One or more cells are not correct :(");
        loser.setStyle("-fx-font-size: 42px;\n" +
                "-fx-background-color: rgba(0, 0, 0, 0.5)");
        loser.setAlignment(Pos.CENTER);
        loser.setWrapText(true);

        Button loserButton = new Button("Try Again");
        loserButton.setOnAction(e-> {
            soundPlayer.seek(Duration.ZERO);
            soundPlayer.play();
            stackPaneGrid.getChildren().remove(stackPaneGrid.getChildren().size() - 1);
            PencilButton.setDisable(false);
            PauseButton.setDisable(false);
            SaveButton.setDisable(false);
            timeLine.play();
        });
        loserScreen.getChildren().addAll(loser, loserButton);
    }

    private void winnerScreen() {
        winnerScreen = new VBox();
        winnerScreen.setAlignment(Pos.CENTER);
        winnerScreen.setSpacing(25);

        Label winner = new Label("WINNER");
        winner.setStyle("-fx-font-size: 72px;\n" +
                "-fx-background-color: rgba(0,0,0,0.5)");
        Button winnerButton = new Button("View Score");
        winnerButton.setOnAction(e -> {
            try {
                changeToWin(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        winnerScreen.getChildren().addAll(winner, winnerButton);
    }

    /* Helper function for the printing of Sudoku puzzle. This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth(StringBuilder sb, String text, int width) {
        for (int i = 0; i < width - text.length(); i++) {
            sb.append(" ");
        }
        sb.append(text);
    }

    /* The print() function outputs the Sudoku grid to a string, using a bit of
     * extra formatting to make the result clearly readable. */
    public String prepareString(int[][] grid) {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuilder sb = new StringBuilder();
        sb.append("-".repeat(Math.max(0, lineLength)));
        sb.append("\n");

        // Go through the Grid, printing out its values separated by spaces
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                printFixedWidth(sb, String.valueOf(grid[i][j]), digits);
                // Print the vertical lines between boxes
                if ((j < N - 1) && ((j + 1) % SIZE == 0)) {
                    sb.append(" |");
                }
                sb.append(" ");
            }
            sb.append("\n");

            // Print the horizontal line between boxes
            if ((i < N - 1) && ((i + 1) % SIZE == 0)) {
                sb.append("-".repeat(Math.max(0, lineLength)));
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private void checkWinningCondition(){
        if(Arrays.deepEquals(currentGrid, solveGrid)){
            stackPaneGrid.getChildren().add(winnerScreen);
            winnerScreen.requestFocus();
        }else{
            stackPaneGrid.getChildren().add(loserScreen);
            loserScreen.requestFocus();
        }
        PencilButton.setDisable(true);
        PauseButton.setDisable(true);
        SaveButton.setDisable(true);
        timeLine.pause();
    }

    @FXML
    private void turnOnPencil(ActionEvent e) throws IOException{
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        if(pencilMark){
            pencilMark = false;
            ((Button)e.getSource()).setStyle(pencilButtonStyle);
        }else{
            pencilMark = true;
            pencilButtonStyle = ((Button)e.getSource()).getStyle();
            ((Button)e.getSource()).setStyle("-fx-background-color: linear-gradient(to bottom right, #c97fda, #b991d0);\n" +
                    "    -fx-background-radius: 25px;\n" +
                    "    -fx-border-radius: 25px;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-font-size: 18px;\n" +
                    "    -fx-font-family: 'Dancing Script', cursive;\n" +
                    "    -fx-padding: 10px 20px;\n" +
                    "    -fx-cursor: hand;\n" +
                    "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);");
        }
    }

    @FXML
    private void startPuzzle(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        if (!started) {
            gridPaneFinal.requestFocus();
            lastHighlightedCell = new int[]{-1, -1};
            StackPane stackPane = stackPaneGrid; // get reference to your StackPane
            if (!stackPane.getChildren().isEmpty()) {
                privacyScreen = (Rectangle) stackPane.getChildren().remove(stackPane.getChildren().size() - 1);
            }
            started = true;
            timeLine.play();
            ((Button)e.getSource()).setVisible(false);
            PauseButton.setVisible(true);
            SaveButton.setVisible(true);
            SaveButton.setDisable(true);
        }
    }

    @FXML
    private void changeToWin(ActionEvent e) throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        //change to winner screen
        timeLine.stop(); // closes the full timeline

        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("win.fxml"));
        WinController.setFileName(fileName);
        WinController.setHintsUsed(hintsUsed);
        WinController.setMillisecond(millisecondsElapsed);
        WinController.setStarsAmount(hintsUsed ? 0 : starsAmount);
        WinController.setTimeOneStar(oneStarsTime, twoStarsTime, threeStarsTime);
        WinController.setGrid(Grid);
        WinController.setSolveGrid(solveGrid);
        root = loader.load();

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(3));
        pauseTransition.setOnFinished(event -> {stage = (Stage) (((Node) e.getSource()).getScene().getWindow());
            scene = stage.getScene();
            scene.setRoot(root);
        });
        pauseTransition.play();
    }

    @FXML
    private void solveRow(ActionEvent e){
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        hintsUsed = true;
        int row = lastHighlightedCell[0];
        int lastCol = lastHighlightedCell[1];

        for(int col = 0; col < N; col++){

            int correct = solveGrid[row][col];

            int current = currentGrid[row][col];

            if(correct != current || col == lastCol){
                StackPane child = (StackPane) getStackPaneInGrid(row, col);

                Label solvedCell = new Label(String.valueOf(correct));
                solvedCell.setStyle("-fx-text-fill: #8ed78e");

                assert child != null;
                child.getChildren().remove(child.getChildren().size()- 1);
                child.getChildren().add(solvedCell);

                currentGrid[row][col] = solveGrid[row][col];
                remainingClues--;

                if(col == lastCol){
                    child.setStyle("-fx-border-color: transparent;");
                }
            }
        }

        if(remainingClues == 0){
            checkWinningCondition();
        }
    }

    @FXML
    private void solveColumn(ActionEvent e){
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        hintsUsed = true;
        int lastRow = lastHighlightedCell[0];
        int lastCol = lastHighlightedCell[1];

        for(int row = 0; row < N; row++){

            int correct = solveGrid[row][lastCol];

            int current = currentGrid[row][lastCol];

            if(correct != current || row == lastRow){
                StackPane child = (StackPane) getStackPaneInGrid(row, lastCol);

                Label solvedCell = new Label(String.valueOf(correct));
                solvedCell.setStyle("-fx-text-fill: #8ed78e");

                assert child != null;
                child.getChildren().remove(child.getChildren().size()- 1);
                child.getChildren().add(solvedCell);

                currentGrid[row][lastCol] = solveGrid[row][lastCol];
                remainingClues--;

                if(row == lastRow){
                    child.setStyle("-fx-border-color: transparent;");
                }
            }
        }
        if(remainingClues == 0){
            checkWinningCondition();
        }
    }

    @FXML
    private void solveBlock(ActionEvent e){
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        hintsUsed = true;
        int blockRowStart = lastHighlightedCell[0] / SIZE * SIZE;
        int blockColStart = lastHighlightedCell[1] / SIZE * SIZE;

        for(int row = blockRowStart; row < blockRowStart + SIZE; row++){
            for(int col = blockColStart; col < blockColStart + SIZE; col++){

                int correct = solveGrid[row][col];
                int current = currentGrid[row][col];

                if(correct != current || (row == lastHighlightedCell[0] && col == lastHighlightedCell[1])){
                    StackPane child = (StackPane) getStackPaneInGrid(row, col);

                    Label solvedCell = new Label(String.valueOf(correct));
                    solvedCell.setStyle("-fx-text-fill: #8ed78e");

                    assert child != null;
                    child.getChildren().remove(child.getChildren().size() - 1);
                    child.getChildren().add(solvedCell);

                    currentGrid[row][col] = solveGrid[row][col];
                    remainingClues--;

                    if(row == lastHighlightedCell[0] && col == lastHighlightedCell[1]){
                        child.setStyle("-fx-border-color: transparent;");
                    }
                }
            }
        }

        if(remainingClues == 0){
            checkWinningCondition();
        }
    }

    @FXML
    private void solveCell(ActionEvent e){ // need dynamic... check
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        hintsUsed = true;
        cellHintOn = true;
        for(int row = 0; row < N; row++){
            for(int col = 0; col < N; col++){
                StackPane child2 = (StackPane) getStackPaneInGrid(row, col);

                Node textField = child2.getChildren().get(child2.getChildren().size()-1);

                if(textField instanceof TextField){
                    if(currentGrid[row][col] == solveGrid[row][col]){
                        textField.setStyle("-fx-text-fill: #8ed78e");
                    }else{
                        textField.setStyle("-fx-text-fill: #fd7878");
                    }
                }
            }
        }

        if(remainingClues == 0){
            checkWinningCondition();
        }
    }

    private Node getStackPaneInGrid(int row, int col){
        for (Node node : gridPaneFinal.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == col) {
                return node;
            }
        }
        return null;
    }

    @FXML
    private void saveGame(ActionEvent e) throws IOException {

        File f = new File(System.getProperty("user.dir")+ File.separator+"Saves"+File.separator + SIZE + "x" + SIZE+File.separator+fileName);

        FileWriter fw = new FileWriter(f);
        if(completed) {
            fw.write(starsAmount + "\n\n");
        }else{
            fw.write(0 + "\n\n");
        }
        fw.write((hintsUsed ? 1 : 0) + "\n\n");
        fw.write(millisecondsElapsed + "\n\n");
        fw.write(threeStarsTime + "\n\n");
        fw.write(twoStarsTime + "\n\n");
        fw.write(oneStarsTime + "\n\n");

        fw.write(prepareString(Grid) + "\n");
        fw.write(prepareString(currentGrid) + "\n");
        fw.write(1 + "\n\n");
        fw.write((completed ? 1:0) + "\n");
        fw.flush();
        fw.close();


        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        FXMLLoader loader = new FXMLLoader(SudokuSolver.class.getResource("stageSelector.fxml"));
        root = loader.load();

        stage = (Stage) (((Node)e.getSource()).getScene().getWindow());
        scene = stage.getScene();
        scene.setRoot(root);
    }

    @FXML
    private void showHints() throws IOException {
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        if(hintVisible){
            hintBox.setVisible(false);
            hintVisible = false;
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Hints about to be used");
            Label content = new Label("Using a hint will cause you to receive 0 roses. Are you sure you want to continue");
            content.setWrapText(true);
            alert.getDialogPane().setContent(content);

            alert.getDialogPane().getStylesheets().clear();
            alert.getDialogPane().getStylesheets().add(SudokuSolver.class.getResource("alert.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog");


            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, ButtonType.CANCEL);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeYes){
                soundPlayer.seek(Duration.ZERO);
                soundPlayer.play();
                hintBox.setVisible(true);
                hintVisible = true;
            }
            else{
                soundPlayer.seek(Duration.ZERO);
                soundPlayer.play();
            }
        }
    }

    @FXML
    private void pausePuzzle(ActionEvent e) throws IOException{
        soundPlayer.seek(Duration.ZERO);
        soundPlayer.play();
        if(started){
            started = false;
            stackPaneGrid.getChildren().add(privacyScreen);
            //pause timer
            timeLine.pause();
            pauseButtonStyle = ((Button)e.getSource()).getStyle();
            ((Button)e.getSource()).setStyle("-fx-background-color: linear-gradient(to bottom right, #c97fda, #b991d0);\n" +
                    "    -fx-background-radius: 25px;\n" +
                    "    -fx-border-radius: 25px;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-font-size: 18px;\n" +
                    "    -fx-font-family: 'Dancing Script', cursive;\n" +
                    "    -fx-padding: 10px 20px;\n" +
                    "    -fx-cursor: hand;\n" +
                    "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);");
            SaveButton.setDisable(false);
        }else{
            started = true;
            privacyScreen = (Rectangle) stackPaneGrid.getChildren().remove(stackPaneGrid.getChildren().size() - 1);
            timeLine.play();
            ((Button)e.getSource()).setStyle(pauseButtonStyle);
            SaveButton.setDisable(true);
        }
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer, MediaPlayer soundPlayer, MediaPlayer masterPlayer, Media rosedoku, Media click) {
        GameController.mediaPlayer = mediaPlayer;
        GameController.soundPlayer = soundPlayer;
        GameController.rosedoku = rosedoku;
        GameController.click = click;
        GameController.masterPlayer = masterPlayer;
    }
    public static void setType(SudokuType type){
        GameController.type = type;
    }
    public static void setGrid(int[][] grid) {
        GameController.Grid = grid;
    }
    public static void setSIZE(int SIZE){
        GameController.SIZE = SIZE;
        GameController.N = SIZE*SIZE;
    }
    public static void setFileName(String f) {
        GameController.fileName = f;
    }
    public static void setCurrentGrid(int[][] grid){
        GameController.currentGrid = grid;
    }
    public static void setMillisecond(int milli){
        GameController.millisecondsElapsed = (long) milli;
    }
    public static void setStarTime(int starOne, int starTwo, int starThree){
        GameController.threeStarsTime = (long) starThree;
        GameController.twoStarsTime = (long) starTwo;
        GameController.oneStarsTime = (long) starOne;
    }
    public static void setCompleted(boolean completed) {
        GameController.completed = completed;
    }
    public static void setPuzzleStarted(boolean puzzleStarted) {
        GameController.puzzleStarted = puzzleStarted;
    }
    public static void setHintsUsed(int hintsUsed) {
        boolean hintsUsedB = hintsUsed == 0 ? false : true;
        GameController.hintsUsed = hintsUsedB;
    }
}
